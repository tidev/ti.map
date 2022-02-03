/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMapPolygonProxy.h"

@implementation TiMapPolygonProxy

@synthesize polygon, polygonRenderer;

- (void)dealloc
{
  RELEASE_TO_NIL(polygon);
  RELEASE_TO_NIL(polygonRenderer);
  RELEASE_TO_NIL(fillColor);
  RELEASE_TO_NIL(strokeColor);

  [super dealloc];
}

- (void)_initWithProperties:(NSDictionary *)properties
{
  if ([properties objectForKey:@"points"] == nil) {
    [self throwException:@"missing required points property" subreason:nil location:CODELOCATION];
  }

  [super _initWithProperties:properties];

  [self setupPolygon];
}

#pragma mark Internal

- (NSString *)apiName
{
  return @"Ti.Map.Polygon";
}

- (void)setupPolygon
{
  id points = [self valueForKey:@"points"];
  CLLocationCoordinate2D *coordArray = malloc(sizeof(CLLocationCoordinate2D) * [points count]);

  for (int i = 0; i < [points count]; i++) {
    id locObj = [points objectAtIndex:i];
    CLLocationCoordinate2D coord = [self processLocation:locObj];
    coordArray[i] = coord;
  }

  id holes = [self valueForKey:@"holes"];
  if (holes != nil && [holes count] > 0) {
    // Holes is a 3-d array containing arrays of lng/lat pairings defining each hole or arrays of
    // lat/lng objects defining each hole.
    NSMutableArray *holePolygons = [NSMutableArray array];
    for (int i = 0; i < [holes count]; i++) {
      id holeObj = [holes objectAtIndex:i];
      CLLocationCoordinate2D *hole = malloc(sizeof(CLLocationCoordinate2D) * [holeObj count]);

      for (int j = 0; j < [holeObj count]; ++j) {
        id obj = [holeObj objectAtIndex:j];
        CLLocationCoordinate2D coord = [self processLocation:obj];
        hole[j] = coord;
      }

      MKPolygon *interiorPolygon = [MKPolygon polygonWithCoordinates:hole count:[holeObj count]];
      free(hole);
      [holePolygons addObject:interiorPolygon];
    }

    polygon = [[MKPolygon polygonWithCoordinates:coordArray count:[points count] interiorPolygons:holePolygons] retain];
  } else {
    polygon = [[MKPolygon polygonWithCoordinates:coordArray count:[points count]] retain];
  }

  free(coordArray);
  polygonRenderer = [[[MKPolygonRenderer alloc] initWithPolygon:polygon] retain];

  [self applyFillColor];
  [self applyStrokeColor];
  [self applyStrokeWidth];
}

// A location can either be a an array of longitude, latitude pairings or
// an array of longitude, latitude objects.
// e.g. [ [123.33, 34.44], [100.39, 78.23], etc. ]
// [ {longitude: 123.33, latitude, 34.44}, {longitude: 100.39, latitude: 78.23}, etc. ]
- (CLLocationCoordinate2D)processLocation:(id)locObj
{
  if ([locObj isKindOfClass:[NSDictionary class]]) {
    CLLocationDegrees lat = [TiUtils doubleValue:[locObj objectForKey:@"latitude"]];
    CLLocationDegrees lon = [TiUtils doubleValue:[locObj objectForKey:@"longitude"]];

    return CLLocationCoordinate2DMake(lat, lon);
  } else if ([locObj isKindOfClass:[NSArray class]]) {
    CLLocationDegrees lat = [TiUtils doubleValue:[locObj objectAtIndex:1]];
    CLLocationDegrees lon = [TiUtils doubleValue:[locObj objectAtIndex:0]];

    return CLLocationCoordinate2DMake(lat, lon);
  } else {
      [self throwException:@"Invalid coordinate tyoe" subreason:@"Use either a dictionary or array" location:CODELOCATION];
  }

  return kCLLocationCoordinate2DInvalid;
}

- (void)applyFillColor
{
  if (polygonRenderer != nil) {
    polygonRenderer.fillColor = fillColor == nil ? [UIColor blackColor] : [fillColor color];
  }
}

- (void)applyStrokeColor
{
  if (polygonRenderer != nil) {
    polygonRenderer.strokeColor = strokeColor == nil ? [UIColor blackColor] : [strokeColor color];
  }
}

- (void)applyStrokeWidth
{
  if (polygonRenderer != nil) {
    polygonRenderer.lineWidth = strokeWidth;
  }
}

#pragma mark Public APIs

- (void)setPoints:(id)value
{
  ENSURE_TYPE(value, NSArray);
  if (![value count]) {
    [self throwException:@"missing required points data" subreason:nil location:CODELOCATION];
  }
  [self replaceValue:value forKey:@"points" notification:NO];
}

- (void)setFillColor:(id)value
{
  if (fillColor != nil) {
    RELEASE_TO_NIL(fillColor);
  }
  fillColor = [[TiUtils colorValue:value] retain];
  [self applyFillColor];
}

- (void)setStrokeColor:(id)value
{
  if (strokeColor != nil) {
    RELEASE_TO_NIL(strokeColor);
  }
  strokeColor = [[TiUtils colorValue:value] retain];
  [self applyStrokeColor];
}

- (void)setStrokeWidth:(id)value
{
  strokeWidth = [TiUtils floatValue:value];
  [self applyStrokeWidth];
}

@end
