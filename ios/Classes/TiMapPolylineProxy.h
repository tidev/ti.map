//
//  TiMapPolylineProxy.h
//  map
//
//  Created by Adam St. John on 2014-10-21.
//
//

#ifndef map_TiMapPolylineProxy_h
#define map_TiMapPolylineProxy_h

#import "TiBase.h"
#import "TiViewProxy.h"
#import <MapKit/MapKit.h>

@class TiMapViewProxy;

@interface TiMapPolylineProxy : TiProxy {


    MKPolyline *polyline;
    MKPolylineRenderer *polylineRenderer;

    float strokeWidth;
    TiColor *strokeColor;

}



@property (nonatomic, readonly) MKPolyline *polyline;
@property (nonatomic, readonly) MKPolylineRenderer *polylineRenderer;




@end


#endif
