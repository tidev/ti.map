/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMapView.h"
#import <MapKit/MapKit.h>
#import <TitaniumKit/TiBase.h>

@class TiViewProxy;

@interface TiMapCustomAnnotationView : MKAnnotationView <TiMapAnnotation> {
  @private
  NSString *lastHitName;
  TiViewProxy *theProxy;
  UIView *wrapperView;
}

- (id)initWithAnnotation:(id<MKAnnotation>)annotation reuseIdentifier:(NSString *)reuseIdentifier map:(TiMapView *)map;
- (NSString *)lastHitName;
- (void)setProxy:(TiViewProxy *)customView;

@end
