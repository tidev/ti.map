/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2015 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMapPolylineProxy.h"




@implementation TiMapPolylineProxy


@synthesize polyline, polylineRenderer;


-(void)dealloc
{

    RELEASE_TO_NIL(polyline);
    RELEASE_TO_NIL(polylineRenderer);
    RELEASE_TO_NIL(strokeColor);

    [super dealloc];
}

-(void)_initWithProperties:(NSDictionary*)properties
{
    if ([properties objectForKey:@"points"] == nil) {
        [self throwException:@"missing required points property" subreason:nil location:CODELOCATION];
    }

    [super _initWithProperties:properties];

    [self setupPolyline];
}


#pragma mark Internal


-(NSString*)apiName
{
    return @"Ti.Map.Polyline";
}


-(void)setupPolyline
{
    id points = [self valueForKey:@"points"];
    CLLocationCoordinate2D* coordArray = malloc(sizeof(CLLocationCoordinate2D) * [points count]);

    for (int i = 0; i < [points count]; i++) {
        id locObj = [points objectAtIndex:i];
        CLLocationCoordinate2D coord = [self processLocation:locObj];
        coordArray[i] = coord;
    }

    polyline = [[MKPolyline polylineWithCoordinates:coordArray count:[points count]] retain];
    free(coordArray);
    polylineRenderer = [[[MKPolylineRenderer alloc] initWithPolyline:polyline] retain];

    [self applyStrokeColor];
    [self applyStrokeWidth];

}

// A location can either be a an array of longitude, latitude pairings or
// an array of longitude, latitude objects.
// e.g. [ [123.33, 34.44], [100.39, 78.23], etc. ]
// [ {longitude: 123.33, latitude, 34.44}, {longitude: 100.39, latitude: 78.23}, etc. ]
-(CLLocationCoordinate2D)processLocation:(id)locObj
{
    CLLocationDegrees lat;
    CLLocationDegrees lon;
    CLLocationCoordinate2D coord;

    if ([locObj isKindOfClass:[NSDictionary class]]) {
        lat = [TiUtils doubleValue:[locObj objectForKey:@"latitude"]];
        lon = [TiUtils doubleValue:[locObj objectForKey:@"longitude"]];
        coord = CLLocationCoordinate2DMake(lat, lon);
    } else if ([locObj isKindOfClass:[NSArray class]]) {
        lat = [TiUtils doubleValue:[locObj objectAtIndex:1]];
        lon = [TiUtils doubleValue:[locObj objectAtIndex:0]];
        coord = CLLocationCoordinate2DMake(lat, lon);
    }
    return coord;
}



-(void)applyStrokeColor
{
    if (polylineRenderer != nil) {
        polylineRenderer.strokeColor = strokeColor == nil? [UIColor blackColor] : [strokeColor color];
    }
}

-(void)applyStrokeWidth
{
    if (polylineRenderer != nil) {
        polylineRenderer.lineWidth = strokeWidth;
    }
}

#pragma mark Public APIs

-(void)setPoints:(id)value
{
    ENSURE_TYPE(value, NSArray);
    if (![value count]) {
        [self throwException:@"missing required points data" subreason:nil location:CODELOCATION];
    }
    [self replaceValue:value forKey:@"points" notification:NO];
}


-(void)setStrokeColor:(id)value
{
    if (strokeColor != nil) {
        RELEASE_TO_NIL(strokeColor);
    }
    strokeColor = [[TiColor colorNamed:value] retain];
    [self applyStrokeColor];
}

-(void)setStrokeWidth:(id)value
{
    strokeWidth = [TiUtils floatValue:value];
    [self applyStrokeWidth];
}



@end
