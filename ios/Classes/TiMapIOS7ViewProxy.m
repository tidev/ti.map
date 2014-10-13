/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMapIOS7ViewProxy.h"
#import "TiMapIOS7View.h"
#import "TiMapUtils.h"

@implementation TiMapIOS7ViewProxy

-(TiMapCameraProxy*)camera
{
    return [TiMapUtils returnValueOnMainThread:^id{
        return [(TiMapIOS7View *)[self view] camera];
    }];
}

-(void)animateCamera:(id)args
{
    TiThreadPerformOnMainThread(^{
        [(TiMapIOS7View *)[self view] animateCamera:args];
    }, NO);
}

-(void)showAnnotations:(id)args
{
    TiThreadPerformOnMainThread(^{
        [(TiMapIOS7View *)[self view] showAnnotations:args];
    }, NO);
}

@end
