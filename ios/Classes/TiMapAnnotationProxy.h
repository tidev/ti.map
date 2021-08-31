/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import <MapKit/MapKit.h>
#import <TitaniumKit/TiBase.h>
#import <TitaniumKit/TiViewProxy.h>

@class TiMapViewProxy;

@interface TiMapAnnotationProxy : TiViewProxy <MKAnnotation, TiProxyObserver> {
  @private
  int tag;
  TiMapViewProxy *delegate;
  BOOL needsRefreshing;
  BOOL needsRefreshingWithSelection;
  BOOL placed;
  CGPoint offset;
}

// Center latitude and longitude of the annotion view.
@property (nonatomic, readonly) CLLocationCoordinate2D coordinate;
@property (nonatomic, readwrite, assign) TiMapViewProxy *delegate;
@property (nonatomic, readonly) BOOL needsRefreshingWithSelection;
@property (nonatomic, readwrite, assign) BOOL placed;
@property (nonatomic, readonly) CGPoint offset;
@property (nonatomic, retain) id<UIViewControllerPreviewing> controllerPreviewing;

// Title and subtitle for use by selection UI.
- (NSString *)title;
- (NSString *)subtitle;
- (id)pincolor;
- (BOOL)animatesDrop;
- (void)setHidden:(id)value;
- (UIView *)leftViewAccessory;
- (UIView *)rightViewAccessory;
- (int)tag;
- (void)animate:(id)arg;
- (void)rotate:(id)arg;

@end
