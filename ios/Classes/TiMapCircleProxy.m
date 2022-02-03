/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMapCircleProxy.h"

@implementation TiMapCircleProxy

@synthesize circleRenderer;

- (void)dealloc
{
  RELEASE_TO_NIL(circleRenderer);
  RELEASE_TO_NIL(fillColor);
  RELEASE_TO_NIL(strokeColor);

  [super dealloc];
}

- (NSArray *)keySequence
{
  return @[ @"center", @"radius" ];
}

- (void)_initWithProperties:(NSDictionary *)properties
{
  if ([properties objectForKey:@"center"] == nil) {
    [self throwException:@"missing required center property" subreason:nil location:CODELOCATION];
  }

  if ([properties objectForKey:@"radius"] == nil) {
    [self throwException:@"missing required radius property" subreason:nil location:CODELOCATION];
  }

  [super _initWithProperties:properties];
}

#pragma mark Internal

- (NSString *)apiName
{
  return @"Ti.Map.Circle";
}

- (MKCircleRenderer *)circleRenderer
{
  if (circleRenderer == nil) {
    circleRenderer = [[[MKCircleRenderer alloc] initWithCircle:[MKCircle circleWithCenterCoordinate:center radius:radius]] retain];
    [circleRenderer setFillColor:fillColor ? [fillColor color] : [UIColor blackColor]];
  }

  return circleRenderer;
}

#pragma mark Public APIs

// Center can either be a an array of [longitude, latitude] or
// a longitude, latitude object.
// e.g. [123.33, 34.44] or {longitude: 123.33, latitude, 34.44}
- (void)setCenter:(id)value
{
  if (!value) {
    [self throwException:@"missing required center data" subreason:nil location:CODELOCATION];
  }

  CLLocationDegrees lat;
  CLLocationDegrees lon;
  if ([value isKindOfClass:[NSDictionary class]]) {
    lat = [TiUtils doubleValue:[value objectForKey:@"latitude"]];
    lon = [TiUtils doubleValue:[value objectForKey:@"longitude"]];
    center = CLLocationCoordinate2DMake(lat, lon);
  } else if ([value isKindOfClass:[NSArray class]]) {
    lat = [TiUtils doubleValue:[value objectAtIndex:1]];
    lon = [TiUtils doubleValue:[value objectAtIndex:0]];
    center = CLLocationCoordinate2DMake(lat, lon);
  }

  [self replaceValue:value forKey:@"center" notification:NO];
}

- (id)center
{
  return [self valueForUndefinedKey:@"center"];
}

- (void)setFillColor:(id)value
{
  if (fillColor != nil) {
    RELEASE_TO_NIL(fillColor);
  }
  fillColor = [[TiUtils colorValue:value] retain];
  [[self circleRenderer] setFillColor:(fillColor == nil ? [UIColor blackColor] : [fillColor color])];
  [self replaceValue:value forKey:@"fillColor" notification:NO];
}

- (void)setStrokeColor:(id)value
{
  if (strokeColor != nil) {
    RELEASE_TO_NIL(strokeColor);
  }
  strokeColor = [[TiUtils colorValue:value] retain];
  [[self circleRenderer] setStrokeColor:(strokeColor == nil ? [UIColor blackColor] : [strokeColor color])];
  [self replaceValue:value forKey:@"strokeColor" notification:NO];
}

- (void)setStrokeWidth:(id)value
{
  strokeWidth = [TiUtils floatValue:value];
  [[self circleRenderer] setLineWidth:strokeWidth];
  [self replaceValue:value forKey:@"strokeWidth" notification:NO];
}

- (void)setOpacity:(id)value
{
  alpha = [TiUtils floatValue:value];
  [[self circleRenderer] setAlpha:alpha];
  [self replaceValue:value forKey:@"opacity" notification:NO];
}

@end
