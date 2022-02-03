/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMapView.h"
#import <MapKit/MapKit.h>
#import <TitaniumKit/TiBase.h>

@interface TiMapImageAnnotationView : MKAnnotationView <TiMapAnnotation> {
  @private

  NSString *lastHitName;
}

- (id)initWithAnnotation:(id<MKAnnotation>)annotation reuseIdentifier:(NSString *)reuseIdentifier map:(TiMapView *)map image:(UIImage *)image;
- (NSString *)lastHitName;

@end
