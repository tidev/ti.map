//
//  MapAnnotation.h
//  TestAnnotationMoving
//
//  Created by Nishith Shah on 14/11/16.
//  Copyright © 2016 Ramkrishna Sharma. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>

@interface MapAnnotation : NSObject <MKAnnotation>

@property (nonatomic, assign) CLLocationCoordinate2D coordinate;
@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *subtitle;
	
@end
