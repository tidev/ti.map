/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
 
#import "TiModule.h"

#define MAKE_IOS7_SYSTEM_PROP(name,map) \
-(NSNumber*)name \
{\
if (![TiUtils isIOS7OrGreater]) {\
const char *propName =  #name;\
[TiMapModule logAddedIniOS7Warning:[NSString stringWithUTF8String:propName]];\
return nil;\
}\
return [NSNumber numberWithInt:map];\
}\

@interface TiMapModule : TiModule {

}

+(void)logAddedIniOS7Warning:(NSString*)name;

@property(nonatomic,readonly) NSNumber *STANDARD_TYPE;
@property(nonatomic,readonly) NSNumber *NORMAL_TYPE; // For parity with Android
@property(nonatomic,readonly) NSNumber *SATELLITE_TYPE;
@property(nonatomic,readonly) NSNumber *HYBRID_TYPE;
@property(nonatomic,readonly) NSNumber *ANNOTATION_RED;
@property(nonatomic,readonly) NSNumber *ANNOTATION_GREEN;
@property(nonatomic,readonly) NSNumber *ANNOTATION_PURPLE;

@property(nonatomic,readonly) NSNumber *ANNOTATION_DRAG_STATE_NONE;
@property(nonatomic,readonly) NSNumber *ANNOTATION_DRAG_STATE_START;
@property(nonatomic,readonly) NSNumber *ANNOTATION_DRAG_STATE_DRAG;
@property(nonatomic,readonly) NSNumber *ANNOTATION_DRAG_STATE_CANCEL;
@property(nonatomic,readonly) NSNumber *ANNOTATION_DRAG_STATE_END;

@end
