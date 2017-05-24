/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiUtils.h"
#import "TiMapCameraProxy.h"

@implementation TiMapCameraProxy

-(id)init
{
	return [self initWithCamera:nil];
}

-(id)initWithCamera:(MKMapCamera*)camera
{
    if (self = [super init]) {
        camera_ = camera;
    }
    return self;
}

-(void)_initWithProperties:(NSDictionary*)properties
{
    id eyeCoord = [properties valueForKey:@"eyeCoordinate"];
    id eyeAlt = [properties valueForKey:@"altitude"];
    id centerCoord = [properties valueForKey:@"centerCoordinate"];
    
    if (eyeCoord != nil && eyeAlt != nil && centerCoord != nil) {
        CLLocationCoordinate2D eyeCoordinate = [self locationCoordinatesFromDict:eyeCoord];
        double eyeAltitude = [TiUtils doubleValue:eyeAlt];
        CLLocationCoordinate2D centerCoordinate = [self locationCoordinatesFromDict:eyeCoord];
        
        camera_ = [MKMapCamera cameraLookingAtCenterCoordinate:centerCoordinate
                                             fromEyeCoordinate:eyeCoordinate
                                                   eyeAltitude:eyeAltitude];
    }
	
	[super _initWithProperties:properties];
}

-(NSString*)apiName
{
    return @"Ti.Map.Camera";
}

-(MKMapCamera*)camera
{
    if (camera_ == nil) {
        camera_ = [MKMapCamera camera];
    }
    return camera_;
}

-(void)setAltitude:(id)value
{
    ENSURE_SINGLE_ARG(value, NSNumber);
    TiThreadPerformOnMainThread(^{
        [self camera].altitude = [TiUtils doubleValue:value];
    }, NO);
}
-(NSNumber*)altitude
{
    __block NSNumber *result;
    TiThreadPerformOnMainThread(^{
        result = [NSNumber numberWithDouble:[self camera].altitude];
    }, YES);
    
    return result;
}

-(void)setCenterCoordinate:(id)args
{
    ENSURE_SINGLE_ARG(args, NSDictionary);
    TiThreadPerformOnMainThread(^{
        [self camera].centerCoordinate = [self locationCoordinatesFromDict:args];
    }, NO);
}
-(NSDictionary*)centerCoordinate
{
    __block NSDictionary *result;
    TiThreadPerformOnMainThread(^{
        CLLocationCoordinate2D centerCord = [self camera].centerCoordinate;
        result = [NSDictionary dictionaryWithObjectsAndKeys:
                  [NSNumber numberWithDouble:centerCord.latitude], @"latitude",
                  [NSNumber numberWithDouble:centerCord.longitude], @"longitude",
                  nil];
    }, YES);
    
    return result;
}

-(void)setHeading:(id)value
{
    ENSURE_SINGLE_ARG(value, NSNumber);
    TiThreadPerformOnMainThread(^{
        [self camera].heading = [TiUtils doubleValue:value];
    }, NO);
}
-(NSNumber *)heading
{
    __block NSNumber *result;
    TiThreadPerformOnMainThread(^{
        result = [NSNumber numberWithDouble:[self camera].heading];
    }, YES);
    
    return result;
}

-(void)setPitch:(id)value
{
    ENSURE_SINGLE_ARG(value, NSNumber);
    TiThreadPerformOnMainThread(^{
        [self camera].pitch = [TiUtils doubleValue:value];
    }, NO);
}
-(NSNumber *)pitch
{
    __block NSNumber *result;
    TiThreadPerformOnMainThread(^{
        result = [NSNumber numberWithDouble:[self camera].pitch];
    }, YES);
    
    return result;
}

# pragma mark Utils

-(CLLocationCoordinate2D)locationCoordinatesFromDict:(NSDictionary*)dict
{
    CLLocationCoordinate2D result;
	result.latitude = [TiUtils doubleValue:[dict valueForKey:@"latitude"]];
	result.longitude = [TiUtils doubleValue:[dict valueForKey:@"longitude"]];
    return result;
}

@end
