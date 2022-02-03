/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMapRouteProxy.h"
#import "TiMapModule.h"
#import <TitaniumKit/TiBase.h>
#import <TitaniumKit/TiUtils.h>

@implementation TiMapRouteProxy

@synthesize level, routeLine, routeRenderer;

- (id)init
{
  if (self = [super init]) {
    width = 10; // Default route width
  }
  return self;
}

- (void)dealloc
{
  RELEASE_TO_NIL(color);
  RELEASE_TO_NIL(routeLine);
  RELEASE_TO_NIL(routeRenderer);

  [super dealloc];
}

- (void)_initWithProperties:(NSDictionary *)properties
{
  if ([properties objectForKey:@"points"] == nil) {
    [self throwException:@"missing required points property" subreason:nil location:CODELOCATION];
  }

  [super _initWithProperties:properties];
}

- (NSString *)apiName
{
  return @"Ti.Map.Route";
}

- (void)processPoints:(NSArray *)points
{
  if (routeLine != nil) {
    NSLog(@"[WARN] Route points can not be changed after route creation.");
    return;
  }

  // Construct the MKPolyline
  MKMapPoint *pointArray = malloc(sizeof(MKMapPoint) * points.count);
  for (int i = 0; i < [points count]; ++i) {
    NSDictionary *entry = [points objectAtIndex:i];
    CLLocationDegrees lat = [TiUtils doubleValue:[entry objectForKey:@"latitude"]];
    CLLocationDegrees lon = [TiUtils doubleValue:[entry objectForKey:@"longitude"]];
    CLLocationCoordinate2D coord = CLLocationCoordinate2DMake(lat, lon);
    MKMapPoint pt = MKMapPointForCoordinate(coord);
    pointArray[i] = pt;
  }
  routeLine = [[MKPolyline polylineWithPoints:pointArray count:[points count]] retain];
  free(pointArray);

  routeRenderer = [(id<TiMKOverlayPathUniversal>)[[MKPolylineRenderer alloc] initWithPolyline:routeLine] retain];
  [self applyColor];
  [self applyWidth];
}

- (void)applyColor
{
  if (routeRenderer != nil) {
    routeRenderer.fillColor = routeRenderer.strokeColor = color ? [color color] : [UIColor blueColor];
  }
}

- (void)applyWidth
{
  if (routeRenderer != nil) {
    routeRenderer.lineWidth = width;
  }
}

#pragma mark Public APIs

- (void)setPoints:(id)value
{
  ENSURE_TYPE(value, NSArray);
  if (![value count]) {
    [self throwException:@"missing required points data" subreason:nil location:CODELOCATION];
  }

  [self processPoints:value];
}

- (void)setColor:(id)value
{
  if (color != nil) {
    RELEASE_TO_NIL(color);
  }
  color = [[TiUtils colorValue:value] retain];
  [self applyColor];
}

- (void)setWidth:(id)value
{
  width = [TiUtils floatValue:value];
  [self applyWidth];
}

- (void)setLevel:(id)value
{
  level = [[TiUtils numberFromObject:value] unsignedIntegerValue];
}

@end
