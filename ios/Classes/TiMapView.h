/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiBase.h"
#import "TiMKOverlayPathUniversal.h"
#import "TiMapCameraProxy.h"
#import "TiUIView.h"
#import "WildcardGestureRecognizer.h"
#import <MapKit/MapKit.h>

@class TiMapAnnotationProxy;

@protocol TiMapAnnotation
@required
- (NSString *)lastHitName;
@end

@interface TiMapView : TiUIView <MKMapViewDelegate, CLLocationManagerDelegate> {
  MKMapView *map;
  BOOL regionFits;
  BOOL animate;
  BOOL loaded;
  BOOL ignoreClicks;
  BOOL ignoreRegionChanged;
  BOOL forceRender;
  MKCoordinateRegion region;
  NSMutableArray *polygonProxies;
  NSMutableArray *circleProxies;
  NSMutableArray *polylineProxies;
  NSMutableArray *imageOverlayProxies;

#if IS_IOS_11
  NSMutableDictionary *clusterAnnotations;
#endif
  //selected annotation
  MKAnnotationView<TiMapAnnotation> *selectedAnnotation;

  // dictionary for object tracking and association
  CFMutableDictionaryRef mapObjects2View; // MKOverlay Object -> MKOverlay Object's renderer

  // Location manager needed for iOS 8 permissions
  CLLocationManager *locationManager;
  KrollCallback *cameraAnimationCallback;
}

@property (nonatomic, readonly) CLLocationDegrees longitudeDelta;
@property (nonatomic, readonly) CLLocationDegrees latitudeDelta;
@property (nonatomic, readonly) NSArray *customAnnotations;

#pragma mark Private APIs

- (TiMapAnnotationProxy *)annotationFromArg:(id)arg;
- (NSArray *)annotationsFromArgs:(id)value;
- (MKMapView *)map;
- (TiMapCameraProxy *)camera;

#pragma mark Public APIs

- (void)animateCamera:(id)args;
- (void)showAnnotations:(id)args;
- (void)showAllAnnotations:(id)value;
- (void)addAnnotation:(id)args;
- (void)addAnnotations:(id)args;
- (void)setAnnotations_:(id)value;
- (void)removeAnnotation:(id)args;
- (void)removeAnnotations:(id)args;
- (void)removeAllAnnotations:(id)args;
- (void)selectAnnotation:(id)args;
- (void)deselectAnnotation:(id)args;
- (void)zoom:(id)args;
- (void)addRoute:(id)args;
- (void)removeRoute:(id)args;
- (void)addPolygon:(id)args;
- (void)addPolygons:(id)args;
- (void)removePolygon:(id)args;
- (void)removePolygon:(id)args remove:(BOOL)r;
- (void)removeAllPolygons;
- (void)addCircle:(id)args;
- (void)addCircles:(id)args;
- (void)removeCircle:(id)args;
- (void)removeCircle:(id)args remove:(BOOL)r;
- (void)removeAllCircles;
- (void)addPolyline:(id)args;
- (void)addPolylines:(id)args;
- (void)removePolyline:(id)args;
- (void)removePolyline:(id)args remove:(BOOL)r;
- (void)removeAllPolylines;
- (void)addImageOverlay:(id)arg;
- (void)addImageOverlays:(id)args;
- (void)removeImageOverlay:(id)arg;
- (void)removeAllImageOverlays;

- (void)firePinChangeDragState:(MKAnnotationView *)pinview newState:(MKAnnotationViewDragState)newState fromOldState:(MKAnnotationViewDragState)oldState;
#if IS_IOS_11
- (void)setClusterAnnotation:(TiMapAnnotationProxy *)annotation forMembers:(NSArray<TiMapAnnotationProxy *> *)members;
#endif

#pragma mark Utils
- (void)addOverlay:(MKPolyline *)polyline level:(MKOverlayLevel)level;

#pragma mark Framework
- (void)refreshAnnotation:(TiMapAnnotationProxy *)proxy readd:(BOOL)yn;
- (void)fireClickEvent:(MKAnnotationView *)pinview source:(NSString *)source deselected:(BOOL)deselected;

@end
