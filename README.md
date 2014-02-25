ti.map
======

This is a fork of the original ti.map modules which add Shape support for maps on Android.

Supported shapes are

- Polygon

- Polyline

- Circle



~~~

var win1 = Titanium.UI.createWindow({  
    title:'Tab 1',
    backgroundColor:'#fff'
});

var MapModule = require('ti.map');
var map = MapModule.createView({
    mapType: MapModule.SATELLITE_TYPE,
    region: {
        latitude: 46.50,
        longitude: 11.75, 
        latitudeDelta: 5, 
        longitudeDelta: 5 
    }, 
    
    width: 'auto',
    height: 'auto'
    
});


win1.addEventListener('open', function() {

    win1.add(map);
	
    // this data is fictional, use your own or see google maps api examples
    var points = [
	{latitude: 45.10, longitude: 11.10},
	{latitude: 45.20, longitude: 11.20},
	{latitude: 45.30, longitude: 11.30},
    ];

    // holes are array of array of LatLng
    var holes = [
	[
	  {latitude: 45.15, longitude: 11.8},
	  {latitude: 45.25, longitude: 11.16},
	  {latitude: 45.27, longitude: 11.28},
	]
    ];
    
    var polyline = [
	{latitude: 46.15, longitude: 12.8},
	{latitude: 46.25, longitude: 12.16},
	{latitude: 46.27, longitude: 12.28},
   ];

    var polygon = MapModule.createPolygon({
        points: points,
        holes: holes,
        strokeColor: "blue",
        strokeWidth: 2.0,
        fillColor: "red",
    });
    map.addPolygon(polygon);
    
    var polyline = MapModule.createPolyline({
        points: holes[0],
        strokeColor: "green",
        strokeWidth: 2.0,
        fillColor: "yellow",
    });
    map.addPolline(polyline);
    
});

win1.open();

~~~


