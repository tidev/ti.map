# Change Log
<pre>
v2.1.3    Fixed memory leak when removing map instance from window [TIMOB-14772]
          Added longClick event support [TIMOB-13989]
          Added support for showInfoWindow property for annotations [TIMOB-12787]
          
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