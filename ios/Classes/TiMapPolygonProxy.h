/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import <MapKit/MapKit.h>
#import <TitaniumKit/TiBase.h>
#import <TitaniumKit/TiViewProxy.h>

@class TiMapViewProxy;

@interface TiMapPolygonProxy : TiProxy {
  MKPolygon *polygon;
  MKPolygonRenderer *polygonRenderer;

  float strokeWidth;
  TiColor *fillColor;
  TiColor *strokeColor;
}

@property (nonatomic, readonly) MKPolygon *polygon;
@property (nonatomic, readonly) MKPolygonRenderer *polygonRenderer;

@end
