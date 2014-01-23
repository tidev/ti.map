# Change Log
<pre>
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