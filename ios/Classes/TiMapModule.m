/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMapModule.h"
#import "TiMapViewProxy.h"
#import "TiMapIOS7ViewProxy.h"
#import "TiMapCameraProxy.h"
#import <MapKit/MapKit.h>

@implementation TiMapModule

#pragma mark Internal

// this is generated for your module, please do not change it
-(id)moduleGUID
{
	return @"fee93b77-8eb3-418c-8f04-013664c4af83";
}

// this is generated for your module, please do not change it
-(NSString*)moduleId
{
	return @"ti.map";
}

-(NSString*)apiName
{
    return @"Ti.Map";
}

#pragma mark Utils

+(void)logAddedIniOS7Warning:(NSString*)name
{
    NSLog(@"[WARN] `%@` is only supported on iOS 7 and greater.", name);
}

#pragma mark Public APIs

-(TiMapViewProxy*)createView:(id)args
{
    Class mapViewProxyClass = ([TiUtils isIOS7OrGreater]) ? [TiMapIOS7ViewProxy class] : [TiMapViewProxy class];
    return [[[mapViewProxyClass alloc] _initWithPageContext:[self pageContext] args:args] autorelease];
}

-(TiMapCameraProxy*)createCamera:(id)args
{
    if (![TiUtils isIOS7OrGreater]) {
        [TiMapModule logAddedIniOS7Warning:@"createCamera()"];
        return nil;
    }
    return [[[TiMapCameraProxy alloc] _initWithPageContext:[self pageContext] args:args] autorelease];
}

MAKE_SYSTEM_PROP(STANDARD_TYPE,MKMapTypeStandard);
MAKE_SYSTEM_PROP(NORMAL_TYPE,MKMapTypeStandard); // For parity with Android
MAKE_SYSTEM_PROP(SATELLITE_TYPE,MKMapTypeSatellite);
MAKE_SYSTEM_PROP(HYBRID_TYPE,MKMapTypeHybrid);
MAKE_SYSTEM_PROP(ANNOTATION_RED,MKPinAnnotationColorRed);
MAKE_SYSTEM_PROP(ANNOTATION_GREEN,MKPinAnnotationColorGreen);
MAKE_SYSTEM_PROP(ANNOTATION_PURPLE,MKPinAnnotationColorPurple);

MAKE_SYSTEM_PROP(ANNOTATION_DRAG_STATE_NONE,MKAnnotationViewDragStateNone);
MAKE_SYSTEM_PROP(ANNOTATION_DRAG_STATE_START,MKAnnotationViewDragStateStarting);
MAKE_SYSTEM_PROP(ANNOTATION_DRAG_STATE_DRAG,MKAnnotationViewDragStateDragging);
MAKE_SYSTEM_PROP(ANNOTATION_DRAG_STATE_CANCEL,MKAnnotationViewDragStateCanceling);
MAKE_SYSTEM_PROP(ANNOTATION_DRAG_STATE_END,MKAnnotationViewDragStateEnding);

MAKE_IOS7_SYSTEM_PROP(OVERLAY_LEVEL_ABOVE_LABELS,MKOverlayLevelAboveLabels);
MAKE_IOS7_SYSTEM_PROP(OVERLAY_LEVEL_ABOVE_ROADS,MKOverlayLevelAboveRoads);

@end
