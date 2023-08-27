/**
 * Axway Titanium
 * Copyright (c) 2009-present by Axway Appcelerator. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMapFeatureAnnotationProxy.h"
#import <MapKit/MapKit.h>

@implementation TiMapFeatureAnnotationProxy

#if IS_SDK_IOS_16
- (id)_initWithPageContext:(id<TiEvaluator>)context andAnnotation:(MKMapFeatureAnnotation *)annotation
{
  if (self = [super _initWithPageContext:pageContext]) {
    _annotation = annotation;
  }
  
  return self;
}

- (MKMapFeatureAnnotation *)annotation
{
  return _annotation;
}
#endif

@end
