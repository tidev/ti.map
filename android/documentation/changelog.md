# Change Log
<pre>
v3.1.0    Android: Add style property [MOD-2311]
v3.0.2    Fix addPolyline() handler
v3.0.1    Update Google Play Services to v9.6.1 (revision 33)
v2.3.10   Android null check on map.clear()
v2.3.9    Android: Annotation.pincolor can't be changed if annotation added to mapView. [TIMOB-20620]
v2.3.8    Support annotation's image property with Ti.Blob [MOD-2205]
v2.3.7    Android: Provide the method setMapToolbarEnabled [MOD-2189]
v2.3.6    Add Street View Panorama [TIMOB-19751]
v2.3.5    Refactor module to use getMapAsync [TIMOB-19726]
v2.3.4    Recompiled map with NDK r10e [TIMOB-19681]
v2.3.3    Update toImage() signature [TIMOB-19314]
          Strip down Google Play Services library to include only maps components [TIMOB-18082]

v2.3.2    Update Google Play Services library and assets [TIMOB-18988]

v2.3.1    Add drawing support. Includes polygons, polylines, and circles. [TIMOB-15410]
          Ensure region property is set after mapMove on android to match behavior of ios. [TIMOB-17857]

v2.3.0    Fixed a couple crashing issues with map, added API name. [TIMOB-18244]

v2.2.5    Fixed the example. [MOD-2073]

v2.2.4    Added support for density-specific images for leftButton and rightButton properties. [MOD-1771]

v2.2.3    Update signature of addAnnotations method.[TIMOB-17988]
          Adding architectures to manifest [TIMOB-18065]

v2.2.2    Update Google Play Services library and assets. [TIMODOPEN-451]

v2.2.1    Update Google Play Services library and assets. [TIMOB-17884]

v2.2.0    Bumping minsdk to 3.3.0 [TIMOB-17048]

v2.1.6    Handle null values for attributes. [TIMOB-16562]

v2.1.5    Update Google Play Services library and its assets. [TIMOB-16510]

v2.1.4    Implement tilt, bearing, zoom, compassEnabled properties [TIMOB-16180].
          Implement maxZoomLevel, minZoomLevel, update Google Play Services SDK [TIMOB-16180].
          Implement support for taking snapshots of the map [TIMOB-16180]

v2.1.3    Fixed memory leak when removing map instance from window [TIMOB-14772].
          Added longClick event support [TIMOB-13989].
          Added support for showInfoWindow property for annotations [TIMOB-12787].
          Added a workaround for: https://code.google.com/p/android/issues/detail?id=11676 [TIMOB-15565].
          Added support for zOrderOnTop [TIMOB-13628].
          Updated Google Play Services SDK. [TIMOB-15591].
          Updated manifest permissions in timodule.xml [TIMOB-14899].

v2.1.2    Clicking on an annotation should center it in map view [TIMOB-13778].
          Fixed a bug where removing an annotation using its title crashed the app [TIMOB-14502].
          Updated Google Play Services SDK.
          Fixed a bug where add/remove annotations didn't modify map's 'annotations' property correctly [TIMOB-14761].

v2.1.1    Added isGooglePlayServicesAvailable [TIMOB-14075].
          Added the userLocationButton property [TIMOB-13003].

v2.1.0    Added the pinchangedragstate event to the Map View.
          Supported custom views for the pin.
          Added the enableZoomControls property to enable/disable zoom controls.
          Added the support of leftButton, leftView, rightButton and leftView for annotations.
          Added the setLocation and zoom methods.
          Updated the minsdk version to 3.1.0.

v2.0.0    Initial Release
