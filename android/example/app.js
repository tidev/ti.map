
var MapModule = require('ti.map');

var code = MapModule.isGooglePlayServicesAvailable();

if (code != MapModule.SUCCESS) {
	alert ("Google Play Services is not installed/updated/available");
} else {
	var win = Ti.UI.createWindow();
	var table = Ti.UI.createTableView();
	var tableData = [];
	
	var multiMapRow = Ti.UI.createTableViewRow({title: 'Multi Map'});
	tableData.push(multiMapRow);
	multiMapRow.addEventListener('click', function(e) {
	   multiMapTest();
	});
	
	
	var annotationRow = Ti.UI.createTableViewRow({title:'AnnotationTest'});
	tableData.push(annotationRow);
	annotationRow.addEventListener('click', function(e) {
	   annotationTest();
	});
	
	
	var routeRow = Ti.UI.createTableViewRow({title:'Routes Test'});
	tableData.push(routeRow);
	routeRow.addEventListener('click', function(e) {
	   routeTest();
	});
	
	
	table.setData(tableData);
	win.add(table);
	win.open();
}




/************************************************************************************/
/************************************************************************************/
									//Multi Maps
/************************************************************************************/
function multiMapTest() {
var win = Ti.UI.createWindow({fullscreen: false});

var map1 = MapModule.createView({
	userLocation: true,
	mapType: MapModule.NORMAL_TYPE,
	animate: true,
	region: {latitude: -33.87365, longitude: 151.20689, latitudeDelta: 0.1, longitudeDelta: 0.1 }, //Sydney
	height: '50%',
	top: 0,
	left: 0,
	width: '50%'
});
var map2 = MapModule.createView({
	userLocation: true,
	mapType: MapModule.NORMAL_TYPE,
	animate: true,
	region: {latitude: -33.87365, longitude: 151.20689, latitudeDelta: 0.1, longitudeDelta: 0.1 }, //Sydney
	height: '50%',
	top: 0,
	right: 0,
	width: '50%'
});
var map3 = MapModule.createView({
	userLocation: true,
	mapType: MapModule.NORMAL_TYPE,
	animate: true,
	region: {latitude: -33.87365, longitude: 151.20689, latitudeDelta: 0.1, longitudeDelta: 0.1 }, //Sydney
	height: '50%',
	bottom: 0,
	left: 0,
	width: '50%'
});
var map4 = MapModule.createView({
	userLocation: true,
	mapType: MapModule.NORMAL_TYPE,
	animate: true,
	region: {latitude: -33.87365, longitude: 151.20689, latitudeDelta: 0.1, longitudeDelta: 0.1 }, //Sydney
	height: '50%',
	bottom: 0,
	right: 0,
	width: '50%'
});

win.add(map1);
win.add(map2);
win.add(map3);
win.add(map4);
win.open();
}


/************************************************************************************/
/************************************************************************************/
									//ANNOTATIONS
/************************************************************************************/
function annotationTest() {
var win = Ti.UI.createWindow({fullscreen: false});
var anno = MapModule.createAnnotation({latitude: -33.87365, image: 'map_pin.png', longitude: 151.20689, title: "Sydney", subtitle: "Sydney is quite chill", draggable: true});
var anno2 = MapModule.createAnnotation({latitude: -33.86365, pincolor: MapModule.ANNOTATION_BLUE, longitude: 151.21689, title: "Anno2", subtitle: "This is anno2", draggable: true});
var anno3 = MapModule.createAnnotation({latitude: -33.85365, longitude: 151.20689, title: "Anno3", subtitle: "This is anno3", draggable: false});
var anno4 = MapModule.createAnnotation({latitude: -33.86365, longitude: 151.22689, title: "Anno4", subtitle: "This is anno4", draggable: true});

Ti.API.info ("Latitude:" + anno.latitude);
Ti.API.info ("Title:" + anno.title);

var map = MapModule.createView({
	userLocation: true,
	mapType: MapModule.NORMAL_TYPE,
	animate: true,
	annotations: [anno, anno2, anno4],
	region: {latitude: -33.87365, longitude: 151.20689, latitudeDelta: 0.1, longitudeDelta: 0.1 }, //Sydney
	top: '30%'
});
Ti.API.info("userLocation: " + map.userLocation);
win.add(map);

map.addEventListener('click', function(e) {
	Ti.API.info("Latitude: " + e.latitude);
	Ti.API.info("Source: " + e.clicksource);
});
var button = Ti.UI.createButton({top: 0, left: 0, title: "Go Mt. View"});
button.addEventListener('click', function(e) {
	map.region = {latitude: 37.3689, longitude: -122.0353, latitudeDelta: 0.1, longitudeDelta: 0.1 }; //Mountain View
});

var button2 = Ti.UI.createButton({top: 0, right: 0, title: "add anno3"});
button2.addEventListener('click', function(e) {
	map.addAnnotation(anno3);
});

var button3 = Ti.UI.createButton({top: 0, title: "rm anno3"});
button3.addEventListener('click', function(e) {
	map.removeAnnotation(anno3);
});

var button4 = Ti.UI.createButton({top: '10%', title: "rm all"});
button4.addEventListener('click', function(e) {
	map.removeAllAnnotations();
});

var button5 = Ti.UI.createButton({top: '10%', left: 0, title: "remove annos"});
button5.addEventListener('click', function(e) {
	Ti.API.info(anno.getTitle());
	map.removeAnnotations(["Sydney", anno2]);
});

var button6 = Ti.UI.createButton({top: '10%', right: 0, title: "select anno2"});
button6.addEventListener('click', function(e) {
	map.selectAnnotation(anno2);
});

var button7 = Ti.UI.createButton({top: '20%', left: 0, title: "desel anno2"});
button7.addEventListener('click', function(e) {
	map.deselectAnnotation(anno2);
});

var button8 = Ti.UI.createButton({top: '20%', right: 0, title: "modify anno2"});
button8.addEventListener('click', function(e) {
	anno2.title = "Hello";
	anno2.subtitle = "Hi there.";
	anno2.longitude = 151.27689;
});

win.add(button);
win.add(button2);
win.add(button3);
win.add(button4);
win.add(button5);
win.add(button6);
win.add(button7);
win.add(button8);
win.open();
}

/********************************************************************************/
							//Routes
/********************************************************************************/
function routeTest() {

var win = Ti.UI.createWindow({fullscreen: false});
var map = MapModule.createView({
	userLocation: true,
	mapType: MapModule.NORMAL_TYPE,
	animate: true,
	region: {latitude: -33.87365, longitude: 151.20689, latitudeDelta: 0.02, longitudeDelta: 0.02 }, //Sydney
	top: '30%'
});
win.add(map);

var route1 = [{latitude: -33.87365, longitude: 151.20689}, {latitude: -33.87469, longitude: 151.20689}, {latitude: -33.87375, longitude: 151.20589}];
var route2 = [{latitude: -33.87565, longitude: 151.20789}, {latitude: -33.87469, longitude: 151.20689}, {latitude: -33.86375, longitude: 151.20589}];

var route = MapModule.createRoute({
    points: route1,
    color: "blue",
    width: 5.0
});

map.addRoute(route);

var button = Ti.UI.createButton({top: 0, left: 0, title: "+1 width"});
var w = 5.0;
button.addEventListener('click', function(e) {
	route.width = w + 1.0;
	w = w + 1.0;
});

var button2 = Ti.UI.createButton({top: 0, title: "change color"});
button2.addEventListener('click', function(e) {
	route.color = 'red';
});

var button3 = Ti.UI.createButton({top: 0, right: 0, title: "change routes"});
button3.addEventListener('click', function(e) {
	route.points = route2;
});

var button4 = Ti.UI.createButton({top: '10%', title: "remove route"});
button4.addEventListener('click', function(e) {
	map.removeRoute(route);
});

var button5 = Ti.UI.createButton({top: '10%', left: 0, title: "add route"});
button5.addEventListener('click', function(e) {
	map.addRoute(route);
});

win.add(button);
win.add(button2);
win.add(button3);
win.add(button4);
win.add(button5);
win.open();
}



