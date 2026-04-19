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

+ (NSArray<NSDictionary *> *)generateCircleCoordinates:(CLLocationCoordinate2D)coordinate withRadius:(double)radius andTolerance:(double)tolerance
{
  NSMutableArray<NSDictionary *> *coordinates = [[NSMutableArray alloc] init];
  double latRadian = coordinate.latitude * M_PI / 180.0;
  double lngRadian = coordinate.longitude * M_PI / 180.0;
  double distance = (radius / 1000.0) / 6371.0; // kms

  for (double angle = 0; angle < 360.0; angle += tolerance) {
    double bearing = angle * M_PI / 180.0;

    double lat2 = asin(sin(latRadian) * cos(distance) + cos(latRadian) * sin(distance) * cos(bearing));
    double lon2 = lngRadian + atan2(sin(bearing) * sin(distance) * cos(latRadian), cos(distance) - sin(latRadian) * sin(lat2));
    lon2 = fmod(lon2 + 3 * M_PI, 2 * M_PI) - M_PI; // normalize to -180..+180º

    NSNumber *latitude = @(lat2 * (180.0 / M_PI));
    NSNumber *longitude = @(lon2 * (180.0 / M_PI));
    [coordinates addObject:@{ @"latitude" : latitude, @"longitude" : longitude }];
  }

  return coordinates;
}

// A location can either be a an array of longitude, latitude pairings or
// an array of longitude, latitude objects.
// e.g. [ [123.33, 34.44], [100.39, 78.23], etc. ]
// [ {longitude: 123.33, latitude, 34.44}, {longitude: 100.39, latitude: 78.23}, etc. ]
+ (CLLocationCoordinate2D)processLocation:(id)locObj
{
  if ([locObj isKindOfClass:[NSDictionary class]]) {
    CLLocationDegrees lat = [TiUtils doubleValue:[locObj objectForKey:@"latitude"]];
    CLLocationDegrees lon = [TiUtils doubleValue:[locObj objectForKey:@"longitude"]];

    return CLLocationCoordinate2DMake(lat, lon);
  } else if ([locObj isKindOfClass:[NSArray class]]) {
    CLLocationDegrees lat = [TiUtils doubleValue:[locObj objectAtIndex:1]];
    CLLocationDegrees lon = [TiUtils doubleValue:[locObj objectAtIndex:0]];

    return CLLocationCoordinate2DMake(lat, lon);
  }

  return kCLLocationCoordinate2DInvalid;
}

@end
