/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMapUtils.h"
#import "TiMapView.h"

@implementation TiMapUtils

+ (id)returnValueOnMainThread:(id (^)(void))block
{
  if ([NSThread isMainThread]) {
    return block();
  }

  __block id result = nil;
  TiThreadPerformOnMainThread(
      ^{
        result = [block() retain];
      },
      YES);
  return [result autorelease];
}

@end
