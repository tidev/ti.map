//
//  TiMapCircleProxy.h
//  map
//
//  Created by Adam St. John on 2014-10-20.
//
//

#ifndef map_TiMapCircleProxy_h
#define map_TiMapCircleProxy_h

#import "TiBase.h"
#import "TiViewProxy.h"
#import <MapKit/MapKit.h>

@class TiMapViewProxy;

@interface TiMapCircleProxy : TiProxy {


    MKCircle *circle;
    MKCircleRenderer *circleRenderer;

    CLLocationCoordinate2D center;
    double radius;
    double strokeWidth;
    TiColor *strokeColor;
    TiColor *fillColor;

}



@property (nonatomic, readonly) MKCircle *circle;
@property (nonatomic, readonly) MKCircleRenderer *circleRenderer;




@end


#endif
