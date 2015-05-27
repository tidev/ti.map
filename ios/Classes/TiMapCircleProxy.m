/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2015 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMapCircleProxy.h"




@implementation TiMapCircleProxy


@synthesize circle, circleRenderer;


-(void)dealloc
{

    RELEASE_TO_NIL(circle);
    RELEASE_TO_NIL(circleRenderer);
    RELEASE_TO_NIL(fillColor);
    RELEASE_TO_NIL(strokeColor);

    [super dealloc];
}

-(void)_initWithProperties:(NSDictionary*)properties
{
    if ([properties objectForKey:@"center"] == nil) {
        [self throwException:@"missing required center property" subreason:nil location:CODELOCATION];
    }

    if ([properties objectForKey:@"radius"] == nil) {
        [self throwException:@"missing required radius property" subreason:nil location:CODELOCATION];
    }

    [super _initWithProperties:properties];
    [self setupCircle];
}


#pragma mark Internal


-(NSString*)apiName
{
    return @"Ti.Map.Circle";
}


-(void)setupCircle
{
    circle = [[MKCircle circleWithCenterCoordinate:center radius:radius] retain];
    circleRenderer = [[[MKCircleRenderer alloc] initWithCircle:circle] retain];
    [self applyFillColor];
    [self applyStrokeColor];
    [self applyStrokeWidth];
}

-(void)applyFillColor
{
    if (circleRenderer != nil) {
        circleRenderer.fillColor = fillColor == nil ? [UIColor blackColor] : [fillColor color];
    }
}

-(void)applyStrokeColor
{
    if (circleRenderer != nil) {
        circleRenderer.strokeColor = strokeColor == nil? [UIColor blackColor] : [strokeColor color];
    }
}

-(void)applyStrokeWidth
{
    if (circleRenderer != nil) {
        circleRenderer.lineWidth = strokeWidth;
    }
}

#pragma mark Public APIs

// Center can either be a an array of [longitude, latitude] or
// a longitude, latitude object.
// e.g. [123.33, 34.44] or {longitude: 123.33, latitude, 34.44}
-(void)setCenter:(id)value
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

}

-(void)setFillColor:(id)value
{
    if (fillColor != nil) {
        RELEASE_TO_NIL(fillColor);
    }
    fillColor = [[TiColor colorNamed:value] retain];
    [self applyFillColor];
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