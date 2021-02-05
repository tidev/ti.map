/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-Present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiMapAnnotationProxy.h"
#import "ImageLoader.h"
#import "TiButtonUtil.h"
#import "TiMapConstants.h"
#import "TiMapView.h"
#import "TiMapViewProxy.h"
#import "TiUIiOSPreviewContextProxy.h"
#import "TiUtils.h"
#import "TiViewProxy.h"
#import "UIColor+AndroidHueParity.h"

@implementation TiMapAnnotationProxy

@synthesize delegate;
@synthesize needsRefreshingWithSelection;
@synthesize placed;
@synthesize offset;
@synthesize controllerPreviewing;

#define LEFT_BUTTON 1
#define RIGHT_BUTTON 2

#pragma mark Internal

- (void)_configure
{
  static int mapTags = 0;
  tag = mapTags++;
  needsRefreshingWithSelection = YES;
  offset = CGPointZero;
  [super _configure];
}

- (NSString *)apiName
{
  return @"Ti.Map.Annotation";
}

- (void)dealloc
{
  RELEASE_TO_NIL(controllerPreviewing);
  [super dealloc];
}

- (NSMutableDictionary *)langConversionTable
{
  return [NSMutableDictionary dictionaryWithObjectsAndKeys:@"title", @"titleid", @"subtitle", @"subtitleid", nil];
}

- (UIView *)makeButton:(id)button tag:(int)buttonTag
{
  UIView *button_view = nil;
  if ([button isKindOfClass:[NSNumber class]]) {
    // this is button type constant
    int type = [TiUtils intValue:button];
    button_view = [TiButtonUtil buttonWithType:type];
  } else {
    UIImage *image = [[ImageLoader sharedLoader] loadImmediateImage:[TiUtils toURL:button proxy:self]];
    if (image != nil) {
      CGSize size = [image size];
      UIButton *bview = [UIButton buttonWithType:UIButtonTypeCustom];
      [TiUtils setView:bview positionRect:CGRectMake(0, 0, size.width, size.height)];
      bview.backgroundColor = [UIColor clearColor];
      [bview setImage:image forState:UIControlStateNormal];
      button_view = bview;
    }
  }
  if (button_view != nil) {
    button_view.tag = buttonTag;
  }
  return button_view;
}

- (void)refreshAfterDelay
{
  dispatch_after(dispatch_time(DISPATCH_TIME_NOW, 0.1 * NSEC_PER_SEC), dispatch_get_main_queue(), ^(void) {
    [self refreshIfNeeded];
  });
}

- (void)setNeedsRefreshingWithSelection:(BOOL)shouldReselect
{
  if (delegate == nil) {
    return; //Nobody to refresh!
  }
  @synchronized(self) {
    BOOL invokeMethod = !needsRefreshing;
    needsRefreshing = YES;
    needsRefreshingWithSelection |= shouldReselect;

    if (invokeMethod) {
      TiThreadPerformOnMainThread(
          ^{
            [self refreshAfterDelay];
          },
          NO);
    }
  }
}

- (void)refreshCoordinateChanges:(void (^)())updateValueCallBack
{
  if (delegate != nil && [delegate viewAttached]) {
    [(TiMapView *)[delegate view] refreshCoordinateChanges:self afterRemove:updateValueCallBack];
  } else {
    updateValueCallBack();
  }
}

- (void)refreshIfNeeded
{
  @synchronized(self) {
    if (!needsRefreshing) {
      return; //Already done.
    }
    if (delegate != nil && [delegate viewAttached]) {
      [(TiMapView *)[delegate view] refreshAnnotation:self readd:needsRefreshingWithSelection];
    }
    needsRefreshing = NO;
    needsRefreshingWithSelection = NO;
  }
}

#pragma mark Public APIs

- (CLLocationCoordinate2D)coordinate
{
  CLLocationCoordinate2D result;
  result.latitude = [TiUtils doubleValue:[self valueForUndefinedKey:@"latitude"]];
  result.longitude = [TiUtils doubleValue:[self valueForUndefinedKey:@"longitude"]];
  return result;
}

- (void)setCoordinate:(CLLocationCoordinate2D)coordinate
{
  [self setValue:[NSNumber numberWithDouble:coordinate.latitude] forUndefinedKey:@"latitude"];
  [self setValue:[NSNumber numberWithDouble:coordinate.longitude] forUndefinedKey:@"longitude"];
}

- (void)setLatitude:(id)latitude
{
  double curValue = [TiUtils doubleValue:[self valueForUndefinedKey:@"latitude"]];
  double newValue = [TiUtils doubleValue:latitude];
  if (newValue != curValue) {
    [self refreshCoordinateChanges:^{
      [self replaceValue:latitude forKey:@"latitude" notification:NO];
    }];
  }
}

- (void)setLongitude:(id)longitude
{
  double curValue = [TiUtils doubleValue:[self valueForUndefinedKey:@"longitude"]];
  double newValue = [TiUtils doubleValue:longitude];
  if (newValue != curValue) {
    [self refreshCoordinateChanges:^{
      [self replaceValue:longitude forKey:@"longitude" notification:NO];
    }];
  }
}

// Title and subtitle for use by selection UI.
- (NSString *)title
{
  return [self valueForUndefinedKey:@"title"];
}

- (void)setTitle:(id)title
{
  title = [TiUtils replaceString:[TiUtils stringValue:title]
                      characters:[NSCharacterSet newlineCharacterSet]
                      withString:@" "];
  //The label will strip out these newlines anyways (Technically, replace them with spaces)

  id current = [self valueForUndefinedKey:@"title"];
  [self replaceValue:title forKey:@"title" notification:NO];
  if (![title isEqualToString:current]) {
    [self setNeedsRefreshingWithSelection:NO];
  }
}

- (NSString *)subtitle
{
  return [self valueForUndefinedKey:@"subtitle"];
}

- (void)setSubtitle:(id)subtitle
{
  subtitle = [TiUtils replaceString:[TiUtils stringValue:subtitle]
                         characters:[NSCharacterSet newlineCharacterSet]
                         withString:@" "];

  // The label will strip out these newlines anyways (Technically, replace them with spaces)

  id current = [self valueForUndefinedKey:@"subtitle"];
  [self replaceValue:subtitle forKey:@"subtitle" notification:NO];

  if (![subtitle isEqualToString:current]) {
    [self setNeedsRefreshingWithSelection:NO];
  }
}

- (void)setHidden:(id)value
{
  id current = [self valueForUndefinedKey:@"hidden"];
  [self replaceValue:value forKey:@"hidden" notification:NO];

  if ([current isEqual:value] == NO) {
    [self setNeedsRefreshingWithSelection:YES];
  }
}

- (id)hidden
{
  return NUMBOOL([TiUtils boolValue:[self valueForUndefinedKey:@"hidden"] def:NO]);
}

- (id)pincolor
{
  return NUMINT([self valueForUndefinedKey:@"pincolor"]);
}

- (void)setPincolor:(id)color
{
  id current = [self valueForUndefinedKey:@"pincolor"];
  [self replaceValue:color forKey:@"pincolor" notification:NO];
  if (current != color) {
    [self setNeedsRefreshingWithSelection:YES];
  }
}

// Mapping both string-colors, color constant and native colors to a pin color
// This is overcomplicated to maintain iOS < 9 compatibility. Remove this when
// we have a minimum iOS verion of 9.0+
- (id)nativePinColor
{
  id current = [self valueForUndefinedKey:@"pincolor"];

  if ([current isKindOfClass:[NSString class]]) {
    return [[TiUtils colorValue:current] color];
  }

  switch ([TiUtils intValue:current def:TiMapAnnotationPinColorRed]) {
  case TiMapAnnotationPinColorGreen: {
#ifdef __IPHONE_9_0
    return [MKPinAnnotationView greenPinColor];
#else
    return MKPinAnnotationColorGreen;
#endif
  }
  case TiMapAnnotationPinColorPurple: {
    return [MKPinAnnotationView purplePinColor];
  }
  case TiMapAnnotationPinColorBlue:
    return [UIColor blueColor];
  case TiMapAnnotationPinColorCyan:
    return [UIColor cyanColor];
  case TiMapAnnotationPinColorMagenta:
    return [UIColor magentaColor];
  case TiMapAnnotationPinColorOrange:
    return [UIColor orangeColor];
  case TiMapAnnotationPinColorYellow:
    return [UIColor yellowColor];

  // UIColor extensions
  case TiMapAnnotationPinColorAzure:
    return [UIColor azureColor];
  case TiMapAnnotationPinColorRose:
    return [UIColor roseColor];
  case TiMapAnnotationPinColorViolet:
    return [UIColor violetColor];
  case TiMapAnnotationPinColorRed:
  default: {
    return [MKPinAnnotationView redPinColor];
  }
  }
}

- (BOOL)animatesDrop
{
  return [TiUtils boolValue:[self valueForUndefinedKey:@"animate"]];
}

- (UIView *)leftViewAccessory
{
  TiViewProxy *viewProxy = [self valueForUndefinedKey:@"leftView"];
  if (viewProxy != nil && [viewProxy isKindOfClass:[TiViewProxy class]]) {
    return [viewProxy view];
  } else {
    id button = [self valueForUndefinedKey:@"leftButton"];
    if (button != nil) {
      return [self makeButton:button tag:LEFT_BUTTON];
    }
  }
  return nil;
}

- (UIView *)rightViewAccessory
{
  TiViewProxy *viewProxy = [self valueForUndefinedKey:@"rightView"];
  if (viewProxy != nil && [viewProxy isKindOfClass:[TiViewProxy class]]) {
    return [viewProxy view];
  } else {
    id button = [self valueForUndefinedKey:@"rightButton"];
    if (button != nil) {
      return [self makeButton:button tag:RIGHT_BUTTON];
    }
  }
  return nil;
}

- (void)setLeftButton:(id)button
{
  id current = [self valueForUndefinedKey:@"leftButton"];
  [self replaceValue:button forKey:@"leftButton" notification:NO];
  if (current != button) {
    [self setNeedsRefreshingWithSelection:YES];
  }
}

- (void)setRightButton:(id)button
{
  id current = [self valueForUndefinedKey:@"rightButton"];
  [self replaceValue:button forKey:@"rightButton" notification:NO];
  if (current != button) {
    [self setNeedsRefreshingWithSelection:YES];
  }
}

- (void)setRightView:(id)rightview
{
  id current = [self valueForUndefinedKey:@"rightView"];
  [self replaceValue:rightview forKey:@"rightView" notification:NO];
  if (current != rightview) {
    [self setNeedsRefreshingWithSelection:YES];
  }
}

- (void)setLeftView:(id)leftview
{
  id current = [self valueForUndefinedKey:@"leftView"];
  [self replaceValue:leftview forKey:@"leftView" notification:NO];
  if (current != leftview) {
    [self setNeedsRefreshingWithSelection:YES];
  }
}

- (void)setPreviewContext:(id)previewContext
{
  Class TiUIiOSPreviewContextProxy = NSClassFromString(@"TiUIiOSPreviewContextProxy");

  if (!TiUIiOSPreviewContextProxy || ![previewContext respondsToSelector:@selector(preview)]) {
    return;
  }

  ENSURE_TYPE(previewContext, TiUIiOSPreviewContextProxy);

  if ([TiUtils forceTouchSupported] == NO) {
    NSLog(@"[WARN] 3DTouch is not available on this device.");
    return;
  }

  if ([previewContext performSelector:@selector(preview)] == nil) {
    NSLog(@"[ERROR] The 'preview' property of your preview context is not existing or invalid. Please provide a valid view to use peek and pop.");
    RELEASE_TO_NIL(previewContext);
    return;
  }

  id current = [self valueForUndefinedKey:@"previewContext"];
  [self replaceValue:previewContext forKey:@"previewContext" notification:NO];

  if (current != previewContext) {
    [self setNeedsRefreshingWithSelection:YES];
  }
}

- (void)setImage:(id)image
{
  id current = [self valueForUndefinedKey:@"image"];
  [self replaceValue:image forKey:@"image" notification:NO];
  if ([current isEqual:image] == NO) {
    [self setNeedsRefreshingWithSelection:YES];
  }
}

- (void)setShowAsMarker:(id)marker
{
  id current = [self valueForUndefinedKey:@"showAsMarker"];
  [self replaceValue:marker forKey:@"showAsMarker" notification:NO];
  if ([current isEqual:marker] == NO) {
    [self setNeedsRefreshingWithSelection:YES];
  }
}

- (void)setMarkerGlyphText:(id)markerGlyphText
{
  id current = [self valueForUndefinedKey:@"markerGlyphText"];
  [self replaceValue:markerGlyphText forKey:@"markerGlyphText" notification:NO];
  if (![current isEqualToString:markerGlyphText]) {
    [self setNeedsRefreshingWithSelection:YES];
  }
}

- (void)setMarkerGlyphColor:(id)markerGlyphColor
{
  id current = [self valueForUndefinedKey:@"markerGlyphColor"];
  [self replaceValue:markerGlyphColor forKey:@"markerGlyphColor" notification:NO];
  if ([current isEqual:markerGlyphColor] == NO) {
    [self setNeedsRefreshingWithSelection:YES];
  }
}

- (void)setMarkerColor:(id)markerColor
{
  id current = [self valueForUndefinedKey:@"markerColor"];
  [self replaceValue:markerColor forKey:@"markerColor" notification:NO];
  if ([current isEqual:markerColor] == NO) {
    [self setNeedsRefreshingWithSelection:YES];
  }
}

- (void)setMarkerGlyphImage:(id)markerGlyphImage
{
  id current = [self valueForUndefinedKey:@"markerGlyphImage"];
  [self replaceValue:markerGlyphImage forKey:@"markerGlyphImage" notification:NO];
  if ([current isEqual:markerGlyphImage] == NO) {
    [self setNeedsRefreshingWithSelection:YES];
  }
}

- (void)setMarkerSelectedGlyphImage:(id)markerSelectedGlyphImage
{
  id current = [self valueForUndefinedKey:@"markerSelectedGlyphImage"];
  [self replaceValue:markerSelectedGlyphImage forKey:@"markerSelectedGlyphImage" notification:NO];
  if ([current isEqual:markerSelectedGlyphImage] == NO) {
    [self setNeedsRefreshingWithSelection:YES];
  }
}

- (void)setMarkerAnimatesWhenAdded:(id)animates
{
  id current = [self valueForUndefinedKey:@"markerAnimatesWhenAdded"];
  [self replaceValue:animates forKey:@"markerAnimatesWhenAdded" notification:NO];
  if ([current isEqual:animates] == NO) {
    [self setNeedsRefreshingWithSelection:YES];
  }
}

- (void)setMarkerTitleVisibility:(id)titleVisibility
{
  id current = [self valueForUndefinedKey:@"markerTitleVisibility"];
  [self replaceValue:titleVisibility forKey:@"markerTitleVisibility" notification:NO];
  if ([current isEqual:titleVisibility] == NO) {
    [self setNeedsRefreshingWithSelection:YES];
  }
}

- (void)setMarkerSubtitleVisibility:(id)subtitleVisibility
{
  id current = [self valueForUndefinedKey:@"markerSubtitleVisibility"];
  [self replaceValue:subtitleVisibility forKey:@"markerSubtitleVisibility" notification:NO];
  if ([current isEqual:subtitleVisibility] == NO) {
    [self setNeedsRefreshingWithSelection:YES];
  }
}

- (void)setCollisionMode:(id)collisionMode
{
  id current = [self valueForUndefinedKey:@"collisionMode"];
  [self replaceValue:collisionMode forKey:@"collisionMode" notification:NO];
  if ([current isEqual:collisionMode] == NO) {
    [self setNeedsRefreshingWithSelection:YES];
  }
}

- (void)setAnnotationDisplayPriority:(id)displayPriority
{
  id current = [self valueForUndefinedKey:@"annotationDisplayPriority"];
  [self replaceValue:displayPriority forKey:@"annotationDisplayPriority" notification:NO];
  if ([current isEqual:displayPriority] == NO) {
    [self setNeedsRefreshingWithSelection:YES];
  }
}

- (void)setClusterIdentifier:(id)clusterIdentifier
{
  id current = [self valueForUndefinedKey:@"clusterIdentifier"];
  [self replaceValue:clusterIdentifier forKey:@"clusterIdentifier" notification:NO];
  if (![current isEqualToString:clusterIdentifier]) {
    [self setNeedsRefreshingWithSelection:YES];
  }
}

- (void)setCustomView:(id)customView
{
  id current = [self valueForUndefinedKey:@"customView"];
  [self replaceValue:customView forKey:@"customView" notification:NO];
  if ([current isEqual:customView] == NO) {
    [current setProxyObserver:nil];
    [self forgetProxy:current];
    [self rememberProxy:customView];
    [customView setProxyObserver:self];
    [self setNeedsRefreshingWithSelection:YES];
  }
}

- (void)proxyDidRelayout:(id)sender
{
  id current = [self valueForUndefinedKey:@"customView"];
  if (([current isEqual:sender] == YES) && (self.placed)) {
    [self setNeedsRefreshingWithSelection:YES];
  }
}

- (void)setCenterOffset:(id)centeroffset
{
  [self replaceValue:centeroffset forKey:@"centerOffset" notification:NO];
  CGPoint newVal = [TiUtils pointValue:centeroffset];
  if (!CGPointEqualToPoint(newVal, offset)) {
    offset = newVal;
    [self setNeedsRefreshingWithSelection:YES];
  }
}

- (int)tag
{
  return tag;
}
- (void)rotate:(id)arg
{
  CGFloat getAngle = [[arg objectAtIndex:0] floatValue];
  [UIView animateWithDuration:1
                   animations:^{
                     MKAnnotationView *annotationView = [[(TiMapView *)[delegate view] map] viewForAnnotation:self];
                     annotationView.transform = CGAffineTransformMakeRotation(getAngle);
                   }];
}

- (void)animate:(id)arg
{
  ENSURE_SINGLE_ARG(arg, NSArray);
  TiMapAnnotationProxy *newAnnotation = self;
  CLLocationCoordinate2D newLocation;
  newLocation.latitude = [[arg objectAtIndex:0] floatValue];
  newLocation.longitude = [[arg objectAtIndex:1] floatValue];
  [(TiMapView *)[delegate view] animateAnnotation:newAnnotation withLocation:newLocation];
}

- (UIView *)view
{
  return [delegate viewForAnnotationProxy:self];
}
@end
