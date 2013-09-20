/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import <MapKit/MapKit.h>

@protocol TiMKOverlayPathUniversal<NSObject>

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
