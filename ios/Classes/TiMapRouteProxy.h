/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMKOverlayPathUniversal.h"
#import <MapKit/MapKit.h>
#import <TitaniumKit/TiProxy.h>
#import <TitaniumKit/TiUtils.h>

@interface TiMapRouteProxy : TiProxy {
  TiColor *color;
  float width;
  NSUInteger level;
  MKPolyline *routeLine;
  id<TiMKOverlayPathUniversal> routeRenderer;
}

@property (nonatomic, readonly) NSUInteger level;
@property (nonatomic, readonly) MKPolyline *routeLine;
@property (nonatomic, readonly) id<TiMKOverlayPathUniversal> routeRenderer;

@end
