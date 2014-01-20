exports.title = '3D features';
exports.run = function(UI, Map) {
	var tilt = 90;
	var bearing = 45;
	var zoom = 17.5;
	var win = UI.createWindow({
		fullscreen : false
	});

	var tiltButton = Ti.UI.createButton({
		top : '10%',
		title : 'Tilt',
		left : '0%'
	});

	var bearingButton = Ti.UI.createButton({
		top : '10%',
		title : 'Bearing'
	});

	var zoomButton = Ti.UI.createButton({
		top : '10%',
		right : 0,
		title : 'Zoom'
	});
	
	var compassButton = Ti.UI.createButton({
		top:'20%',
		title: 'Toggle Compass'
	});

	tiltButton.addEventListener('click', function(e) {
		tilt = Math.random() * 90;
		map.region = {
			latitude : 49.261902,
			longitude : -123.12494706,
			tilt : tilt,
			bearing : bearing,
			zoom : zoom
		};
	});

	bearingButton.addEventListener('click', function(e) {
		bearing = Math.random() * 360;
		map.region = {
			latitude : 49.261902,
			longitude : -123.12494706,
			tilt : tilt,
			bearing : bearing,
			zoom : zoom
		};

	});

	zoomButton.addEventListener('click', function(e) {
		zoom = Math.random() * 21;
		map.region = {
			latitude : 49.261902,
			longitude : -123.12494706,
			tilt : tilt,
			bearing : bearing,
			zoom : zoom
		};

	});

	compassButton.addEventListener('click', function(e) {
		map.compassEnabled = !map.compassEnabled;
	});

	var map = Map.createView({
		userLocation : true,
		mapType : Map.NORMAL_TYPE,
		region : {
			latitude : 49.261902,
			longitude : -123.12494706,
			tilt : tilt,
			bearing : bearing,
			zoom : zoom
		}, //Sydney
		top : '30%'
	});

	map.addEventListener('regionchanged', function(e) {
		Ti.API.info("Latitude: " + e.latitude);
		Ti.API.info("Longitude: " + e.longitude);
		Ti.API.info("Type: " + e.type);
	});

	win.add(map);
	win.add(tiltButton);
	win.add(bearingButton);
	win.add(zoomButton);
	win.add(compassButton);
	win.open();
}
