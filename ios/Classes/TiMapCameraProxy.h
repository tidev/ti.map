/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiProxy.h"
#import <MapKit/MapKit.h>

@interface TiMapCameraProxy : TiProxy {
    
@private
    MKMapCamera *camera_;
}

-(id)init;
-(id)initWithCamera:(MKMapCamera*)camera;

-(MKMapCamera*)camera;

@end
