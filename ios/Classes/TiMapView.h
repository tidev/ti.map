/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
 
#import "TiBase.h"
#import "TiUIView.h"
#import <MapKit/MapKit.h>

@class TiMapAnnotationProxy;

@protocol TiMapAnnotation
@required
-(NSString *)lastHitName;
@end


@interface TiMapView : TiUIView<MKMapViewDelegate> {
@private
	MKMapView *map;
	BOOL regionFits;
	BOOL animate;
	BOOL loaded;
	BOOL ignoreClicks;
	BOOL ignoreRegionChanged;
	BOOL forceRender;
	MKCoordinateRegion region;
	
    // routes
    // dictionaries for object tracking and association
    CFMutableDictionaryRef mapLine2View;   // MKPolyline(route line) -> MKPolylineView(route view)
    CFMutableDictionaryRef mapName2Line;   // NSString(name) -> MKPolyline(route line)
    
}

@property (nonatomic, readonly) CLLocationDegrees longitudeDelta;
@property (nonatomic, readonly) CLLocationDegrees latitudeDelta;
@property (nonatomic, readonly) NSArray *customAnnotations;

#pragma mark Private APIs
-(TiMapAnnotationProxy*)annotationFromArg:(id)arg;
-(NSArray*)annotationsFromArgs:(id)value;

#pragma mark Public APIs
-(void)addAnnotation:(id)args;
-(void)addAnnotations:(id)args;
-(void)setAnnotations_:(id)value;
-(void)removeAnnotation:(id)args;
-(void)removeAnnotations:(id)args;
-(void)removeAllAnnotations:(id)args;
-(void)selectAnnotation:(id)args;
-(void)deselectAnnotation:(id)args;
-(void)zoom:(id)args;
-(void)addRoute:(id)args;
-(void)removeRoute:(id)args;
-(void)firePinChangeDragState:(MKAnnotationView *) pinview newState:(MKAnnotationViewDragState)newState fromOldState:(MKAnnotationViewDragState)oldState;

#pragma mark Framework
-(void)refreshAnnotation:(TiMapAnnotationProxy*)proxy readd:(BOOL)yn;
-(void)fireClickEvent:(MKAnnotationView *) pinview source:(NSString *)source;

@end


@protocol TiMKOverlayPathUniversal

@property (retain) UIColor *fillColor;
@property (retain) UIColor *strokeColor;

@property CGFloat lineWidth; // defaults to 0, which is MKRoadWidthAtZoomScale(currentZoomScale)
@property CGLineJoin lineJoin; // defaults to kCGLineJoinRound
@property CGLineCap lineCap; // defaults to kCGLineCapRound
@property CGFloat miterLimit; // defaults to 10
@property CGFloat lineDashPhase; // defaults to 0
@property (copy) NSArray *lineDashPattern; // an array of NSNumbers, defaults to nil

// subclassers should override this to create a path and then set it on
// themselves with self.path = newPath;
- (void)createPath;
// returns cached path or calls createPath if path has not yet been created
@property CGPathRef path; // path will be retained
- (void)invalidatePath;

// subclassers may override these
- (void)applyStrokePropertiesToContext:(CGContextRef)context
                           atZoomScale:(MKZoomScale)zoomScale;
- (void)applyFillPropertiesToContext:(CGContextRef)context
                         atZoomScale:(MKZoomScale)zoomScale;
- (void)strokePath:(CGPathRef)path inContext:(CGContextRef)context;
- (void)fillPath:(CGPathRef)path inContext:(CGContextRef)context;

@optional
- (id)initWithPolyline:(MKPolyline *)polyline;

@property (nonatomic, readonly) MKPolyline *polyline;

@end
