/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
#import "TiMapViewProxy.h"
#import "TiCutoutCircle.h"
#import "TiMapAnnotationProxy.h"
#import "TiMapCircleProxy.h"
#import "TiMapImageOverlayProxy.h"
#import "TiMapPolygonProxy.h"
#import "TiMapPolylineProxy.h"
#import "TiMapRouteProxy.h"
#import "TiMapUtils.h"
#import "TiMapView.h"

@implementation TiMapViewProxy

#pragma mark Internal

- (NSArray *)keySequence
{
  return [NSArray arrayWithObjects:
                      @"animate",
                  @"location",
                  @"regionFit",
                  nil];
}

- (void)_destroy
{
  RELEASE_TO_NIL(selectedAnnotation);
  RELEASE_TO_NIL(annotationsToAdd);
  RELEASE_TO_NIL(annotationsToRemove);
  RELEASE_TO_NIL(routesToAdd);
  RELEASE_TO_NIL(routesToRemove);
  RELEASE_TO_NIL(polygonsToAdd);
  RELEASE_TO_NIL(polygonsToRemove);
  RELEASE_TO_NIL(circlesToAdd);
  RELEASE_TO_NIL(circlesToRemove);
  RELEASE_TO_NIL(polylinesToAdd);
  RELEASE_TO_NIL(polylinesToRemove);
  RELEASE_TO_NIL(imageOvelaysToAdd);
  RELEASE_TO_NIL(imageOvelaysToRemove);
  [super _destroy];
}

- (NSString *)apiName
{
  return @"Ti.Map.View";
}

- (NSNumber *)longitudeDelta
{
  __block CLLocationDegrees delta = 0.0;

  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          delta = [(TiMapView *)[self view] longitudeDelta];
        },
        YES);
  }
  return [NSNumber numberWithDouble:delta];
}

- (NSNumber *)latitudeDelta
{
  __block CLLocationDegrees delta = 0.0;

  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          delta = [(TiMapView *)[self view] latitudeDelta];
        },
        YES);
  }
  return [NSNumber numberWithDouble:delta];
}

- (void)viewDidAttach
{
  ENSURE_UI_THREAD_0_ARGS;
  TiMapView *ourView = (TiMapView *)[self view];

  for (id arg in annotationsToAdd) {
    [ourView addAnnotation:arg];
  }

  for (id arg in annotationsToRemove) {
    [ourView removeAnnotation:arg];
  }

  for (id arg in routesToAdd) {
    [ourView addRoute:arg];
  }

  for (id arg in routesToRemove) {
    [ourView removeRoute:arg];
  }

  for (id arg in polygonsToAdd) {
    [ourView addPolygon:arg];
  }

  for (id arg in polygonsToRemove) {
    [ourView removePolygon:arg];
  }

  for (id arg in circlesToAdd) {
    [ourView addCircle:arg];
  }

  for (id arg in circlesToRemove) {
    [ourView removeCircle:arg];
  }

  for (id arg in polylinesToAdd) {
    [ourView addPolyline:arg];
  }

  for (id arg in polylinesToRemove) {
    [ourView removePolyline:arg];
  }

  for (id arg in imageOvelaysToAdd) {
    [ourView addImageOverlay:arg];
  }

  for (id arg in imageOvelaysToRemove) {
    [ourView removeImageOverlay:arg];
  }

  [ourView selectAnnotation:selectedAnnotation];
  if (zoomCount > 0) {
    for (int i = 0; i < zoomCount; i++) {
      [ourView zoom:[NSNumber numberWithDouble:1.0]];
    }
  } else {
    for (int i = zoomCount; i < 0; i++) {
      [ourView zoom:[NSNumber numberWithDouble:-1.0]];
    }
  }

  RELEASE_TO_NIL(selectedAnnotation);
  RELEASE_TO_NIL(annotationsToAdd);
  RELEASE_TO_NIL(annotationsToRemove);
  RELEASE_TO_NIL(routesToAdd);
  RELEASE_TO_NIL(routesToRemove);
  RELEASE_TO_NIL(polygonsToAdd);
  RELEASE_TO_NIL(polygonsToRemove);
  RELEASE_TO_NIL(circlesToAdd);
  RELEASE_TO_NIL(circlesToRemove);
  RELEASE_TO_NIL(polylinesToAdd);
  RELEASE_TO_NIL(polylinesToRemove);

  [super viewDidAttach];
}

- (TiMapAnnotationProxy *)annotationFromArg:(id)arg
{
  if ([arg isKindOfClass:[TiMapAnnotationProxy class]]) {
    [(TiMapAnnotationProxy *)arg setDelegate:self];
    [arg setPlaced:NO];
    return arg;
  }
  ENSURE_TYPE(arg, NSDictionary);
  TiMapAnnotationProxy *proxy = [[[TiMapAnnotationProxy alloc] _initWithPageContext:[self pageContext] args:[NSArray arrayWithObject:arg]] autorelease];

  [proxy setDelegate:self];
  return proxy;
}

- (MKAnnotationView *)viewForAnnotationProxy:(TiMapAnnotationProxy *)annotationProxy
{
  MKMapView *mapView = [(TiMapView *)[self view] map];
  return [mapView viewForAnnotation:annotationProxy];
}

#pragma mark Public API

- (void)zoom:(id)arg
{
  ENSURE_SINGLE_ARG(arg, NSObject);
  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] zoom:arg];
        },
        NO);
  } else {
    double v = [TiUtils doubleValue:arg];
    // TODO: Find good delta tolerance value to deal with floating point goofs
    if (v == 0.0) {
      return;
    }
    if (v > 0) {
      zoomCount++;
    } else {
      zoomCount--;
    }
  }
}

- (void)selectAnnotation:(id)arg
{
  ENSURE_SINGLE_ARG(arg, NSObject);
  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] selectAnnotation:arg];
        },
        NO);
  } else {
    if (selectedAnnotation != arg) {
      RELEASE_TO_NIL(selectedAnnotation);
      selectedAnnotation = [arg retain];
    }
  }
}

- (void)deselectAnnotation:(id)arg
{
  ENSURE_SINGLE_ARG(arg, NSObject);
  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] deselectAnnotation:arg];
        },
        NO);
  } else {
    RELEASE_TO_NIL(selectedAnnotation);
  }
}

- (void)setLocation:(id)args
{
  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] setLocation:args];
        },
        NO);
  }
}

- (void)addAnnotation:(id)arg
{
  ENSURE_SINGLE_ARG(arg, NSObject);
  TiMapAnnotationProxy *annProxy = [self annotationFromArg:arg];
  [self rememberProxy:annProxy];

  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] addAnnotation:arg];
        },
        NO);
  } else {
    if (annotationsToAdd == nil) {
      annotationsToAdd = [[NSMutableArray alloc] init];
    }
    if (annotationsToRemove != nil && [annotationsToRemove containsObject:arg]) {
      [annotationsToRemove removeObject:arg];
    } else {
      [annotationsToAdd addObject:arg];
    }
  }
}

- (void)addAnnotations:(id)arg
{
  ENSURE_SINGLE_ARG(arg, NSArray);
  NSMutableArray *newAnnotations = [NSMutableArray arrayWithCapacity:[arg count]];
  for (id ann in arg) {
    TiMapAnnotationProxy *annotation = [self annotationFromArg:ann];
    [newAnnotations addObject:annotation];
    [self rememberProxy:annotation];
  }

  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] addAnnotations:newAnnotations];
        },
        NO);
  } else {
    for (id annotation in newAnnotations) {
      [self addAnnotation:annotation];
    }
  }
}

- (void)addCutoutCircle:(id)arg
{
  ENSURE_SINGLE_ARG(arg, NSDictionary);

  [self replaceValue:arg forKey:@"cutoutCircle" notification:NO];

  CGFloat latitude = [TiUtils floatValue:@"latitude" properties:arg];
  CGFloat longitude = [TiUtils floatValue:@"longitude" properties:arg];
  CGFloat radius = [TiUtils doubleValue:@"radius" properties:arg];
  double tolerance = [TiUtils doubleValue:@"tolerance" properties:arg def:3.0];

  CLLocationCoordinate2D WORLD_COORDINATES[6];
  WORLD_COORDINATES[0] = CLLocationCoordinate2DMake(90, 0);
  WORLD_COORDINATES[1] = CLLocationCoordinate2DMake(90, 180);
  WORLD_COORDINATES[2] = CLLocationCoordinate2DMake(-90, 180);
  WORLD_COORDINATES[3] = CLLocationCoordinate2DMake(-90, 0);
  WORLD_COORDINATES[4] = CLLocationCoordinate2DMake(-90, -180);
  WORLD_COORDINATES[5] = CLLocationCoordinate2DMake(90, -180);

  CLLocationCoordinate2D coordinate = CLLocationCoordinate2DMake(latitude, longitude);
  NSArray<NSDictionary *> *circleCoordinates = [TiMapUtils generateCircleCoordinates:coordinate
                                                                          withRadius:radius
                                                                        andTolerance:tolerance];

  CLLocationCoordinate2D *circleCoordinatesNative = malloc(sizeof(CLLocationCoordinate2D) * [circleCoordinates count]);

  for (NSUInteger i = 0; i < [circleCoordinates count]; ++i) {
    CLLocationCoordinate2D coordinate = [TiMapUtils processLocation:[circleCoordinates objectAtIndex:i]];
    circleCoordinatesNative[i] = coordinate;
  }

  MKPolygon *circlePolygon = [MKPolygon polygonWithCoordinates:circleCoordinatesNative count:circleCoordinates.count];
  TiCutoutCircle *cutoutPolygon = [TiCutoutCircle polygonWithCoordinates:WORLD_COORDINATES count:6 interiorPolygons:@[ circlePolygon ]];

  [[(TiMapView *)[self view] map] addOverlay:cutoutPolygon];
}

- (void)setAnnotations:(id)arg
{
  ENSURE_TYPE(arg, NSArray);

  NSMutableArray *newAnnotations = [NSMutableArray arrayWithCapacity:[arg count]];
  for (id ann in arg) {
    [newAnnotations addObject:[self annotationFromArg:ann]];
  }

  BOOL attached = [self viewAttached];
  __block NSArray *currentAnnotations = nil;
  if (attached) {
    TiThreadPerformOnMainThread(
        ^{
          currentAnnotations = [[(TiMapView *)[self view] customAnnotations] retain];
        },
        YES);
  } else {
    currentAnnotations = annotationsToAdd;
  }

  // Because the annotations may contain an annotation proxy and not just
  // descriptors for them, we have to check and make sure there is
  // no overlap and remember/forget appropriately.

  for (id annProxy in currentAnnotations) {
    // Remove old proxy references that should not rendered anymore
    // Also only forget proxies, not native annotations like cluster annotations
    if ([annProxy isKindOfClass:[TiMapAnnotationProxy class]] && ![newAnnotations containsObject:annProxy]) {
      [self forgetProxy:annProxy];
    }
  }
  for (TiMapAnnotationProxy *annProxy in newAnnotations) {
    if (![currentAnnotations containsObject:annProxy]) {
      [self rememberProxy:annProxy];
    }
  }

  if (attached) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] setAnnotations_:newAnnotations];
        },
        NO);
    [currentAnnotations release];
  } else {
    RELEASE_TO_NIL(annotationsToAdd);
    RELEASE_TO_NIL(annotationsToRemove);

    annotationsToAdd = [[NSMutableArray alloc] initWithArray:newAnnotations];
  }
}

- (NSArray *)annotations
{
  if ([self viewAttached]) {
    __block NSArray *currentAnnotations = nil;
    TiThreadPerformOnMainThread(
        ^{
          currentAnnotations = [[(TiMapView *)[self view] customAnnotations] retain];
        },
        YES);
    return [currentAnnotations autorelease];
  } else {
    return annotationsToAdd;
  }
}

- (void)removeAnnotation:(id)arg
{
  ENSURE_SINGLE_ARG(arg, NSObject);

  // For legacy reasons, we can apparently allow the arg here to be a string (0.8 compatibility?!?)
  // and so only need to convert/remember/forget if it is an annotation instead.
  if ([arg isKindOfClass:[TiMapAnnotationProxy class]]) {
    [self forgetProxy:arg];
  }

  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] removeAnnotation:arg];
        },
        NO);
  } else {
    if (annotationsToRemove == nil) {
      annotationsToRemove = [[NSMutableArray alloc] init];
    }
    if (annotationsToAdd != nil && [annotationsToAdd containsObject:arg]) {
      [annotationsToAdd removeObject:arg];
    } else {
      [annotationsToRemove addObject:arg];
    }
  }
}

- (void)removeAnnotations:(id)arg
{
  ENSURE_SINGLE_ARG(arg, NSArray);
  for (id ann in arg) {
    if ([ann isKindOfClass:[TiMapAnnotationProxy class]]) {
      [self forgetProxy:ann];
    }
  }

  if ([self viewAttached]) {
    [(TiMapView *)[self view] removeAnnotations:arg];
  } else {
    for (id annotation in arg) {
      [self removeAnnotation:annotation];
    }
  }
}

- (void)removeAllAnnotations:(id)unused
{
  if ([self viewAttached]) {
    __block NSArray *currentAnnotations = nil;
    TiThreadPerformOnMainThread(
        ^{
          currentAnnotations = [[(TiMapView *)[self view] customAnnotations] retain];
        },
        YES);

    for (id object in currentAnnotations) {
      if (![object isKindOfClass:[MKClusterAnnotation class]]) {
        TiMapAnnotationProxy *annProxy = [self annotationFromArg:object];
        [self forgetProxy:annProxy];
      }
    }
    [currentAnnotations release];
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] removeAllAnnotations:unused];
        },
        NO);
  } else {
    for (TiMapAnnotationProxy *annotation in annotationsToAdd) {
      [self forgetProxy:annotation];
    }

    RELEASE_TO_NIL(annotationsToAdd);
    RELEASE_TO_NIL(annotationsToRemove);
  }
}

- (void)addRoute:(id)arg
{
  ENSURE_SINGLE_ARG(arg, TiMapRouteProxy);

  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] addRoute:arg];
        },
        NO);
  } else {
    if (routesToAdd == nil) {
      routesToAdd = [[NSMutableArray alloc] init];
    }
    if (routesToRemove != nil && [routesToRemove containsObject:arg]) {
      [routesToRemove removeObject:arg];
    } else {
      [routesToAdd addObject:arg];
    }
  }
}

- (void)removeRoute:(id)arg
{
  ENSURE_SINGLE_ARG(arg, TiMapRouteProxy);

  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] removeRoute:arg];
        },
        NO);
  } else {
    if (routesToRemove == nil) {
      routesToRemove = [[NSMutableArray alloc] init];
    }
    if (routesToAdd != nil && [routesToAdd containsObject:arg]) {
      [routesToAdd removeObject:arg];
    } else {
      [routesToRemove addObject:arg];
    }
  }
}

- (void)addPolygons:(id)arg
{
  ENSURE_SINGLE_ARG(arg, NSArray);

  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] addPolygons:arg];
        },
        NO);
  } else {
    for (id poly in arg) {
      [self addPolygon:poly];
    }
  }
}

- (void)addPolygon:(id)arg
{
  ENSURE_SINGLE_ARG(arg, TiMapPolygonProxy);

  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] addPolygon:arg];
        },
        NO);
  } else {
    if (polygonsToAdd == nil) {
      polygonsToAdd = [[NSMutableArray alloc] init];
    }
    if (polygonsToRemove != nil && [polygonsToRemove containsObject:arg]) {
      [polygonsToRemove removeObject:arg];
    } else {
      [polygonsToAdd addObject:arg];
    }
  }
}

- (void)removePolygon:(id)arg
{
  ENSURE_SINGLE_ARG(arg, TiMapPolygonProxy);

  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] removePolygon:arg];
        },
        NO);
  } else {
    if (polygonsToRemove == nil) {
      polygonsToRemove = [[NSMutableArray alloc] init];
    }
    if (polygonsToAdd != nil && [polygonsToAdd containsObject:arg]) {
      [polygonsToAdd removeObject:arg];
    } else {
      [polygonsToRemove addObject:arg];
    }
  }
}

- (void)removeAllPolygons:(id)args
{
  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] removeAllPolygons];
        },
        NO);
  }
}

- (void)setPolygons:(id)arg
{
  ENSURE_TYPE(arg, NSArray);

  NSMutableArray *initialPolygons = [NSMutableArray arrayWithCapacity:[arg count]];
  for (id poly in arg) {
    ENSURE_TYPE(poly, TiMapPolygonProxy);
    [initialPolygons addObject:poly];
  }

  BOOL attached = [self viewAttached];

  if (attached) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] addPolygons:polygonsToAdd];
          [initialPolygons release];
        },
        NO);
  } else {
    RELEASE_TO_NIL(polygonsToAdd);
    RELEASE_TO_NIL(polygonsToRemove);

    polygonsToAdd = [[NSMutableArray alloc] initWithArray:initialPolygons];
  }
}

- (void)addCircles:(id)arg
{
  ENSURE_SINGLE_ARG(arg, NSArray);

  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] addCircles:arg];
        },
        NO);
  } else {
    for (id circle in arg) {
      [self addCircle:circle];
    }
  }
}

- (void)addCircle:(id)arg
{
  ENSURE_SINGLE_ARG(arg, TiMapCircleProxy);

  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] addCircle:arg];
        },
        NO);
  } else {
    if (circlesToAdd == nil) {
      circlesToAdd = [[NSMutableArray alloc] init];
    }
    if (circlesToRemove != nil && [circlesToRemove containsObject:arg]) {
      [circlesToRemove removeObject:arg];
    } else {
      [circlesToAdd addObject:arg];
    }
  }
}

- (void)removeCircle:(id)arg
{
  ENSURE_SINGLE_ARG(arg, TiMapCircleProxy);

  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] removeCircle:arg];
        },
        NO);
  } else {
    if (circlesToRemove == nil) {
      circlesToRemove = [[NSMutableArray alloc] init];
    }
    if (circlesToAdd != nil && [circlesToAdd containsObject:arg]) {
      [circlesToAdd removeObject:arg];
    } else {
      [circlesToRemove addObject:arg];
    }
  }
}

- (void)removeAllCircles:(id)args
{
  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] removeAllCircles];
        },
        NO);
  }
}

- (void)setCircles:(id)arg
{
  ENSURE_TYPE(arg, NSArray);

  NSMutableArray *initialCircles = [NSMutableArray arrayWithCapacity:[arg count]];
  for (id circle in arg) {
    ENSURE_TYPE(circle, TiMapCircleProxy);
    [initialCircles addObject:circle];
  }

  BOOL attached = [self viewAttached];

  if (attached) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] addCircles:circlesToAdd];
          [initialCircles release];
        },
        NO);
  } else {
    RELEASE_TO_NIL(circlesToAdd);
    RELEASE_TO_NIL(circlesToRemove);
    circlesToAdd = [[NSMutableArray alloc] initWithArray:initialCircles];
  }
}

- (void)addPolylines:(id)arg
{
  ENSURE_SINGLE_ARG(arg, NSArray);

  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] addPolylines:arg];
        },
        NO);
  } else {
    for (id poly in arg) {
      [self addPolyline:poly];
    }
  }
}

- (void)addPolyline:(id)arg
{
  ENSURE_SINGLE_ARG(arg, TiMapPolylineProxy);

  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] addPolyline:arg];
        },
        NO);
  } else {
    if (polylinesToAdd == nil) {
      polylinesToAdd = [[NSMutableArray alloc] init];
    }
    if (polylinesToRemove != nil && [polylinesToRemove containsObject:arg]) {
      [polylinesToRemove removeObject:arg];
    } else {
      [polylinesToAdd addObject:arg];
    }
  }
}

- (void)removePolyline:(id)arg
{
  ENSURE_SINGLE_ARG(arg, TiMapPolylineProxy);

  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] removePolyline:arg];
        },
        NO);
  } else {
    if (polylinesToRemove == nil) {
      polylinesToRemove = [[NSMutableArray alloc] init];
    }
    if (polylinesToAdd != nil && [polylinesToAdd containsObject:arg]) {
      [polylinesToAdd removeObject:arg];
    } else {
      [polylinesToRemove addObject:arg];
    }
  }
}

- (void)removeAllPolylines:(id)args
{
  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] removeAllPolylines];
        },
        NO);
  }
}

- (void)setPolylines:(id)arg
{
  ENSURE_TYPE(arg, NSArray);

  NSMutableArray *initialPolylines = [NSMutableArray arrayWithCapacity:[arg count]];
  for (id poly in arg) {
    ENSURE_TYPE(poly, TiMapPolylineProxy);
    [initialPolylines addObject:poly];
  }

  BOOL attached = [self viewAttached];

  if (attached) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] addPolylines:polylinesToAdd];
          [initialPolylines release];
        },
        NO);
  } else {
    RELEASE_TO_NIL(polylinesToAdd);
    RELEASE_TO_NIL(polylinesToRemove);

    polylinesToAdd = [[NSMutableArray alloc] initWithArray:initialPolylines];
  }
}

- (void)addImageOverlays:(id)arg
{
  ENSURE_SINGLE_ARG(arg, NSArray);

  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] addImageOverlays:arg];
        },
        NO);
  } else {
    for (id circle in arg) {
      [self addImageOverlay:circle];
    }
  }
}

- (void)addImageOverlay:(id)arg
{
  ENSURE_SINGLE_ARG(arg, TiMapImageOverlayProxy);

  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] addImageOverlay:arg];
        },
        NO);
  } else {
    if (imageOvelaysToAdd == nil) {
      imageOvelaysToAdd = [[NSMutableArray alloc] init];
    }
    if (imageOvelaysToRemove != nil && [imageOvelaysToRemove containsObject:arg]) {
      [imageOvelaysToRemove removeObject:arg];
    } else {
      [imageOvelaysToAdd addObject:arg];
    }
  }
}

- (void)removeImageOverlay:(id)arg
{
  ENSURE_SINGLE_ARG(arg, TiMapImageOverlayProxy);

  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] removeImageOverlay:arg];
        },
        NO);
  } else {
    if (imageOvelaysToRemove == nil) {
      imageOvelaysToRemove = [[NSMutableArray alloc] init];
    }
    if (imageOvelaysToAdd != nil && [imageOvelaysToAdd containsObject:arg]) {
      [imageOvelaysToAdd removeObject:arg];
    } else {
      [imageOvelaysToRemove addObject:arg];
    }
  }
}

- (void)removeAllImageOverlays:(id)args
{
  if ([self viewAttached]) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] removeAllImageOverlays];
        },
        NO);
  }
}

- (void)setImageOverlays:(id)args
{
  ENSURE_TYPE(args, NSArray);

  NSMutableArray *initialImageOverlays = [NSMutableArray arrayWithCapacity:[args count]];
  for (id imageOverlay in args) {
    ENSURE_TYPE(imageOverlay, TiMapImageOverlayProxy);
    [initialImageOverlays addObject:imageOverlay];
  }

  BOOL attached = [self viewAttached];

  if (attached) {
    TiThreadPerformOnMainThread(
        ^{
          [(TiMapView *)[self view] addImageOverlays:imageOvelaysToAdd];
          [initialImageOverlays release];
        },
        NO);
  } else {
    RELEASE_TO_NIL(imageOvelaysToAdd);
    RELEASE_TO_NIL(imageOvelaysToRemove);
    imageOvelaysToAdd = [[NSMutableArray alloc] initWithArray:initialImageOverlays];
  }
}

- (void)setClusterAnnotation:(id)args
{
  ENSURE_DICT(args);
  NSArray *memberAnnotations = [args objectForKey:@"memberAnnotations"];
  id annotation = [args objectForKey:@"annotation"];
  TiMapAnnotationProxy *annotationProxy = [self annotationFromArg:annotation];
  [self rememberProxy:annotationProxy];

  if ([self viewAttached]) {
    [(TiMapView *)[self view] setClusterAnnotation:annotationProxy forMembers:memberAnnotations];
  }
}

#pragma mark Public APIs iOS 7

- (TiMapCameraProxy *)camera
{
  return [TiMapUtils returnValueOnMainThread:^id {
    return [(TiMapView *)[self view] camera];
  }];
}

- (void)animateCamera:(id)args
{
  TiThreadPerformOnMainThread(
      ^{
        [(TiMapView *)[self view] animateCamera:args];
      },
      NO);
}

- (void)showAllAnnotations:(id)unused
{
  TiThreadPerformOnMainThread(
      ^{
        [(TiMapView *)[self view] showAllAnnotations:unused];
      },
      NO);
}

- (void)showAnnotations:(id)args
{
  TiThreadPerformOnMainThread(
      ^{
        [(TiMapView *)[self view] showAnnotations:args];
      },
      NO);
}

- (NSNumber *)containsCoordinate:(id)args
{
  return [(TiMapView *)[self view] containsCoordinate:args];
}

@end
