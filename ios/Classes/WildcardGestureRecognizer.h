//
//  WildcardGestureRecognizer.h
//
//

#ifndef map_WildcardGestureRecognizer_h
#define map_WildcardGestureRecognizer_h


#import <Foundation/Foundation.h>
#import <UIKit/UIEvent.h>
#import <UIKit/UIGestureRecognizer.h>

typedef void (^TouchesEventBlock)(NSSet * touches, UIEvent * event);

@interface WildcardGestureRecognizer : UIGestureRecognizer {
    TouchesEventBlock touchesBeganCallback;
}
@property(copy) TouchesEventBlock touchesBeganCallback;


@end


#endif
