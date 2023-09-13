/**
 * Axway Titanium
 * Copyright (c) 2009-present by Axway Appcelerator. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiProxy.h"
#import <MapKit/MapKit.h>

@interface TiMapFeatureAnnotationProxy : TiProxy {
#if IS_SDK_IOS_16
  MKMapFeatureAnnotation *_annotation;
#endif
}

#if IS_SDK_IOS_16
- (id)_initWithPageContext:(id<TiEvaluator>)context andAnnotation:(MKMapFeatureAnnotation *)annotation;
- (MKMapFeatureAnnotation *)annotation;
#endif

@end
