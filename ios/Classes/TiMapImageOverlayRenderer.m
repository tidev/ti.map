/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMapImageOverlayRenderer.h"
#import <TitaniumKit/TiBase.h>

@interface TiMapImageOverlayRenderer ()

@property (nonatomic, retain) UIImage *overlayImage;

@end

@implementation TiMapImageOverlayRenderer

- (void)dealloc
{
  RELEASE_TO_NIL(_overlayImage);
  [super dealloc];
}

- (instancetype)initWithOverlay:(id<MKOverlay>)overlay overlayImage:(UIImage *)overlayImage
{
  self = [super initWithOverlay:overlay];
  if (self) {
    _overlayImage = overlayImage;
  }

  return self;
}

- (void)drawMapRect:(MKMapRect)mapRect zoomScale:(MKZoomScale)zoomScale inContext:(CGContextRef)context
{
  CGImageRef imageReference = self.overlayImage.CGImage;

  MKMapRect theMapRect = self.overlay.boundingMapRect;
  CGRect theRect = [self rectForMapRect:theMapRect];

  CGContextScaleCTM(context, 1.0, -1.0);
  CGContextTranslateCTM(context, 0.0, -theRect.size.height);
  CGContextDrawImage(context, theRect, imageReference);
}

@end
