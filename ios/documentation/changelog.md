# Change Log
<pre>
v2.5.0  Add iOS9 mapTypes 'HYBRID_FLYOVER_TYPE' and 'SATELLITE_FLYOVER_TYPE'. [MOD-2152]
v2.4.1  Fixed an issue where pins have not been draggable anymore. [MOD-2131]
v2.4.0  iOS9: Upgrade map module to support bitcode. [TIMOB-19385]
v2.3.2  Fixed map crash with polygons when not setting mapType. [TIMOB-19102]
v2.3.1  Add drawing support. Includes polygons, polylines, and circles. [TIMOB-15410]
        Fixes longclick event on iOS. [Github #41]
v2.2.2  Fixed map annotations showing undeclared buttons in iOS7 [TIMOB-17953]

v2.2.1  Fixed map draggable map pins [TIMOB-18510]

v2.2.0  Updated to build for 64-bit [TIMOB-17928]
        Adding architectures to manifest [TIMOB-18065]

v2.0.6  Fixed map not responding to touch after animating camera [TIMOB-17749]

v2.0.5  Fixed exception when setting "centerCoordinate" on camera [TIMOB-17659]

v2.0.4  Fixed "userLocation" permissions for iOS 8 [TIMOB-17665]
        Bumping minsdk to 3.4.0 [TIMODOPEN-437]

v2.0.2  Fixed ignoring userLocation property during view creation [TIMOB-12733]

v2.0.1  Fixed annotation not showing leftButton rightButton [TC-3524]

v2.0.0  Fixed methods deprecated in iOS 7 [MOD-1521]
        Add Support for iOS7 MapCamera [MOD-1523]
        Expose new iOS7 properties and methods of MapView [MOD-1522]
        Fixed map view with percentage values become grayed when rotating the screen [MOD-1613]

v1.0.0  Moved out of the Titanium SDK to a standalone module [MOD-1514]
