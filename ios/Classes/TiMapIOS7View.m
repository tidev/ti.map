/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMapIOS7View.h"
#import "TiMapUtils.h"

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

-(TiMapCameraProxy*)camera
{
    return [TiMapUtils returnValueOnMainThread:^id{
        return [[TiMapCameraProxy alloc] initWithCamera:[self map].camera];
    }];
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
    TiThreadPerformOnMainThread(^{
        [self map].camera = [value camera];
    }, YES);
}

-(void)setPitchEnabled_:(id)value
{
    TiThreadPerformOnMainThread(^{
        [self map].pitchEnabled = [TiUtils boolValue:value];
    }, YES);
}

-(void)setRotateEnabled_:(id)value
{
    TiThreadPerformOnMainThread(^{
        [self map].rotateEnabled = [TiUtils boolValue:value];
    }, YES);
}

-(void)setShowsBuildings_:(id)value
{
    TiThreadPerformOnMainThread(^{
        [self map].showsBuildings = [TiUtils boolValue:value];
    }, YES);
}

-(void)setShowsPointsOfInterest_:(id)value
{
    TiThreadPerformOnMainThread(^{
        [self map].showsPointsOfInterest = [TiUtils boolValue:value];
    }, YES);
}

-(void)setShowsCompass_:(id)value
{
    DEPRECATED_REPLACED(@"View.showsCompass", @"6.1.0", @"View.compassEnabled");
    [self setCompassEnabled_:value];
}

-(void)setCompassEnabled_:(id)value
{
    if ([TiUtils isIOS9OrGreater] == YES) {
#ifdef __IPHONE_9_0
        TiThreadPerformOnMainThread(^{
            [[self map] setShowsCompass:[TiUtils boolValue:value]];
        }, YES);
#endif
    } else {
        NSLog(@"[WARN] The property 'showsCompass' is only available on iOS 9 and later.");
    }
}

-(void)setShowsScale_:(id)value
{
    if ([TiUtils isIOS9OrGreater] == YES) {
#ifdef __IPHONE_9_0
        TiThreadPerformOnMainThread(^{
            [self map].showsScale = [TiUtils boolValue:value];
        }, YES);
#endif
    } else {
        NSLog(@"[WARN] The property 'showsScale' is only available on iOS 9 and later.");
    }
}

-(void)setShowsTraffic_:(id)value
{
    if ([TiUtils isIOS9OrGreater] == YES) {
#ifdef __IPHONE_9_0
        TiThreadPerformOnMainThread(^{
            [self map].showsTraffic = [TiUtils boolValue:value];
        }, YES);
#endif
    } else {
        NSLog(@"[WARN] The property 'showsTraffic' is only available on iOS 9 and later.");
    }}

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
    TiThreadPerformOnMainThread(^{
        [UIView animateWithDuration:(duration / 1000)
                              delay:(delay / 1000)
                            options:curve
                         animations:^{
                             [self map].camera = [cameraProxy camera];
                         }
                         completion:nil];
    }, NO);
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
