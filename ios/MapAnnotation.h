//
//  MapAnnotation.h
//  Appcelerator Titanium Mobile
//
//  Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
//  Licensed under the terms of the Apache Public License
//  Please see the LICENSE included with this distribution for details.
//

#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>

@interface MapAnnotation : NSObject <MKAnnotation>

@property (nonatomic, assign) CLLocationCoordinate2D coordinate;
@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *subtitle;

@end
