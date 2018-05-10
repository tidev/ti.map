/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMapImageOverlayRenderer.h"
#import "TiProxy.h"

@interface TiMapImageOverlay : NSObject <MKOverlay>

@end

@interface TiMapImageOverlayProxy : TiProxy {
  @private
  UIImage *_image;
  CLLocationCoordinate2D _midCoordinate;
  MKMapRect _mapRect;
}
@property (nonatomic, readonly) TiMapImageOverlayRenderer *imageOverlayRenderer;
@property (nonatomic, readonly) TiMapImageOverlay *imageOverlay;

@end
