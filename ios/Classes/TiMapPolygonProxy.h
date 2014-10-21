//
//  TiMapPolygonProxy.h
//  map
//
//  Created by Adam St. John on 2014-06-09.
//
//

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
