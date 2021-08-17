/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMapModule.h"
#import "TiMapCameraProxy.h"
#import "TiMapConstants.h"
#import "TiMapViewProxy.h"
#import <MapKit/MapKit.h>

@implementation TiMapModule

#pragma mark Internal

// this is generated for your module, please do not change it
- (id)moduleGUID
{
  return @"fee93b77-8eb3-418c-8f04-013664c4af83";
}

// this is generated for your module, please do not change it
- (NSString *)moduleId
{
  return @"ti.map";
}

- (NSString *)apiName
{
  return @"Ti.Map";
}

#pragma mark Public APIs

- (TiMapViewProxy *)createView:(id)args
{
  return [[[TiMapViewProxy alloc] _initWithPageContext:[self pageContext] args:args] autorelease];
}

- (TiMapCameraProxy *)createCamera:(id)args
{
  return [[[TiMapCameraProxy alloc] _initWithPageContext:[self pageContext] args:args] autorelease];
}

MAKE_SYSTEM_PROP(STANDARD_TYPE, MKMapTypeStandard);
MAKE_SYSTEM_PROP(NORMAL_TYPE, MKMapTypeStandard); // For parity with Android
MAKE_SYSTEM_PROP(SATELLITE_TYPE, MKMapTypeSatellite);
MAKE_SYSTEM_PROP(HYBRID_TYPE, MKMapTypeHybrid);
MAKE_SYSTEM_PROP(HYBRID_FLYOVER_TYPE, MKMapTypeHybridFlyover);
MAKE_SYSTEM_PROP(SATELLITE_FLYOVER_TYPE, MKMapTypeSatelliteFlyover);
MAKE_SYSTEM_PROP(MUTED_STANDARD_TYPE, MKMapTypeMutedStandard);
MAKE_SYSTEM_PROP(ANNOTATION_RED, TiMapAnnotationPinColorRed);
MAKE_SYSTEM_PROP(ANNOTATION_GREEN, TiMapAnnotationPinColorGreen);
MAKE_SYSTEM_PROP(ANNOTATION_PURPLE, TiMapAnnotationPinColorPurple);
MAKE_SYSTEM_PROP(ANNOTATION_AZURE, TiMapAnnotationPinColorAzure);
MAKE_SYSTEM_PROP(ANNOTATION_BLUE, TiMapAnnotationPinColorBlue);
MAKE_SYSTEM_PROP(ANNOTATION_CYAN, TiMapAnnotationPinColorCyan);
MAKE_SYSTEM_PROP(ANNOTATION_MAGENTA, TiMapAnnotationPinColorMagenta);
MAKE_SYSTEM_PROP(ANNOTATION_ORANGE, TiMapAnnotationPinColorOrange);
MAKE_SYSTEM_PROP(ANNOTATION_ROSE, TiMapAnnotationPinColorRose);
MAKE_SYSTEM_PROP(ANNOTATION_VIOLET, TiMapAnnotationPinColorViolet);
MAKE_SYSTEM_PROP(ANNOTATION_YELLOW, TiMapAnnotationPinColorYellow);

MAKE_SYSTEM_PROP(ANNOTATION_DRAG_STATE_NONE, MKAnnotationViewDragStateNone);
MAKE_SYSTEM_PROP(ANNOTATION_DRAG_STATE_START, MKAnnotationViewDragStateStarting);
MAKE_SYSTEM_PROP(ANNOTATION_DRAG_STATE_DRAG, MKAnnotationViewDragStateDragging);
MAKE_SYSTEM_PROP(ANNOTATION_DRAG_STATE_CANCEL, MKAnnotationViewDragStateCanceling);
MAKE_SYSTEM_PROP(ANNOTATION_DRAG_STATE_END, MKAnnotationViewDragStateEnding);

MAKE_SYSTEM_PROP(OVERLAY_LEVEL_ABOVE_LABELS, MKOverlayLevelAboveLabels);
MAKE_SYSTEM_PROP(OVERLAY_LEVEL_ABOVE_ROADS, MKOverlayLevelAboveRoads);

MAKE_SYSTEM_PROP(POLYLINE_PATTERN_DASHED, TiMapOverlyPatternTypeDashed);
MAKE_SYSTEM_PROP(POLYLINE_PATTERN_DOTTED, TiMapOverlyPatternTypeDotted);

MAKE_SYSTEM_PROP(FEATURE_VISIBILITY_ADAPTIVE, MKFeatureVisibilityAdaptive);
MAKE_SYSTEM_PROP(FEATURE_VISIBILITY_HIDDEN, MKFeatureVisibilityHidden);
MAKE_SYSTEM_PROP(FEATURE_VISIBILITY_VISIBLE, MKFeatureVisibilityVisible);

MAKE_SYSTEM_PROP(ANNOTATION_VIEW_COLLISION_MODE_RECTANGLE, MKAnnotationViewCollisionModeRectangle);
MAKE_SYSTEM_PROP(ANNOTATION_VIEW_COLLISION_MODE_CIRCLE, MKAnnotationViewCollisionModeCircle);

MAKE_SYSTEM_PROP_DBL(FEATURE_DISPLAY_PRIORITY_REQUIRED, MKFeatureDisplayPriorityRequired);
MAKE_SYSTEM_PROP_DBL(FEATURE_DISPLAY_PRIORITY_DEFAULT_HIGH, MKFeatureDisplayPriorityDefaultHigh);
MAKE_SYSTEM_PROP_DBL(FEATURE_DISPLAY_PRIORITY_DEFAULT_LOW, MKFeatureDisplayPriorityDefaultLow);

@end
