/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMapView.h"
#import "TiMapCameraProxy.h"

@interface TiMapIOS7View : TiMapView {
@private
    KrollCallback *cameraAnimationCallback;
}

-(TiMapCameraProxy*)camera;
-(void)animateCamera:(id)args;
-(void)showAnnotations:(id)args;

@end
