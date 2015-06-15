var Map = require('ti.map');

var win = Ti.UI.createWindow();
    mapView = Map.createView();

win.add(mapView);
win.open();