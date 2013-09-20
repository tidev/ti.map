/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMapIOS7View.h"

@implementation TiMapIOS7View

-(id)init
{
    if (self = [super init]) {
        cameraAnimationCallback = nil;
    }
    return self;
}

-(void)dealloc
{
	if (cameraAnimationCallback) {
        RELEASE_TO_NIL(cameraAnimationCallback);
    }
	[super dealloc];
}

#pragma mark Public APIs iOS 7

-(void)setTintColor_:(id)color
{
    TiColor *ticolor = [TiUtils colorValue:color];
    UIColor* theColor = [ticolor _color];
    [[self map] performSelector:@selector(setTintColor:) withObject:theColor];
    [self performSelector:@selector(setTintColor:) withObject:theColor];
}

-(void)setCamera_:(TiMapCameraProxy*)value
{
    [self map].camera = [value camera];
}
-(TiMapCameraProxy*)camera
{
    return [[[TiMapCameraProxy alloc] initWithCamera:[self map].camera] autorelease];
}

-(void)setPitchEnabled_:(id)value
{
    [self map].pitchEnabled = [TiUtils boolValue:value];
}

-(void)setRotateEnabled_:(id)value
{
    [self map].rotateEnabled = [TiUtils boolValue:value];
}

-(void)setShowsBuildings_:(id)value
{
    [self map].showsBuildings = [TiUtils boolValue:value];
}

-(void)setShowsPointsOfInterest_:(id)value
{
    [self map].showsPointsOfInterest = [TiUtils boolValue:value];
}

-(void)animateCamera:(id)args
{
    enum Args {
        kArgAnimationDict = 0,
        kArgCount,
        kArgCallback = kArgCount
    };
    NSDictionary *animationDict = [args objectAtIndex:kArgAnimationDict];
    ENSURE_TYPE(animationDict, NSDictionary);
    // Callback is optional
    cameraAnimationCallback = ([args count] > kArgCallback) ? [[args objectAtIndex:kArgCallback] retain] : nil;
    
    id cameraProxy = [animationDict objectForKey:@"camera"];
    ENSURE_TYPE(cameraProxy, TiMapCameraProxy);
    
    double duration = [TiUtils doubleValue:[animationDict objectForKey:@"duration"] def:400];
    double delay = [TiUtils doubleValue:[animationDict objectForKey:@"delay"] def:0];
    NSUInteger curve = [TiUtils intValue:[animationDict objectForKey:@"curve"] def:UIViewAnimationOptionCurveEaseInOut];
    
    // Apple says to use `mapView:regionDidChangeAnimated:` instead of `completion`
    // to know when the camera animation has completed
    [UIView animateWithDuration:(duration / 1000)
                          delay:(delay / 1000)
                        options:curve
                     animations:^{
                         [self map].camera = [cameraProxy camera];
                     }
                     completion:nil];
}

-(void)showAnnotations:(id)args
{
    ENSURE_SINGLE_ARG_OR_NIL(args, NSArray);
    // If no annotations are passed in, use the annotations on the map
    if (args == nil) {
        args = [self customAnnotations];
    }
    TiThreadPerformOnMainThread(^{
        [[self map] showAnnotations:args animated:animate];
    },NO);
}

#pragma mark Utils

// These methods override the default implementation in TiMapView
-(void)addOverlay:(MKPolyline*)polyline level:(MKOverlayLevel)level
{
    [map addOverlay:polyline level:level];
}

#pragma mark Delegates

- (void)mapView:(MKMapView *)mapView regionDidChangeAnimated:(BOOL)animated
{
    if (animated && cameraAnimationCallback != nil) {
        [cameraAnimationCallback call:nil thisObject:nil];
        RELEASE_TO_NIL(cameraAnimationCallback);
    }
    [super mapView:mapView regionDidChangeAnimated:animated];
}

@end
