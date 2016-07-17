/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2016 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
 
#import "TiViewProxy.h"
#import "TiMapAnnotationProxy.h"
#import "TiMapPolygonProxy.h"
#import "TiMapCircleProxy.h"
#import "TiMapPolylineProxy.h"

@interface TiMapViewProxy : TiViewProxy {
	TiMapAnnotationProxy* selectedAnnotation; // Annotation to select on initial display
	NSMutableArray* annotationsToAdd; // Annotations to add on initial display
	NSMutableArray* annotationsToRemove; // Annotations to remove on initial display
	NSMutableArray* routesToAdd; 
	NSMutableArray* routesToRemove;
    NSMutableArray* polygonsToAdd;
    NSMutableArray* polygonsToRemove;
    NSMutableArray* circlesToAdd;
    NSMutableArray* circlesToRemove;
    NSMutableArray* polylinesToAdd;
    NSMutableArray* polylinesToRemove;
	int zoomCount; // Number of times to zoom in/out on initial display
}

@property (nonatomic, readonly) NSNumber* longitudeDelta;
@property (nonatomic, readonly) NSNumber* latitudeDelta;

-(TiMapAnnotationProxy*)annotationFromArg:(id)arg;

-(void)addAnnotation:(id)args;
-(void)addAnnotations:(id)args;
-(void)removeAnnotation:(id)args;
-(void)removeAnnotations:(id)args;
-(void)removeAllAnnotations:(id)args;
-(void)selectAnnotation:(id)args;
-(void)deselectAnnotation:(id)args;
-(void)zoom:(id)args;
-(void)addRoute:(id)args;
-(void)removeRoute:(id)args;
-(void)addPolygons:(id)args;
-(void)addPolygon:(id)args;
-(void)removePolygon:(id)args;
-(void)removeAllPolygons:(id)args;
-(void)addCircles:(id)args;
-(void)addCircle:(id)args;
-(void)removeCircle:(id)args;
-(void)removeAllCircles:(id)args;
-(void)addPolylines:(id)args;
-(void)addPolyline:(id)args;
-(void)removePolyline:(id)args;
-(void)removeAllPolylines:(id)args;

@end
