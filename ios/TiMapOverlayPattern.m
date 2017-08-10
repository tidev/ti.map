//
//  TiMapOverlayPattern.m
//  map
//
//  Created by Hans Knöchel on 04.07.17.
//

#import "TiMapOverlayPattern.h"

@implementation TiMapOverlayPattern

- (instancetype)initWithPatternType:(TiMapOverlyPatternType)patternType andGapLength:(NSInteger)gapLength dashLength:(NSInteger)dashLength
{
    if (self = [super init]) {
        _type = patternType;
        _dashLength = dashLength;
        _gapLength = gapLength;
    }
    
    return self;
}
    
@end
