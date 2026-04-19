/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import <CoreLocation/CoreLocation.h>
#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>

@interface TiMapUtils : NSObject

+ (id)returnValueOnMainThread:(id (^)(void))block;

+ (NSDictionary<NSString *, id> *)dictionaryFromPlacemark:(CLPlacemark *)placemark;

+ (MKLocalSearchCompleterResultType)mappedResultTypes:(NSArray<NSNumber *> *)inputResultTypes;

+ (NSArray<NSDictionary *> *)generateCircleCoordinates:(CLLocationCoordinate2D)coordinate withRadius:(double)radius andTolerance:(double)tolerance;

+ (CLLocationCoordinate2D)processLocation:(id)locObj;

@end
