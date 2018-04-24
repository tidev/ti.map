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

- (instancetype)initWithMidCoordinate:(CLLocationCoordinate2D)midCoordinate andMapRect:(MKMapRect)mapRect
{
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

- (void)dealloc
{
  RELEASE_TO_NIL(imageOverlayRenderer);
  RELEASE_TO_NIL(imageOverlay);
  RELEASE_TO_NIL(image);

  [super dealloc];
}

- (NSArray *)keySequence
{
  return @[ @"image", @"boundsCoordinate" ];
}

- (void)_initWithProperties:(NSDictionary *)properties
{
  if ([properties objectForKey:@"image"] == nil) {
    [self throwException:@"missing required image property" subreason:nil location:CODELOCATION];
  }

  if ([properties objectForKey:@"boundsCoordinate"] == nil) {
    [self throwException:@"missing required boundsCoordinate property" subreason:nil location:CODELOCATION];
  }
  [super _initWithProperties:properties];
}

#pragma mark Internal

- (NSString *)apiName
{
  return @"Ti.Map.ImageOverlay";
}

- (TiMapImageOverlayRenderer *)imageOverlayRenderer
{
  if (imageOverlayRenderer == nil) {
    imageOverlay = [[TiMapImageOverlay alloc] initWithMidCoordinate:midCoordinate andMapRect:mapRect];
    imageOverlayRenderer = [[[TiMapImageOverlayRenderer alloc] initWithOverlay:imageOverlay overlayImage:image] retain];
  }

  return imageOverlayRenderer;
}

#pragma mark Public APIs

- (void)setImage:(id)value
{
  if (!value) {
    [self throwException:@"missing required image data" subreason:nil location:CODELOCATION];
  }
  image = [[TiUtils image:value proxy:self] retain];
  [self replaceValue:value forKey:@"image" notification:NO];
}

- (void)setBoundsCoordinate:(id)value
{
  if (!value) {
    [self throwException:@"missing required bounds coordinate data" subreason:nil location:CODELOCATION];
  }

  ENSURE_DICT(value);
  ENSURE_ARG_COUNT(value, 2)
  NSDictionary *topLeftDict = [value objectForKey:@"topLeft"];
  NSDictionary *bottomRightDict = [value objectForKey:@"bottomRight"];

  CLLocationDegrees topLeftLat = [TiUtils doubleValue:[topLeftDict objectForKey:@"latitude"]];
  CLLocationDegrees topLeftLong = [TiUtils doubleValue:[topLeftDict objectForKey:@"longitude"]];
  CLLocationDegrees bottomRightLat = [TiUtils doubleValue:[bottomRightDict objectForKey:@"latitude"]];
  CLLocationDegrees bottomRightLong = [TiUtils doubleValue:[bottomRightDict objectForKey:@"longitude"]];
  midCoordinate = CLLocationCoordinate2DMake((topLeftLat + bottomRightLat) / 2, (topLeftLong + bottomRightLong) / 2);

  MKMapPoint topLeft = MKMapPointForCoordinate(CLLocationCoordinate2DMake(topLeftLat, topLeftLong));
  MKMapPoint bottomRight = MKMapPointForCoordinate(CLLocationCoordinate2DMake(bottomRightLat, bottomRightLong));

  mapRect = MKMapRectMake(topLeft.x, topLeft.y, fabs(topLeft.x - bottomRight.x), fabs(topLeft.y - bottomRight.y));

  [self replaceValue:value forKey:@"boundsCoordinate" notification:NO];
}
@end
