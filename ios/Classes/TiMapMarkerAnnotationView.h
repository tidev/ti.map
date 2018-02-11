/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
#if IS_IOS_11
#import "TiBase.h"
#import <MapKit/MapKit.h>
#import "TiMapView.h"

@interface TiMapMarkerAnnotationView : MKMarkerAnnotationView<TiMapAnnotation>
{
@private
    NSString *lastHitName;
}

-(id)initWithAnnotation:(id<MKAnnotation>)annotation reuseIdentifier:(NSString *)reuseIdentifier map:(TiMapView *)map;
-(NSString *)lastHitName;

@end
#endif
