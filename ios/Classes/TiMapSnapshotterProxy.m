/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMapSnapshotterProxy.h"
#import "TiMapView.h"
#import "TiMapViewProxy.h"
#import <TitaniumKit/TiBlob.h>

@implementation TiMapSnapshotterProxy

CLLocationDegrees latitudeCoord;
CLLocationDegrees longitudeCoord;
bool showPin = NO;

- (NSString *)apiName
{
  return @"Ti.Map.Snapshotter";
}

- (void)dealloc
{
  RELEASE_TO_NIL(options);
  [super dealloc];
}

- (MKCoordinateRegion)regionFromDict:(NSDictionary *)dict
{
  CLLocationDegrees latitudeDelta = [TiUtils floatValue:@"latitudeDelta" properties:dict];
  CLLocationDegrees longitudeDelta = [TiUtils floatValue:@"longitudeDelta" properties:dict];
    

  CLLocationDegrees latitude = [TiUtils floatValue:@"latitude" properties:dict];
  CLLocationDegrees longitude = [TiUtils floatValue:@"longitude" properties:dict];

    latitudeCoord = latitude;
    longitudeCoord = longitude;
    
  return MKCoordinateRegionMake(CLLocationCoordinate2DMake(latitude, longitude), MKCoordinateSpanMake(latitudeDelta, longitudeDelta));
}

- (MKMapSnapshotOptions *)options
{
  if (!options) {
    options = [[MKMapSnapshotOptions alloc] init];
    [options setScale:[[UIScreen mainScreen] scale]];
    CGRect bounds = [[UIScreen mainScreen] bounds];
    [options setSize:bounds.size];
  }
  return options;
}

- (void)setRegion:(id)value
{
  ENSURE_TYPE(value, NSDictionary);
  [[self options] setRegion:[self regionFromDict:value]];
}

- (void)setMapType:(id)value
{
  ENSURE_TYPE(value, NSNumber);
  [[self options] setMapType:[TiUtils intValue:value]];
}

- (void)setShowPin:(id)value
{
  ENSURE_TYPE(value, NSNumber);
  showPin = [TiUtils boolValue:value];
}


- (void)setShowsBuildings:(id)value
{
  ENSURE_TYPE(value, NSNumber);
  [[self options] setShowsBuildings:[TiUtils boolValue:value]];
}

- (void)setShowsPointsOfInterest:(id)value
{
  ENSURE_TYPE(value, NSNumber);
  [[self options] setShowsPointsOfInterest:[TiUtils boolValue:value]];
}

- (void)setSize:(id)args
{
  ENSURE_SINGLE_ARG(args, NSDictionary);
  float width = [TiUtils floatValue:[args objectForKey:@"width"]];
  float height = [TiUtils floatValue:[args objectForKey:@"height"]];

  [[self options] setSize:CGSizeMake(width, height)];
}

- (void)takeSnapshot:(id)args
{
  ENSURE_SINGLE_ARG(args, NSDictionary);

  KrollCallback *successCallback = [args objectForKey:@"success"];
  KrollCallback *ErrorCallback = [args objectForKey:@"error"];

  MKMapSnapshotter *snapshotter = [[MKMapSnapshotter alloc] initWithOptions:[self options]];
  [snapshotter startWithCompletionHandler:^(MKMapSnapshot *snapshot, NSError *error) {
    if (error) {
      [self _fireEventToListener:@"blob" withObject:[TiUtils stringValue:error] listener:ErrorCallback thisObject:nil];
      return;
    }
      TiBlob *blob;
      
      if (showPin == YES){
        CGRect finalImageRect = CGRectMake(0, 0, snapshot.image.size.width, snapshot.image.size.height);

        MKAnnotationView *pin = [[MKPinAnnotationView alloc] initWithAnnotation:nil reuseIdentifier:@""];
        UIImage *pinImage = pin.image;

        // ok, let's start to create our final image
        UIGraphicsBeginImageContextWithOptions(snapshot.image.size, YES, snapshot.image.scale);

        // first, draw the image from the snapshotter
        [snapshot.image drawAtPoint:CGPointMake(0, 0)];

        CGPoint point = [snapshot pointForCoordinate:CLLocationCoordinate2DMake(latitudeCoord, longitudeCoord)];
        
        if (CGRectContainsPoint(finalImageRect, point)) // this is too conservative, but you get the idea
        {
            CGPoint pinCenterOffset = pin.centerOffset;
            point.x -= pin.bounds.size.width / 2.0;
            point.y -= pin.bounds.size.height / 2.0;
            point.x += pinCenterOffset.x;
            point.y += pinCenterOffset.y;

            [pinImage drawAtPoint:point];
        }
        UIImage *finalImage = UIGraphicsGetImageFromCurrentImageContext();
        UIGraphicsEndImageContext();
          
        blob = [[TiBlob alloc] initWithImage:finalImage];
      }
      else{
        blob = [[TiBlob alloc] initWithImage:snapshot.image];
      }
      
    [blob setMimeType:@"image/png" type:TiBlobTypeImage];

    NSDictionary *event = [NSDictionary dictionaryWithObject:blob forKey:@"image"];
    [self _fireEventToListener:@"blob" withObject:event listener:successCallback thisObject:nil];
  }];
}

@end
