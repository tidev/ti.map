/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2015 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiBase.h"
#import "TiViewProxy.h"
#import <MapKit/MapKit.h>

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
