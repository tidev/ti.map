/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMapUtils.h"
#import "TiMapView.h"
#import <Contacts/CNPostalAddress.h>
#import <Contacts/CNPostalAddressFormatter.h>

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

+ (NSDictionary<NSString *, id> *)dictionaryFromPlacemark:(CLPlacemark *)placemark
{
  NSMutableDictionary *place = [NSMutableDictionary dictionary];

  if (placemark.thoroughfare) {
    place[@"street"] = placemark.thoroughfare;
  }
  if (placemark.locality) {
    place[@"city"] = placemark.locality;
  }
  if (placemark.administrativeArea) {
    place[@"state"] = placemark.administrativeArea;
  }
  if (placemark.country) {
    place[@"country"] = placemark.country;
  }
  if (placemark.postalCode) {
    place[@"postalCode"] = placemark.postalCode;
  }
  if (placemark.ISOcountryCode) {
    place[@"countryCode"] = placemark.ISOcountryCode;
  }
  if (placemark.postalAddress) {
    place[@"address"] = [CNPostalAddressFormatter stringFromPostalAddress:placemark.postalAddress style:CNPostalAddressFormatterStyleMailingAddress];
  }

  return place;
}

+ (MKLocalSearchCompleterResultType)mappedResultTypes:(NSArray<NSNumber *> *)inputResultTypes
{
  MKLocalSearchCompleterResultType resultTypes = 0;

  for (NSNumber *number in inputResultTypes) {
    MKLocalSearchCompleterResultType typeValue = [number unsignedIntegerValue];
    resultTypes |= typeValue;
  }

  return resultTypes;
}

@end
