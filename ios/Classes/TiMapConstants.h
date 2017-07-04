/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#ifndef TiMapConstants_h
#define TiMapConstants_h

typedef NS_ENUM(NSUInteger, TiMapAnnotationPinColor) {
    // For parity with iOS < 9
    TiMapAnnotationPinColorRed = 0,
    TiMapAnnotationPinColorGreen,
    TiMapAnnotationPinColorPurple,
    
    // For parity with Android
    TiMapAnnotationPinColorAzure,
    TiMapAnnotationPinColorBlue,
    TiMapAnnotationPinColorCyan,
    TiMapAnnotationPinColorMagenta,
    TiMapAnnotationPinColorOrange,
    TiMapAnnotationPinColorRose,
    TiMapAnnotationPinColorViolet,
    TiMapAnnotationPinColorYellow
};

typedef NS_ENUM(NSUInteger, TiMapOverlyPatternType) {
    TiMapOverlyPatternTypeDashed = 0,
    TiMapOverlyPatternTypeDotted,
    TiMapOverlyPatternTypeCustom // Unused
};

#endif /* TiMapConstants_h */
