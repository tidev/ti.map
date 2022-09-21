/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import <TitaniumKit/TiModule.h>
#import <MapKit/MapKit.h>

#if IS_SDK_IOS_16
@interface TiMapModule : TiModule <MKLookAroundViewControllerDelegate> {
#else
  @interface TiMapModule : TiModule {
#endif
  UIColor *colorRed;
}

@property (nonatomic, readonly) NSNumber *STANDARD_TYPE;
@property (nonatomic, readonly) NSNumber *NORMAL_TYPE; // For parity with Android
@property (nonatomic, readonly) NSNumber *SATELLITE_TYPE;
@property (nonatomic, readonly) NSNumber *HYBRID_TYPE;
@property (nonatomic, readonly) NSNumber *HYBRID_FLYOVER_TYPE;
@property (nonatomic, readonly) NSNumber *SATELLITE_FLYOVER_TYPE;
@property (nonatomic, readonly) NSNumber *MUTED_STANDARD_TYPE;

@property (nonatomic, readonly) NSNumber *ANNOTATION_RED;
@property (nonatomic, readonly) NSNumber *ANNOTATION_GREEN;
@property (nonatomic, readonly) NSNumber *ANNOTATION_PURPLE;

@property (nonatomic, readonly) NSNumber *ANNOTATION_DRAG_STATE_NONE;
@property (nonatomic, readonly) NSNumber *ANNOTATION_DRAG_STATE_START;
@property (nonatomic, readonly) NSNumber *ANNOTATION_DRAG_STATE_DRAG;
@property (nonatomic, readonly) NSNumber *ANNOTATION_DRAG_STATE_CANCEL;
@property (nonatomic, readonly) NSNumber *ANNOTATION_DRAG_STATE_END;

@end
