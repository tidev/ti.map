/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2016 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMapSnapshotterProxy.h"
#import "TiMapViewProxy.h"
#import "TiMapView.h"

@implementation TiMapSnapshotterProxy

-(NSString*)apiName
{
    return @"Ti.Map.Snapshotter";
}


-(void)dealloc
{
    RELEASE_TO_NIL(options);
    [super dealloc];
}

-(MKCoordinateRegion)regionFromDict:(NSDictionary*)dict
{
    CLLocationDegrees latitudeDelta = [TiUtils floatValue:@"latitudeDelta" properties:dict];
    CLLocationDegrees longitudeDelta = [TiUtils floatValue:@"longitudeDelta" properties:dict];
    
    CLLocationDegrees latitude = [TiUtils floatValue:@"latitude" properties:dict];
    CLLocationDegrees longitude = [TiUtils floatValue:@"longitude" properties:dict];
    
    return MKCoordinateRegionMake(CLLocationCoordinate2DMake(latitude, longitude), MKCoordinateSpanMake(latitudeDelta, longitudeDelta));
}

-(MKMapSnapshotOptions*)options
{
    if (!options) {
        options = [[MKMapSnapshotOptions alloc] init];
        [options setScale:[[UIScreen mainScreen] scale]];
        CGRect bounds = [[UIScreen mainScreen] bounds];
        [options setSize:bounds.size];
    }
    return options;
}

-(void)setRegion:(id)value
{
    ENSURE_TYPE(value, NSDictionary);
    [[self options] setRegion:[self regionFromDict:value]];
}

-(void)setMapType:(id)value
{
    ENSURE_TYPE(value, NSNumber);
    [[self options] setMapType:[TiUtils intValue:value]];
}

-(void)setShowBuilding:(id)value
{
    ENSURE_TYPE(value, NSNumber);
    [[self options] setShowsBuildings:[TiUtils boolValue:value]];
}

-(void)setShowsPointsOfInterest:(id)value
{
    ENSURE_TYPE(value, NSNumber);
    [[self options] setShowsPointsOfInterest:[TiUtils boolValue:value]];
}

-(void)setSize:(id)args
{
    ENSURE_SINGLE_ARG(args, NSDictionary);
    float width = [TiUtils floatValue:[args objectForKey:@"width"]];
    float height = [TiUtils floatValue:[args objectForKey:@"height"]];
    
    [[self options] setSize:CGSizeMake(width,height)];
}

-(void)takeSnapshot:(id)args
{
    ENSURE_SINGLE_ARG(args, NSDictionary);
    
    KrollCallback *successCallback = [args objectForKey:@"success"];
    KrollCallback *ErrorCallback = [args objectForKey:@"error"];
    
    MKMapSnapshotter *snapshotter = [[MKMapSnapshotter alloc] initWithOptions:[self options]];
        [snapshotter startWithCompletionHandler:^(MKMapSnapshot *snapshot, NSError *error) {
            if (error) {
                [self _fireEventToListener:@"blob" withObject:[TiUtils stringValue:error] listener:ErrorCallback thisObject:nil];
                return;
            }
            
            TiBlob *blob = [[[TiBlob alloc] _initWithPageContext:[self pageContext]] autorelease];
            [blob setImage:[snapshot image]];
            [blob setMimeType:@"image/png" type:TiBlobTypeImage];
            
            NSDictionary *event = [NSDictionary dictionaryWithObject:blob forKey:@"image"];
            [self _fireEventToListener:@"blob" withObject:event listener:successCallback thisObject:nil];
        }];

}

@end
