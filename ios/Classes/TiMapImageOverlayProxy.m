/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMapImageOverlayProxy.h"
#import "TiUtils.h"

@implementation TiMapImageOverlay
@synthesize coordinate, boundingMapRect;

- (instancetype)initWithMidCoordinate:(CLLocationCoordinate2D)midCoordinate andMapRect:(MKMapRect)mapRect {
  self = [super init];
  if (self) {
    boundingMapRect = mapRect;
    coordinate = midCoordinate;
  }
  return self;
}
@end

@implementation TiMapImageOverlayProxy

@synthesize imageOverlayRenderer, imageOverlay;

- (void)dealloc {
  RELEASE_TO_NIL(imageOverlayRenderer);
  RELEASE_TO_NIL(imageOverlay);
  RELEASE_TO_NIL(_image);

  [super dealloc];
}

- (NSArray *)keySequence {
  return @[ @"image", @"boundsCoordinate" ];
}

- (void)_initWithProperties:(NSDictionary *)properties {
  if ([properties objectForKey:@"image"] == nil) {
    [self throwException:@"missing required image property" subreason:nil location:CODELOCATION];
  }

  if ([properties objectForKey:@"boundsCoordinate"] == nil) {
    [self throwException:@"missing required boundsCoordinate property" subreason:nil location:CODELOCATION];
  }
  [super _initWithProperties:properties];
}

#pragma mark Internal

- (NSString *)apiName {
  return @"Ti.Map.ImageOverlay";
}

- (TiMapImageOverlayRenderer *)imageOverlayRenderer {
  if (imageOverlayRenderer == nil) {
    imageOverlay = [[TiMapImageOverlay alloc] initWithMidCoordinate:_midCoordinate andMapRect:_mapRect];
    imageOverlayRenderer = [[[TiMapImageOverlayRenderer alloc] initWithOverlay:imageOverlay overlayImage:_image] retain];
  }

  return imageOverlayRenderer;
}

#pragma mark Public APIs

- (void)setImage:(id)value {
  if (!value) {
    [self throwException:@"Missing required \"image\" property." subreason:nil location:CODELOCATION];
  }
  _image = [[TiUtils image:value proxy:self] retain];
  [self replaceValue:value forKey:@"image" notification:NO];
}

- (void)setBoundsCoordinate:(id)value {
  if (!value) {
    [self throwException:@"Missing required \"boundsCoordinate\" property." subreason:nil location:CODELOCATION];
  }

  ENSURE_DICT(value);
  ENSURE_ARG_COUNT(value, 2)
  NSDictionary *topLeftDictionary = [value objectForKey:@"topLeft"];
  NSDictionary *bottomRightDictionary = [value objectForKey:@"bottomRight"];

  CLLocationDegrees topLeftLatitude = [TiUtils doubleValue:[topLeftDictionary objectForKey:@"latitude"]];
  CLLocationDegrees topLeftLongitude = [TiUtils doubleValue:[topLeftDictionary objectForKey:@"longitude"]];
  CLLocationDegrees bottomRightLatitude = [TiUtils doubleValue:[bottomRightDictionary objectForKey:@"latitude"]];
  CLLocationDegrees bottomRightLongitude = [TiUtils doubleValue:[bottomRightDictionary objectForKey:@"longitude"]];
  _midCoordinate = CLLocationCoordinate2DMake((topLeftLatitude + bottomRightLatitude) / 2, (topLeftLongitude + bottomRightLongitude) / 2);

  MKMapPoint topLeftPoint = MKMapPointForCoordinate(CLLocationCoordinate2DMake(topLeftLatitude, topLeftLongitude));
  MKMapPoint bottomRightPoint = MKMapPointForCoordinate(CLLocationCoordinate2DMake(bottomRightLatitude, bottomRightLongitude));

  _mapRect = MKMapRectMake(topLeftPoint.x, topLeftPoint.y, fabs(topLeftPoint.x - bottomRightPoint.x), fabs(topLeftPoint.y - bottomRightPoint.y));

  [self replaceValue:value forKey:@"boundsCoordinate" notification:NO];
}

@end
