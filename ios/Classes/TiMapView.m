/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMapView.h"
#import "TiApp.h"
#import "TiBase.h"
#import "TiMapAnnotationProxy.h"
#import "TiMapCircleProxy.h"
#import "TiMapCustomAnnotationView.h"
#import "TiMapImageAnnotationView.h"
#import "TiMapImageOverlayProxy.h"
#import "TiMapMarkerAnnotationView.h"
#import "TiMapModule.h"
#import "TiMapPinAnnotationView.h"
#import "TiMapPolygonProxy.h"
#import "TiMapPolylineProxy.h"
#import "TiMapRouteProxy.h"
#import "TiMapUtils.h"
#import "TiUtils.h"
#import <MapKit/MapKit.h>

@implementation TiMapView

CLLocationCoordinate2D userNewLocation;

#pragma mark Internal

- (void)dealloc
{
  if (map != nil) {
    map.delegate = nil;
    RELEASE_TO_NIL(map);
  }
  if (mapObjects2View) {
    CFRelease(mapObjects2View);
    mapObjects2View = nil;
  }

  selectedAnnotation = nil;
  RELEASE_TO_NIL(locationManager);
  RELEASE_TO_NIL(polygonProxies);
  RELEASE_TO_NIL(polylineProxies);
  RELEASE_TO_NIL(circleProxies);
  RELEASE_TO_NIL(imageOverlayProxies);
  RELEASE_TO_NIL(clusterAnnotations);
  [super dealloc];
}

- (void)render
{
  if (![NSThread isMainThread]) {
    TiThreadPerformOnMainThread(
        ^{
          [self render];
        },
        NO);
    return;
  }
  //TIMOB-10892 if any of below conditions is true , regionthatfits returns invalid.
  if (map == nil || map.bounds.size.width == 0 || map.bounds.size.height == 0) {
    return;
  }

  if (region.center.latitude != 0 && region.center.longitude != 0 && !isnan(region.center.latitude) && !isnan(region.center.longitude)) {
    if (regionFits) {
      [map setRegion:[map regionThatFits:region] animated:animate];
    } else {
      [map setRegion:region animated:animate];
    }
  }
}

- (MKMapView *)map
{
  if (map == nil) {
    map = [[MKMapView alloc] initWithFrame:CGRectZero];
    map.delegate = self;
    map.userInteractionEnabled = YES;
    map.autoresizingMask = UIViewAutoresizingNone;

    [self addSubview:map];
    mapObjects2View = CFDictionaryCreateMutable(NULL, 10, &kCFTypeDictionaryKeyCallBacks, &kCFTypeDictionaryValueCallBacks);
    [self registerTouchEvents];
    //Initialize loaded state to YES. This will automatically go to NO if the map needs to download new data
    loaded = YES;
  }
  return map;
}

- (TiMapCameraProxy *)camera
{
  return [TiMapUtils returnValueOnMainThread:^id {
    return [[TiMapCameraProxy alloc] initWithCamera:[self map].camera];
  }];
}

- (void)registerTouchEvents
{
  UILongPressGestureRecognizer *longPressInterceptor = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(handleLongPressOnMap:)];

  UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleOverlayTap:)];
  tap.cancelsTouchesInView = NO;

  [map addGestureRecognizer:tap];
  [map addGestureRecognizer:longPressInterceptor];

  [longPressInterceptor release];
  [tap release];
}

- (void)handleOverlayTap:(UIGestureRecognizer *)tap
{
  CGPoint tapPoint = [tap locationInView:self.map];

  CLLocationCoordinate2D tapCoord = [self.map convertPoint:tapPoint toCoordinateFromView:self.map];
  MKMapPoint mapPoint = MKMapPointForCoordinate(tapCoord);

  [self handlePolygonClick:mapPoint];
  [self handlePolylineClick:mapPoint];
  [self handleCircleClick:mapPoint];
  [self handleMapClick:mapPoint];
}

- (id)accessibilityElement
{
  return [self map];
}

- (NSArray *)customAnnotations
{
  NSMutableArray *annotations = [NSMutableArray arrayWithArray:self.map.annotations];
  [annotations removeObject:self.map.userLocation];
  return annotations;
}

- (void)willFirePropertyChanges
{
  regionFits = [TiUtils boolValue:[self.proxy valueForKey:@"regionFit"]];
  animate = [TiUtils boolValue:[self.proxy valueForKey:@"animate"]];
}

- (void)didFirePropertyChanges
{
  [self render];
}

- (void)setBounds:(CGRect)bounds
{
  // TIMOB-13102:
  // When the bounds change the mapview fires the regionDidChangeAnimated delegate method
  // Here we update the region property which is not what we want.
  // Instead we set a forceRender flag and render in frameSizeChanged and capture updated
  // region there.
  CGRect oldBounds = (map != nil) ? [map bounds] : CGRectZero;
  forceRender = (oldBounds.size.width == 0 || oldBounds.size.height == 0);
  ignoreRegionChanged = YES;
  [super setBounds:bounds];
  ignoreRegionChanged = NO;
}

- (void)frameSizeChanged:(CGRect)frame bounds:(CGRect)bounds
{
  [[self map] setFrame:bounds];
  [super frameSizeChanged:frame bounds:bounds];
  if (forceRender) {
    //Set this to NO so that region gets captured.
    ignoreRegionChanged = NO;
    [self render];
    forceRender = NO;
  }
}

- (TiMapAnnotationProxy *)annotationFromArg:(id)arg
{
  return [(TiMapViewProxy *)[self proxy] annotationFromArg:arg];
}

- (NSArray *)annotationsFromArgs:(id)value
{
  ENSURE_TYPE_OR_NIL(value, NSArray);
  NSMutableArray *result = [NSMutableArray arrayWithCapacity:[value count]];
  if (value != nil) {
    for (id arg in value) {
      [result addObject:[self annotationFromArg:arg]];
    }
  }
  return result;
}

- (void)refreshAnnotation:(TiMapAnnotationProxy *)proxy readd:(BOOL)yn
{
  NSArray *selected = map.selectedAnnotations;
  BOOL wasSelected = [selected containsObject:proxy]; //If selected == nil, this still returns FALSE.
  ignoreClicks = YES;
  if (yn == NO) {
    [map deselectAnnotation:proxy animated:NO];
  } else {
    [map removeAnnotation:proxy];
    [map addAnnotation:proxy];
    [map setNeedsLayout];
  }
  if (wasSelected) {
    [map selectAnnotation:proxy animated:NO];
  }
  ignoreClicks = NO;
}

- (void)refreshCoordinateChanges:(TiMapAnnotationProxy *)proxy afterRemove:(void (^)())callBack
{
  NSArray *selected = map.selectedAnnotations;
  BOOL wasSelected = [selected containsObject:proxy]; //If selected == nil, this still returns FALSE.
  ignoreClicks = YES;
  [map removeAnnotation:proxy];
  callBack();
  [map addAnnotation:proxy];
  [map setNeedsLayout];
  if (wasSelected) {
    [map selectAnnotation:proxy animated:NO];
  }
  ignoreClicks = NO;
}

#pragma mark Public APIs

- (void)addAnnotation:(id)args
{
  ENSURE_SINGLE_ARG(args, NSObject);
  ENSURE_UI_THREAD(addAnnotation, args);
  [[self map] addAnnotation:[self annotationFromArg:args]];
}

- (void)addAnnotations:(id)args
{
  ENSURE_TYPE(args, NSArray);
  ENSURE_UI_THREAD(addAnnotations, args);
  [[self map] addAnnotations:[self annotationsFromArgs:args]];
}

- (void)removeAnnotation:(id)args
{
  ENSURE_SINGLE_ARG(args, NSObject);

  id<MKAnnotation> doomedAnnotation = nil;

  if ([args isKindOfClass:[NSString class]]) {
    // for pre 0.9, we supported removing by passing the annotation title
    NSString *title = [TiUtils stringValue:args];
    for (id<MKAnnotation> an in self.customAnnotations) {
      if ([title isEqualToString:an.title]) {
        doomedAnnotation = an;
        break;
      }
    }
  } else if ([args isKindOfClass:[TiMapAnnotationProxy class]]) {
    doomedAnnotation = args;
  }

  TiThreadPerformOnMainThread(
      ^{
        [[self map] removeAnnotation:doomedAnnotation];
      },
      NO);
}

- (void)removeAnnotations:(id)args
{
  ENSURE_TYPE(args, NSArray); // assumes an array of TiMapAnnotationProxy, and NSString classes

  // Test for annotation title strings
  NSMutableArray *doomedAnnotations = [NSMutableArray arrayWithArray:args];
  NSUInteger count = [doomedAnnotations count];
  id doomedAn;
  for (int i = 0; i < count; i++) {
    doomedAn = [doomedAnnotations objectAtIndex:i];
    if ([doomedAn isKindOfClass:[NSString class]]) {
      // for pre 0.9, we supported removing by passing the annotation title
      NSString *title = [TiUtils stringValue:doomedAn];
      for (id<MKAnnotation> an in self.customAnnotations) {
        if ([title isEqualToString:an.title]) {
          [doomedAnnotations replaceObjectAtIndex:i withObject:an];
        }
      }
    }
  }

  TiThreadPerformOnMainThread(
      ^{
        [[self map] removeAnnotations:doomedAnnotations];
      },
      NO);
}

- (void)removeAllAnnotations:(id)args
{
  ENSURE_UI_THREAD(removeAllAnnotations, args);
  [self.map removeAnnotations:self.customAnnotations];
}

- (void)setAnnotations_:(id)value
{
  ENSURE_TYPE_OR_NIL(value, NSArray);
  ENSURE_UI_THREAD(setAnnotations_, value)
      [self.map removeAnnotations:self.customAnnotations];
  if (value != nil) {
    [[self map] addAnnotations:[self annotationsFromArgs:value]];
  }
}

- (void)setSelectedAnnotation:(id<MKAnnotation>)annotation
{
  [[self map] selectAnnotation:annotation animated:animate];
}

- (void)selectAnnotation:(id)args
{
  ENSURE_SINGLE_ARG_OR_NIL(args, NSObject);
  ENSURE_UI_THREAD(selectAnnotation, args);

  if (args == nil) {
    for (id<MKAnnotation> annotation in [[self map] selectedAnnotations]) {
      [[self map] deselectAnnotation:annotation animated:animate];
    }
    return;
  }

  if ([args isKindOfClass:[NSString class]]) {
    // for pre 0.9, we supported selecting by passing the annotation title
    NSString *title = [TiUtils stringValue:args];
    for (id<MKAnnotation> an in [NSArray arrayWithArray:[self map].annotations]) {
      if ([title isEqualToString:an.title]) {
        // TODO: Slide the view over to the selected annotation, and/or zoom so it's with all other selected.
        [self setSelectedAnnotation:an];
        break;
      }
    }
  } else if ([args isKindOfClass:[TiMapAnnotationProxy class]]) {
    [self setSelectedAnnotation:args];
  }
}

- (void)deselectAnnotation:(id)args
{
  ENSURE_SINGLE_ARG(args, NSObject);
  ENSURE_UI_THREAD(deselectAnnotation, args);

  if ([args isKindOfClass:[NSString class]]) {
    // for pre 0.9, we supporting selecting by passing the annotation title
    NSString *title = [TiUtils stringValue:args];
    for (id<MKAnnotation> an in [NSArray arrayWithArray:[self map].annotations]) {
      if ([title isEqualToString:an.title]) {
        [[self map] deselectAnnotation:an animated:animate];
        break;
      }
    }
  } else if ([args isKindOfClass:[TiMapAnnotationProxy class]]) {
    [[self map] deselectAnnotation:args animated:animate];
  }
}

- (void)zoom:(id)args
{
  ENSURE_SINGLE_ARG(args, NSObject);
  ENSURE_UI_THREAD(zoom, args);

  double v = [TiUtils doubleValue:args];
  // TODO: Find a good delta tolerance value to deal with floating point goofs
  if (v == 0.0) {
    return;
  }
  MKCoordinateRegion _region = [[self map] region];
  // TODO: Adjust zoom factor based on v
  if (v > 0) {
    _region.span.latitudeDelta = _region.span.latitudeDelta / 2.0002;
    _region.span.longitudeDelta = _region.span.longitudeDelta / 2.0002;
  } else {
    _region.span.latitudeDelta = _region.span.latitudeDelta * 2.0002;
    _region.span.longitudeDelta = _region.span.longitudeDelta * 2.0002;
  }
  region = _region;
  [self render];
}

- (MKCoordinateRegion)regionFromDict:(NSDictionary *)dict
{
  CGFloat latitudeDelta = [TiUtils floatValue:@"latitudeDelta" properties:dict];
  CGFloat longitudeDelta = [TiUtils floatValue:@"longitudeDelta" properties:dict];
  CLLocationCoordinate2D center;
  center.latitude = [TiUtils floatValue:@"latitude" properties:dict];
  center.longitude = [TiUtils floatValue:@"longitude" properties:dict];
  MKCoordinateRegion region_;
  MKCoordinateSpan span;
  span.longitudeDelta = longitudeDelta;
  span.latitudeDelta = latitudeDelta;
  region_.center = center;
  region_.span = span;
  return region_;
}

- (NSDictionary *)dictionaryFromRegion
{
  NSMutableDictionary *theDict = [NSMutableDictionary dictionary];
  [theDict setObject:NUMFLOAT(region.center.latitude) forKey:@"latitude"];
  [theDict setObject:NUMFLOAT(region.center.longitude) forKey:@"longitude"];
  [theDict setObject:NUMFLOAT(region.span.latitudeDelta) forKey:@"latitudeDelta"];
  [theDict setObject:NUMFLOAT(region.span.longitudeDelta) forKey:@"longitudeDelta"];

  return theDict;
}

- (CLLocationDegrees)longitudeDelta
{
  if (loaded) {
    MKCoordinateRegion _region = [[self map] region];
    return _region.span.longitudeDelta;
  }
  return 0.0;
}

- (CLLocationDegrees)latitudeDelta
{
  if (loaded) {
    MKCoordinateRegion _region = [[self map] region];
    return _region.span.latitudeDelta;
  }
  return 0.0;
}

#pragma mark Public APIs

- (void)setMapType_:(id)value
{
  [[self map] setMapType:[TiUtils intValue:value]];
}

- (void)setRegion_:(id)value
{
  if (value == nil) {
    // unset the region and set it back to the user's location of the map
    // what else to do??
    MKUserLocation *user = [self map].userLocation;
    if (user != nil) {
      region.center = user.location.coordinate;
      [self render];
    } else {
      // if we unset but we're not allowed to get the users location, what to do?
    }
  } else {
    region = [self regionFromDict:value];
    [self render];
  }
}

- (void)setAnimate_:(id)value
{
  animate = [TiUtils boolValue:value];
}

- (void)setRegionFit_:(id)value
{
  regionFits = [TiUtils boolValue:value];
  [self render];
}

- (void)setUserLocation_:(id)value
{
  ENSURE_SINGLE_ARG(value, NSObject);

  // Release the locationManager in case it was already created
  RELEASE_TO_NIL(locationManager);
  BOOL userLocation = [TiUtils boolValue:value def:NO];
  // if userLocation is true and this is iOS 8 or greater, then ask for permission
  if (userLocation) {
    // the locationManager needs to be created to permissions
    locationManager = [[CLLocationManager alloc] init];
    // set the "userLocation" on the delegate callback to avoid console warnings from the OS
    locationManager.delegate = self;
    if ([[NSBundle mainBundle] objectForInfoDictionaryKey:@"NSLocationAlwaysUsageDescription"]) {
      [locationManager requestAlwaysAuthorization];
    } else if ([[NSBundle mainBundle] objectForInfoDictionaryKey:@"NSLocationWhenInUseUsageDescription"]) {
      [locationManager requestWhenInUseAuthorization];
    } else {
      NSLog(@"[ERROR] The keys NSLocationAlwaysUsageDescription or NSLocationWhenInUseUsageDescription are not defined in your tiapp.xml. Starting with iOS 8 this is required.");
    }
    // Create the map
    [self map];
  } else {
    // else, just apply the userLocation
    [self map].showsUserLocation = userLocation;
  }
}

- (void)setLocation_:(id)location
{
  ENSURE_SINGLE_ARG(location, NSDictionary);
  //comes in like region: {latitude:100, longitude:100, latitudeDelta:0.5, longitudeDelta:0.5}
  id lat = [location objectForKey:@"latitude"];
  id lon = [location objectForKey:@"longitude"];
  id latdelta = [location objectForKey:@"latitudeDelta"];
  id londelta = [location objectForKey:@"longitudeDelta"];
  if (lat) {
    region.center.latitude = [lat doubleValue];
  }
  if (lon) {
    region.center.longitude = [lon doubleValue];
  }
  if (latdelta) {
    region.span.latitudeDelta = [latdelta doubleValue];
  }
  if (londelta) {
    region.span.longitudeDelta = [londelta doubleValue];
  }
  id an = [location objectForKey:@"animate"];
  if (an) {
    animate = [an boolValue];
  }
  id rf = [location objectForKey:@"regionFit"];
  if (rf) {
    regionFits = [rf boolValue];
  }
  [self render];
}

- (void)addRoute:(TiMapRouteProxy *)route
{
  TiThreadPerformOnMainThread(
      ^{
        CFDictionaryAddValue(mapObjects2View, [route routeLine], [route routeRenderer]);
        [self addOverlay:[route routeLine] level:[route level]];
      },
      NO);
}

- (void)removeRoute:(TiMapRouteProxy *)route
{
  TiThreadPerformOnMainThread(
      ^{
        MKPolyline *routeLine = [route routeLine];
        CFDictionaryRemoveValue(mapObjects2View, routeLine);
        [map removeOverlay:routeLine];
      },
      NO);
}

- (void)addPolygons:(NSMutableArray *)polygons
{
  for (TiMapPolygonProxy *poly in polygons) {
    [self addPolygon:poly];
  }
}

- (void)addPolygon:(TiMapPolygonProxy *)polygonProxy
{
  TiThreadPerformOnMainThread(
      ^{
        MKPolygon *poly = [polygonProxy polygon];
        CFDictionaryAddValue(mapObjects2View, poly, [polygonProxy polygonRenderer]);
        [map addOverlay:poly];

        if (polygonProxies == nil) {
          polygonProxies = [[NSMutableArray alloc] init];
        }
        [polygonProxies addObject:polygonProxy];
      },
      NO);
}

- (void)removePolygon:(TiMapPolygonProxy *)polygonProxy
{
  [self removePolygon:polygonProxy remove:YES];
}

- (void)removePolygon:(TiMapPolygonProxy *)polygonProxy remove:(BOOL)r
{
  TiThreadPerformOnMainThread(
      ^{
        MKPolygon *poly = [polygonProxy polygon];
        CFDictionaryRemoveValue(mapObjects2View, poly);
        [map removeOverlay:poly];
        if (r) {
          [polygonProxies removeObject:polygonProxy];
        }
      },
      NO);
}

- (void)removeAllPolygons
{
  for (int i = 0; i < [polygonProxies count]; i++) {
    TiMapPolygonProxy *proxy = [polygonProxies objectAtIndex:i];
    [self removePolygon:proxy remove:NO];
  }
  [polygonProxies removeAllObjects];
}

- (void)addCircle:(TiMapCircleProxy *)circleProxy
{

  TiThreadPerformOnMainThread(
      ^{
        MKCircle *circle = [[circleProxy circleRenderer] circle];
        CFDictionaryAddValue(mapObjects2View, circle, [circleProxy circleRenderer]);
        [map addOverlay:circle];

        if (circleProxies == nil) {
          circleProxies = [[NSMutableArray alloc] init];
        }
        [circleProxies addObject:circleProxy];
      },
      NO);
}

- (void)addCircles:(NSMutableArray *)circles
{
  for (TiMapCircleProxy *circle in circles) {
    [self addCircle:circle];
  }
}

- (void)removeCircle:(TiMapCircleProxy *)circleProxy
{
  [self removeCircle:circleProxy remove:YES];
}

- (void)removeCircle:(TiMapCircleProxy *)circleProxy remove:(BOOL)r
{
  TiThreadPerformOnMainThread(
      ^{
        MKCircle *circle = [[circleProxy circleRenderer] circle];
        CFDictionaryRemoveValue(mapObjects2View, circle);
        [map removeOverlay:circle];
        if (r) {
          [circleProxies removeObject:circleProxy];
        }
      },
      NO);
}

- (void)removeAllCircles
{
  for (int i = 0; i < [circleProxies count]; i++) {
    TiMapCircleProxy *circle = [circleProxies objectAtIndex:i];
    [self removeCircle:circle remove:NO];
  }
  [circleProxies removeAllObjects];
}

- (void)addPolylines:(NSMutableArray *)polylines
{
  for (TiMapPolylineProxy *poly in polylines) {
    [self addPolyline:poly];
  }
}

- (void)addPolyline:(TiMapPolylineProxy *)polylineProxy
{
  TiThreadPerformOnMainThread(
      ^{
        MKPolyline *poly = [polylineProxy polyline];
        CFDictionaryAddValue(mapObjects2View, poly, [polylineProxy polylineRenderer]);
        [map addOverlay:poly];
        if (polylineProxies == nil) {
          polylineProxies = [[NSMutableArray alloc] init];
        }
        [polylineProxies addObject:polylineProxy];
      },
      NO);
}

- (void)removePolyline:(TiMapPolylineProxy *)polylineProxy
{
  [self removePolyline:polylineProxy remove:YES];
}

- (void)removePolyline:(TiMapPolylineProxy *)polylineProxy remove:(BOOL)r
{
  TiThreadPerformOnMainThread(
      ^{
        MKPolyline *poly = [polylineProxy polyline];
        CFDictionaryRemoveValue(mapObjects2View, poly);
        [map removeOverlay:poly];
        if (r) {
          [polylineProxies removeObject:polylineProxy];
        }
      },
      NO);
}

- (void)removeAllPolylines
{
  for (int i = 0; i < [polylineProxies count]; i++) {
    TiMapPolylineProxy *proxy = [polylineProxies objectAtIndex:i];
    [self removePolyline:proxy remove:NO];
  }
  [polylineProxies removeAllObjects];
}

- (void)addImageOverlay:(TiMapImageOverlayProxy *)imageOverlayProxy
{
  TiThreadPerformOnMainThread(
      ^{
        TiMapImageOverlayRenderer *renderer = [imageOverlayProxy imageOverlayRenderer];
        TiMapImageOverlay *overlay = [imageOverlayProxy imageOverlay];
        CFDictionaryAddValue(mapObjects2View, overlay, renderer);
        [map addOverlay:overlay];
        if (imageOverlayProxies == nil) {
          imageOverlayProxies = [[NSMutableArray alloc] init];
        }
        [imageOverlayProxies addObject:imageOverlayProxy];
      },
      NO);
}

- (void)addImageOverlays:(NSMutableArray *)imageOverlays
{
  for (TiMapImageOverlayProxy *imageOverlay in imageOverlays) {
    [self addImageOverlay:imageOverlay];
  }
}
- (void)removeImageOverlay:(TiMapImageOverlayProxy *)imageOverlayProxy
{
  [self removeImageOverlay:imageOverlayProxy remove:YES];
}

- (void)removeImageOverlay:(TiMapImageOverlayProxy *)imageOverlayProxy remove:(BOOL)r
{
  TiThreadPerformOnMainThread(
      ^{
        TiMapImageOverlay *imageOverlay = [imageOverlayProxy imageOverlay];
        CFDictionaryRemoveValue(mapObjects2View, imageOverlay);
        [map removeOverlay:imageOverlay];
        if (r) {
          [imageOverlayProxies removeObject:imageOverlayProxy];
        }
      },
      NO);
}

- (void)removeAllImageOverlays
{
  for (int i = 0; i < [imageOverlayProxies count]; i++) {
    TiMapImageOverlayProxy *imageOverlay = [imageOverlayProxies objectAtIndex:i];
    [self removeImageOverlay:imageOverlay remove:NO];
  }
  [imageOverlayProxies removeAllObjects];
}

- (void)setTintColor_:(id)color
{
  TiColor *ticolor = [TiUtils colorValue:color];
  UIColor *theColor = [ticolor _color];
  [[self map] performSelector:@selector(setTintColor:) withObject:theColor];
  [self performSelector:@selector(setTintColor:) withObject:theColor];
}

- (void)setCamera_:(TiMapCameraProxy *)value
{
  TiThreadPerformOnMainThread(
      ^{
        [self map].camera = [value camera];
      },
      YES);
}

- (void)setPitchEnabled_:(id)value
{
  TiThreadPerformOnMainThread(
      ^{
        [self map].pitchEnabled = [TiUtils boolValue:value];
      },
      YES);
}

- (void)setRotateEnabled_:(id)value
{
  TiThreadPerformOnMainThread(
      ^{
        [self map].rotateEnabled = [TiUtils boolValue:value];
      },
      YES);
}

- (void)setScrollEnabled_:(id)value
{
  TiThreadPerformOnMainThread(
      ^{
        [self map].scrollEnabled = [TiUtils boolValue:value];
      },
      YES);
}

- (void)setZoomEnabled_:(id)value
{
  TiThreadPerformOnMainThread(
      ^{
        [self map].zoomEnabled = [TiUtils boolValue:value];
      },
      YES);
}

- (void)setShowsBuildings_:(id)value
{
  TiThreadPerformOnMainThread(
      ^{
        [self map].showsBuildings = [TiUtils boolValue:value];
      },
      YES);
}

- (void)setShowsPointsOfInterest_:(id)value
{
  TiThreadPerformOnMainThread(
      ^{
        [self map].showsPointsOfInterest = [TiUtils boolValue:value];
      },
      YES);
}

- (void)setShowsCompass_:(id)value
{
  DEPRECATED_REPLACED(@"Map.View.showsCompass", @"6.1.0", @"Map.View.compassEnabled");
  [self setCompassEnabled_:value];
}

- (void)setCompassEnabled_:(id)value
{
  TiThreadPerformOnMainThread(
      ^{
        [[self map] setShowsCompass:[TiUtils boolValue:value]];
      },
      YES);
}

- (void)setShowsScale_:(id)value
{
  TiThreadPerformOnMainThread(
      ^{
        [self map].showsScale = [TiUtils boolValue:value];
      },
      YES);
}

- (void)setShowsTraffic_:(id)value
{
  TiThreadPerformOnMainThread(
      ^{
        [self map].showsTraffic = [TiUtils boolValue:value];
      },
      YES);
}

- (void)showAllAnnotations:(id)value
{
  MKMapRect zoomRect = MKMapRectNull;
  for (id<MKAnnotation> annotation in self.map.annotations) {
    MKMapPoint annotationPoint = MKMapPointForCoordinate(annotation.coordinate);
    MKMapRect pointRect = MKMapRectMake(annotationPoint.x, annotationPoint.y, 0.1, 0.1);
    zoomRect = MKMapRectUnion(zoomRect, pointRect);
  }
  [self.map setVisibleMapRect:zoomRect animated:YES];
}

- (void)setPadding_:(id)value
{
  TiThreadPerformOnMainThread(
      ^{
        [self map].layoutMargins = [TiUtils contentInsets:value];
      },
      YES);
}

- (void)animateCamera:(id)args
{
  enum Args {
    kArgAnimationDict = 0,
    kArgCount,
    kArgCallback = kArgCount
  };
  NSDictionary *animationDict = [args objectAtIndex:kArgAnimationDict];
  ENSURE_TYPE(animationDict, NSDictionary);
  // Callback is optional
  cameraAnimationCallback = ([args count] > kArgCallback) ? [[args objectAtIndex:kArgCallback] retain] : nil;

  id cameraProxy = [animationDict objectForKey:@"camera"];
  ENSURE_TYPE(cameraProxy, TiMapCameraProxy);

  double duration = [TiUtils doubleValue:[animationDict objectForKey:@"duration"] def:400];
  double delay = [TiUtils doubleValue:[animationDict objectForKey:@"delay"] def:0];
  NSUInteger curve = [TiUtils intValue:[animationDict objectForKey:@"curve"] def:UIViewAnimationOptionCurveEaseInOut];

  // Apple says to use `mapView:regionDidChangeAnimated:` instead of `completion`
  // to know when the camera animation has completed
  TiThreadPerformOnMainThread(
      ^{
        [UIView animateWithDuration:(duration / 1000)
                              delay:(delay / 1000)
                            options:curve
                         animations:^{
                           [self map].camera = [cameraProxy camera];
                         }
                         completion:nil];
      },
      NO);
}

- (void)showAnnotations:(id)args
{
  ENSURE_SINGLE_ARG_OR_NIL(args, NSArray);

  TiThreadPerformOnMainThread(
      ^{
        [[self map] showAnnotations:args ?: [self customAnnotations] animated:animate];
      },
      NO);
}

- (void)setClusterAnnotation:(TiMapAnnotationProxy *)annotation forMembers:(NSArray<TiMapAnnotationProxy *> *)members
{
  if (!clusterAnnotations) {
    clusterAnnotations = [[NSMutableDictionary alloc] init];
  }

  TiMapAnnotationProxy *annotationProxy = [clusterAnnotations objectForKey:members];
  if (annotationProxy) {
    [[self proxy] forgetProxy:annotationProxy];
  }
  [clusterAnnotations removeObjectForKey:members];
  [clusterAnnotations setObject:annotation forKey:members];
}

- (TiMapAnnotationProxy *)clusterAnnotationProxyForMembers:(NSArray *)members
{
  return [clusterAnnotations objectForKey:members];
}

#pragma mark Utils

// These methods override the default implementation in TiMapView
- (void)addOverlay:(MKPolyline *)polyline level:(MKOverlayLevel)level
{
  [map addOverlay:polyline level:level];
}

#pragma mark Delegates

// Delegate for >= iOS 8
- (void)locationManager:(CLLocationManager *)manager didChangeAuthorizationStatus:(CLAuthorizationStatus)status
{
  if ((status == kCLAuthorizationStatusAuthorizedWhenInUse) || (status == kCLAuthorizationStatusAuthorizedAlways)) {
    self.map.showsUserLocation = [TiUtils boolValue:[self.proxy valueForKey:@"userLocation"] def:NO];
  }
}

// Delegate for >= iOS 7
- (MKOverlayRenderer *)mapView:(MKMapView *)mapView rendererForOverlay:(id<MKOverlay>)overlay
{
  return (MKOverlayRenderer *)CFDictionaryGetValue(mapObjects2View, overlay);
}

- (void)mapView:(MKMapView *)mapView regionWillChangeAnimated:(BOOL)animated
{
  if (ignoreRegionChanged) {
    return;
  }

  if ([[self proxy] _hasListeners:@"regionwillchange"]) {
    [self fireEvent:@"regionwillchange" withRegion:region animated:animated];
  }
}

- (void)mapView:(MKMapView *)mapView regionDidChangeAnimated:(BOOL)animated
{
  if (animated && cameraAnimationCallback != nil) {
    [cameraAnimationCallback call:nil thisObject:nil];
    RELEASE_TO_NIL(cameraAnimationCallback);
  }

  if (ignoreRegionChanged) {
    return;
  }

  region = [mapView region];
  [self.proxy replaceValue:[self dictionaryFromRegion] forKey:@"region" notification:NO];

  if ([self.proxy _hasListeners:@"regionChanged"]) {
    DEPRECATED_REPLACED(@"Map.View.Event.regionChanged", @"5.4.0", @"Map.View.Event.regionchanged");
    [self fireEvent:@"regionChanged" withRegion:[mapView region] animated:animated];
  }

  if ([self.proxy _hasListeners:@"regionchanged"]) {
    [self fireEvent:@"regionchanged" withRegion:[mapView region] animated:animated];
  }
}

- (void)mapViewWillStartLoadingMap:(MKMapView *)mapView
{
  loaded = NO;
  if ([self.proxy _hasListeners:@"loading"]) {
    [self.proxy fireEvent:@"loading" withObject:nil];
  }
}

- (void)mapViewDidFinishLoadingMap:(MKMapView *)mapView
{
  ignoreClicks = YES;
  loaded = YES;
  if ([self.proxy _hasListeners:@"complete"]) {
    [self.proxy fireEvent:@"complete" withObject:nil errorCode:0 message:nil];
  }
  ignoreClicks = NO;
}

- (void)mapViewDidFailLoadingMap:(MKMapView *)mapView withError:(NSError *)error
{
  if ([self.proxy _hasListeners:@"error"]) {
    NSString *message = [TiUtils messageFromError:error];
    NSDictionary *event = [NSDictionary dictionaryWithObject:message forKey:@"message"];
    [self.proxy fireEvent:@"error" withObject:event errorCode:[error code] message:message];
  }
}

- (void)mapView:(MKMapView *)mapView annotationView:(MKAnnotationView *)annotationView didChangeDragState:(MKAnnotationViewDragState)newState fromOldState:(MKAnnotationViewDragState)oldState
{
  [self firePinChangeDragState:annotationView newState:newState fromOldState:oldState];
  if (oldState == MKAnnotationViewDragStateStarting) {
    [annotationView setDragState:MKAnnotationViewDragStateDragging];
  } else if (oldState == MKAnnotationViewDragStateEnding || newState == MKAnnotationViewDragStateCanceling) {
    [annotationView setDragState:MKAnnotationViewDragStateNone];
  }
}

- (void)firePinChangeDragState:(MKAnnotationView *)pinview newState:(MKAnnotationViewDragState)newState fromOldState:(MKAnnotationViewDragState)oldState
{
  TiMapAnnotationProxy *viewProxy = [self proxyForAnnotation:pinview];

  if (viewProxy == nil)
    return;

  TiProxy *ourProxy = [self proxy];
  BOOL parentWants = [ourProxy _hasListeners:@"pinchangedragstate"];
  BOOL viewWants = [viewProxy _hasListeners:@"pinchangedragstate"];

  if (!parentWants && !viewWants)
    return;

  id title = [viewProxy title];
  if (title == nil)
    title = [NSNull null];

  NSDictionary *event = [NSDictionary dictionaryWithObjectsAndKeys:
                                          viewProxy, @"annotation",
                                      ourProxy, @"map",
                                      title, @"title",
                                      [NSNumber numberWithInteger:[pinview tag]], @"index",
                                      NUMINT(newState), @"newState",
                                      NUMINT(oldState), @"oldState",
                                      nil];

  if (parentWants)
    [ourProxy fireEvent:@"pinchangedragstate" withObject:event];

  if (viewWants)
    [viewProxy fireEvent:@"pinchangedragstate" withObject:event];
}

- (TiMapAnnotationProxy *)proxyForAnnotation:(MKAnnotationView *)pinview
{
  for (id annotation in [map annotations]) {
    if ([annotation isKindOfClass:[TiMapAnnotationProxy class]]) {
      if ([(TiMapAnnotationProxy *)annotation tag] == pinview.tag) {
        return annotation;
      }
    }
  }
  return nil;
}

- (void)mapView:(MKMapView *)mapView didSelectAnnotationView:(MKAnnotationView *)view
{
  if ([view conformsToProtocol:@protocol(TiMapAnnotation)]) {
    BOOL isSelected = [view isSelected];
    MKAnnotationView<TiMapAnnotation> *ann = (MKAnnotationView<TiMapAnnotation> *)view;

    selectedAnnotation = [ann retain];

    // If canShowCallout == true we will try to find calloutView to hadleTap on callout
    if ([ann canShowCallout]) {
      dispatch_after(dispatch_time(DISPATCH_TIME_NOW, 0.2 * NSEC_PER_SEC), dispatch_get_main_queue(), ^(void) {
        [self findCalloutView:ann];
      });
    }
    [self fireClickEvent:view source:isSelected ? @"pin" : [ann lastHitName] deselected:NO];
  }
}

- (void)findCalloutView:(UIView *)node
{
  // Dig annotation subviews to find _MKSmallCalloutPassthroughButton
  if ([node isKindOfClass:[NSClassFromString(@"_MKSmallCalloutPassthroughButton") class]]) {
    // Add tap recogniser to this view
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleCalloutTap:)];
    [node addGestureRecognizer:tap];
  } else {
    for (UIView *child in node.subviews) {
      [self findCalloutView:child];
    }
  }
}

- (void)mapView:(MKMapView *)mapView didDeselectAnnotationView:(MKAnnotationView *)view
{
  if ([view conformsToProtocol:@protocol(TiMapAnnotation)]) {
    BOOL isSelected = [TiUtils boolValue:[view isSelected] def:NO];
    MKAnnotationView<TiMapAnnotation> *ann = (MKAnnotationView<TiMapAnnotation> *)view;

    if (selectedAnnotation == ann) {
      RELEASE_TO_NIL(ann);
    }

    // TODO: Fire a deselected event for the annotation?
    [self fireClickEvent:view source:isSelected ? @"pin" : @"map" deselected:YES];
  }
}

- (void)mapView:(MKMapView *)mapView annotationView:(MKAnnotationView *)aview calloutAccessoryControlTapped:(UIControl *)control
{
  if ([aview conformsToProtocol:@protocol(TiMapAnnotation)]) {
    MKMarkerAnnotationView *pinview = (MKMarkerAnnotationView *)aview;
    NSString *clickSource = @"unknown";
    if (aview.leftCalloutAccessoryView == control) {
      clickSource = @"leftButton";
    } else if (aview.rightCalloutAccessoryView == control) {
      clickSource = @"rightButton";
    }
    [self fireClickEvent:pinview source:clickSource deselected:NO];
  }
}

- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotationProxy:(TiMapAnnotationProxy *)ann
{
  BOOL marker = [TiUtils boolValue:[ann valueForUndefinedKey:@"showAsMarker"] def:NO];

  id customView = [ann valueForUndefinedKey:@"customView"];
  if ((customView == nil) || (customView == [NSNull null]) || (![customView isKindOfClass:[TiViewProxy class]])) {
    customView = nil;
  }

  NSString *identifier = nil;
  UIImage *image = nil;
  if (customView == nil && !marker) {
    id imagePath = [ann valueForUndefinedKey:@"image"];
    image = [TiUtils image:imagePath proxy:ann];
    identifier = (image != nil) ? @"timap-image" : @"timap-marker";
  } else if (customView) {
    identifier = @"timap-customView";
  }
  MKAnnotationView *annView = nil;
  annView = (MKAnnotationView *)[mapView dequeueReusableAnnotationViewWithIdentifier:identifier];

  if (annView == nil) {
    if ([identifier isEqualToString:@"timap-customView"]) {
      annView = [[[TiMapCustomAnnotationView alloc] initWithAnnotation:ann reuseIdentifier:identifier map:self] autorelease];
    } else if ([identifier isEqualToString:@"timap-image"]) {
      annView = [[[TiMapImageAnnotationView alloc] initWithAnnotation:ann reuseIdentifier:identifier map:self image:image] autorelease];
    } else {
      annView = [[[TiMapMarkerAnnotationView alloc] initWithAnnotation:ann reuseIdentifier:identifier map:self] autorelease];
    }
  }
  if ([identifier isEqualToString:@"timap-customView"]) {
    [((TiMapCustomAnnotationView *)annView) setProxy:customView];
  } else if ([identifier isEqualToString:@"timap-image"]) {
    annView.image = image;
  } else {
    MKMarkerAnnotationView *markerView = (MKMarkerAnnotationView *)annView;
    markerView.markerTintColor = [[TiUtils colorValue:[ann valueForUndefinedKey:@"markerColor"]] color];
    markerView.glyphText = [ann valueForUndefinedKey:@"markerGlyphText"];
    markerView.glyphTintColor = [[TiUtils colorValue:[ann valueForUndefinedKey:@"markerGlyphColor"]] color];
    markerView.animatesWhenAdded = [TiUtils boolValue:[ann valueForUndefinedKey:@"markerAnimatesWhenAdded"]];
    markerView.glyphImage = [TiUtils image:[ann valueForUndefinedKey:@"markerGlyphImage"] proxy:ann];
    markerView.selectedGlyphImage = [TiUtils image:[ann valueForUndefinedKey:@"markerSelectedGlyphImage"] proxy:ann];
    markerView.titleVisibility = [TiUtils intValue:[ann valueForUndefinedKey:@"markerTitleVisibility"]];
    markerView.subtitleVisibility = [TiUtils intValue:[ann valueForUndefinedKey:@"markerSubtitleVisibility"]];
  }
  annView.canShowCallout = [TiUtils boolValue:[ann valueForUndefinedKey:@"canShowCallout"] def:YES];
  annView.enabled = YES;
  annView.centerOffset = ann.offset;
  annView.clusteringIdentifier = [ann valueForUndefinedKey:@"clusterIdentifier"];
  annView.collisionMode = [TiUtils intValue:[ann valueForUndefinedKey:@"collisionMode"]];
  annView.displayPriority = [TiUtils floatValue:[ann valueForUndefinedKey:@"annotationDisplayPriority"] def:1000];

  UIView *left = [ann leftViewAccessory];
  UIView *right = [ann rightViewAccessory];

  [annView setHidden:[TiUtils boolValue:[ann valueForUndefinedKey:@"hidden"] def:NO]];

  if (left != nil) {
    annView.leftCalloutAccessoryView = left;
  }

  if (right != nil) {
    annView.rightCalloutAccessoryView = right;
  }

  [annView setDraggable:[TiUtils boolValue:[ann valueForUndefinedKey:@"draggable"]]];
  annView.userInteractionEnabled = YES;
  annView.tag = [ann tag];

  id previewContext = [ann valueForUndefinedKey:@"previewContext"];
  if (previewContext && [TiUtils forceTouchSupported] && [previewContext performSelector:@selector(preview)] != nil) {
    UIViewController *controller = [[[TiApp app] controller] topPresentedController];
    [controller unregisterForPreviewingWithContext:ann.controllerPreviewing];

    Class TiPreviewingDelegate = NSClassFromString(@"TiPreviewingDelegate");

    if (!TiPreviewingDelegate) {
      NSLog(@"[ERROR] Unable to receive TiPreviewingDelegate class!");
      return nil;
    }

#ifndef __clang_analyzer__
    // We can ignore this, as it's guarded above
    id previewingDelegate = [[TiPreviewingDelegate alloc] performSelector:@selector(initWithPreviewContext:) withObject:previewContext];
    ann.controllerPreviewing = [controller registerForPreviewingWithDelegate:previewingDelegate sourceView:annView];
#endif
  }
  return annView;
}
- (void)animateAnnotation:(TiMapAnnotationProxy *)newAnnotation withLocation:(CLLocationCoordinate2D)newLocation
{
  userNewLocation.latitude = newLocation.latitude;
  userNewLocation.longitude = newLocation.longitude;

  [UIView animateWithDuration:2
                   animations:^{
                     newAnnotation.coordinate = userNewLocation;
                     MKAnnotationView *annotationView = (MKAnnotationView *)[self.map viewForAnnotation:newAnnotation];
                   }];
}
// mapView:viewForAnnotation: provides the view for each annotation.
// This method may be called for all or some of the added annotations.
// For MapKit provided annotations (eg. MKUserLocation) return nil to use the MapKit provided annotatiown view.
- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id<MKAnnotation>)annotation
{
  if ([annotation isKindOfClass:[TiMapAnnotationProxy class]]) {
    TiMapAnnotationProxy *annotationProxy = (TiMapAnnotationProxy *)annotation;
    return [self mapView:mapView viewForAnnotationProxy:annotationProxy];
  } else if ([annotation isKindOfClass:[MKClusterAnnotation class]]) {
    TiMapAnnotationProxy *annotationProxy = [self clusterAnnotationProxyForMembers:((MKClusterAnnotation *)annotation).memberAnnotations];
    if (!annotationProxy) {
      return nil;
    }
    MKClusterAnnotation *clusterAnnotation = ((MKClusterAnnotation *)annotation);
    clusterAnnotation.title = [annotationProxy valueForUndefinedKey:@"title"];
    clusterAnnotation.subtitle = [annotationProxy valueForUndefinedKey:@"subtitle"];
    return [self mapView:mapView viewForAnnotationProxy:annotationProxy];
  }
  return nil;
}

- (MKClusterAnnotation *)mapView:(MKMapView *)mapView clusterAnnotationForMemberAnnotations:(NSArray<id<MKAnnotation>> *)memberAnnotations
{
  MKClusterAnnotation *annotation = [[MKClusterAnnotation alloc] initWithMemberAnnotations:memberAnnotations];
  TiProxy *mapProxy = [self proxy];
  NSDictionary *event = @{ @"map" : mapProxy,
    @"memberAnnotations" : memberAnnotations };

  if ([mapProxy _hasListeners:@"clusterstart"]) {
    [mapProxy fireEvent:@"clusterstart" withObject:event];
  }
  return annotation;
}

// mapView:didAddAnnotationViews: is called after the annotation views have been added and positioned in the map.
// The delegate can implement this method to animate the adding of the annotations views.
// Use the current positions of the annotation views as the destinations of the animation.
- (void)mapView:(MKMapView *)mapView didAddAnnotationViews:(NSArray *)views
{
  for (MKAnnotationView<TiMapAnnotation> *thisView in views) {
    if (![thisView conformsToProtocol:@protocol(TiMapAnnotation)]) {
      return;
    }
    /*Image Annotation don't have any animation of its own. 
         *So in this case we do a custom animation, to place the 
         *image annotation on top of the mapview.*/
    if ([thisView isKindOfClass:[TiMapImageAnnotationView class]] || [thisView isKindOfClass:[TiMapCustomAnnotationView class]]) {
      TiMapAnnotationProxy *anntProxy = [self proxyForAnnotation:thisView];
      if ([anntProxy animatesDrop] && ![anntProxy placed]) {
        CGRect viewFrame = thisView.frame;
        thisView.frame = CGRectMake(viewFrame.origin.x, viewFrame.origin.y - self.frame.size.height, viewFrame.size.width, viewFrame.size.height);
        [UIView animateWithDuration:0.4
                              delay:0.0
                            options:UIViewAnimationCurveEaseOut
                         animations:^{
                           thisView.frame = viewFrame;
                         }
                         completion:nil];
      }
    }
    TiMapAnnotationProxy *thisProxy = [self proxyForAnnotation:thisView];
    [thisProxy setPlaced:YES];
  }
}

#pragma mark Click detection

- (id<MKAnnotation>)wasHitOnAnnotation:(CGPoint)point inView:(UIView *)view
{
  id<MKAnnotation> result = nil;
  for (UIView *subview in [view subviews]) {

    if (![subview pointInside:[self convertPoint:point toView:subview] withEvent:nil]) {
      continue;
    }

    if ([subview isKindOfClass:[MKAnnotationView class]]) {
      result = [(MKAnnotationView *)subview annotation];
    } else {
      result = [self wasHitOnAnnotation:point inView:subview];
    }

    if (result != nil) {
      break;
    }
  }
  return result;
}

#pragma mark Event generation

- (void)handleCalloutTap:(UIGestureRecognizer *)sender
{
  [self fireClickEvent:selectedAnnotation source:@"infoWindow" deselected:NO];
}

- (void)handleLongPressOnMap:(UIGestureRecognizer *)sender
{
  if (sender.state == UIGestureRecognizerStateBegan) {
    TiProxy *mapProxy = [self proxy];
    CGPoint location = [sender locationInView:map];
    CLLocationCoordinate2D coord = [map convertPoint:location toCoordinateFromView:map];
    NSNumber *lat = [NSNumber numberWithDouble:coord.latitude];
    NSNumber *lng = [NSNumber numberWithDouble:coord.longitude];
    NSDictionary *event = [NSDictionary dictionaryWithObjectsAndKeys:
                                            mapProxy, @"map", lat, @"latitude", lng, @"longitude", nil];
    if ([mapProxy _hasListeners:@"longclick"]) {
      [mapProxy fireEvent:@"longclick" withObject:event];
    }
  }
}

- (void)handleMapClick:(MKMapPoint)point
{
  TiProxy *mapProxy = [self proxy];

  CLLocationCoordinate2D clickCoordinate = MKCoordinateForMapPoint(point);
  NSNumber *lat = [NSNumber numberWithDouble:clickCoordinate.latitude];
  NSNumber *lng = [NSNumber numberWithDouble:clickCoordinate.longitude];
  NSDictionary *event = [NSDictionary dictionaryWithObjectsAndKeys:
                                          mapProxy, @"map", lat, @"latitude", lng, @"longitude", nil];

  if ([mapProxy _hasListeners:@"mapclick"]) {
    [mapProxy fireEvent:@"mapclick" withObject:event];
  }
}

- (void)handlePolygonClick:(MKMapPoint)point
{
  for (int i = 0; i < [polygonProxies count]; i++) {
    TiMapPolygonProxy *proxy = [polygonProxies objectAtIndex:i];

    MKPolygonRenderer *polygonRenderer = proxy.polygonRenderer;

    CGPoint polygonViewPoint = [polygonRenderer pointForMapPoint:point];
    BOOL inPolygon = CGPathContainsPoint(polygonRenderer.path, NULL, polygonViewPoint, NO);
    if (inPolygon && [TiUtils boolValue:[proxy valueForKey:@"touchEnabled"] def:YES]) {
      [self fireShapeClickEvent:proxy point:point sourceType:@"polygon"];
    }
  }
}

- (void)handleCircleClick:(MKMapPoint)point
{
  for (int i = 0; i < [circleProxies count]; i++) {
    TiMapCircleProxy *circle = [circleProxies objectAtIndex:i];

    MKCircleRenderer *circRenderer = circle.circleRenderer;

    CGPoint circleViewPoint = [circRenderer pointForMapPoint:point];
    BOOL inCircle = CGPathContainsPoint(circRenderer.path, NULL, circleViewPoint, NO);
    if (inCircle && [TiUtils boolValue:[circle valueForKey:@"touchEnabled"] def:YES]) {
      [self fireShapeClickEvent:circle point:point sourceType:@"circle"];
    }
  }
}
- (void)handlePolylineClick:(MKMapPoint)point
{
  for (int i = 0; i < [polylineProxies count]; i++) {
    TiMapPolylineProxy *proxy = [polylineProxies objectAtIndex:i];

    MKPolylineRenderer *polylineRenderer = proxy.polylineRenderer;

    CGPoint polylineViewPoint = [polylineRenderer pointForMapPoint:point];
    BOOL onPolyline = CGPathContainsPoint(polylineRenderer.path, NULL, polylineViewPoint, NO);
    if (onPolyline && [TiUtils boolValue:[proxy valueForKey:@"touchEnabled"] def:YES]) {
      [self fireShapeClickEvent:proxy point:point sourceType:@"polyline"];
    }
  }
}

- (void)fireEvent:(NSString *)event withRegion:(MKCoordinateRegion)_region animated:(BOOL)animated
{
  NSDictionary *object = [NSDictionary dictionaryWithObjectsAndKeys:
                                           event, @"type",
                                       [NSNumber numberWithDouble:_region.center.latitude], @"latitude",
                                       [NSNumber numberWithDouble:_region.center.longitude], @"longitude",
                                       [NSNumber numberWithDouble:_region.span.latitudeDelta], @"latitudeDelta",
                                       [NSNumber numberWithDouble:_region.span.longitudeDelta], @"longitudeDelta",
                                       NUMBOOL(animated), @"animated", nil];

  [self.proxy fireEvent:event withObject:object];
}

- (void)fireClickEvent:(MKAnnotationView *)pinview source:(NSString *)source deselected:(BOOL)deselected
{
  if (ignoreClicks) {
    return;
  }

  TiMapAnnotationProxy *viewProxy = [self proxyForAnnotation:pinview];
  if (viewProxy == nil) {
    return;
  }

  TiProxy *mapProxy = [self proxy];

  id title = [viewProxy title];
  if (title == nil) {
    title = [NSNull null];
  }

  NSInteger indexNumber = [pinview tag];
  id clicksource = source ? source : (id)[NSNull null];

  NSDictionary *event = [NSDictionary dictionaryWithObjectsAndKeys:
                                          clicksource, @"clicksource", viewProxy, @"annotation", mapProxy, @"map",
                                      title, @"title", NUMINTEGER(indexNumber), @"index", NUMBOOL(deselected), @"deselected", nil];

  [self doClickEvent:viewProxy mapProxy:mapProxy event:event];
}

- (void)fireShapeClickEvent:(id)sourceProxy point:(MKMapPoint)point sourceType:(NSString *)sourceType
{
  if (ignoreClicks) {
    return;
  }

  TiProxy *mapProxy = [self proxy];
  CLLocationCoordinate2D coord = MKCoordinateForMapPoint(point);

  NSNumber *lat = [NSNumber numberWithDouble:coord.latitude];
  NSNumber *lng = [NSNumber numberWithDouble:coord.longitude];

  // In iOS, sometimes the source property is forced to the mapProxy and so we have to send along
  // a more robust message via 'shape' and 'shapeType'.
  NSDictionary *event = [NSDictionary dictionaryWithObjectsAndKeys:sourceType, @"clicksource",
                                      mapProxy, @"map", lat, @"latitude", lng, @"longitude", sourceProxy, @"source", sourceProxy, @"shape", sourceType, @"shapeType", nil];

  [self doClickEvent:sourceProxy mapProxy:mapProxy event:event];
}

// Common functionality to fire event on map proxy and view proxy objects
- (void)doClickEvent:(id)viewProxy mapProxy:(id)mapProxy event:(NSDictionary *)event
{
  BOOL parentWants = [mapProxy _hasListeners:@"click"];
  BOOL viewWants;
  if ([viewProxy respondsToSelector:@selector(_hasListeners)]) {
    viewWants = [viewProxy _hasListeners:@"click"];
  } else {
    viewWants = FALSE;
  }

  if (parentWants) {
    [mapProxy fireEvent:@"click" withObject:event];
  }

  if (viewWants) {
    [viewProxy fireEvent:@"click" withObject:event];
  }
}

@end
