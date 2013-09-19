/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMapModule.h"
#import <MapKit/MapKit.h>

@implementation TiMapModule

#pragma mark Internal

// this is generated for your module, please do not change it
-(id)moduleGUID
{
	return @"b15e1285-ae9f-44a1-8b15-fdabb289cca9";
}

// this is generated for your module, please do not change it
-(NSString*)moduleId
{
	return @"ti.map";
}

MAKE_SYSTEM_PROP(STANDARD_TYPE,MKMapTypeStandard);
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

@end
