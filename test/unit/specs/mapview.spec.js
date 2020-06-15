var Map = require('ti.map');
const ANDROID = Ti.Platform.osname === 'android';

var mountainView = Map.createAnnotation({
	latitude: 37.390749,
	longitude: -122.081651,
	title: 'Appcelerator Headquarters',
	subtitle: 'Mountain View, CA',
	pincolor: Map.ANNOTATION_RED,
	myid: 1, // Custom property to uniquely identify this annotation.
});

var mapview = Map.createView({
	mapType: Map.NORMAL_TYPE,
	region: {
		latitude: 33.74511,
		longitude: -84.38993,
		latitudeDelta: 0.01,
		longitudeDelta: 0.01,
		tilt: 90,
		bearing: 45,
		zoom: 17.5,
	},
	animate: true,
	regionFit: true,
	compassEnabled: true,
	enableZoomControls: true,
	indoorEnabled: true,
	mapToolbarEnabled: true,
	maxZoomLevel: 2,
	minZoomLevel: 1,
	pitchEnabled: true,
	annotations: [ mountainView ],
	rotateEnabled: true,
	scrollEnabled: true,
	showsBuildings: true,
	showsPointsOfInterest: true,
	showsScale: true,
	showsTraffic: true,
	traffic: true,
	userLocation: true,
	userLocationButton: true,
	zOrderOnTop: true,
	zoom: 2,
	zoomEnabled: true,
});

describe('Porperty', function () {
	it('should have valid map type', () => {
		expect(mapview.mapType).toEqual(Map.NORMAL_TYPE);
	});

	it('should have valid region', () => {
		expect(mapview.region).toEqual({
			latitude: 33.74511,
			longitude: -84.38993,
			latitudeDelta: 0.01,
			longitudeDelta: 0.01,
			tilt: 90,
			bearing: 45,
			zoom: 17.5,
		});
	});

	it('should animate', () => {
		if (ANDROID) {
			expect(mapview.animate).toEqual(true);
		} else {
			expect(mapview.animate).toEqual(jasmine.any(Function));
		}
	});

	it('should have rtegion to fit', () => {
		expect(mapview.regionFit).toEqual(true);
	});

	it('should be compassEnabled', () => {
		expect(mapview.compassEnabled).toEqual(true);
	});

	it('should be enableZoomControls', () => {
		expect(mapview.enableZoomControls).toEqual(true);
	});

	it('should be mapToolbarEnabled', () => {
		expect(mapview.mapToolbarEnabled).toEqual(true);
	});

	it('should be maxZoomLevel', () => {
		if (ANDROID) {
			expect(mapview.maxZoomLevel).toEqual(0);
		} else {
			expect(mapview.maxZoomLevel).toEqual(2);
		}
	});

	it('should be minZoomLevel', () => {
		if (ANDROID) {
			expect(mapview.minZoomLevel).toEqual(0);
		} else {
			expect(mapview.minZoomLevel).toEqual(1);
		}
	});

	it('should be pitchEnabled', () => {
		expect(mapview.pitchEnabled).toEqual(true);
	});

	it('should be rotateEnabled', () => {
		expect(mapview.rotateEnabled).toEqual(true);
	});

	it('should be scrollEnabled', () => {
		expect(mapview.scrollEnabled).toEqual(true);
	});

	it('should be showsBuildings', () => {
		expect(mapview.showsBuildings).toEqual(true);
	});

	it('should be showsPointsOfInterest', () => {
		expect(mapview.showsPointsOfInterest).toEqual(true);
	});

	it('should be showsScale', () => {
		expect(mapview.showsScale).toEqual(true);
	});

	it('should be showsTraffic', () => {
		expect(mapview.showsTraffic).toEqual(true);
	});

	it('should be traffic', () => {
		expect(mapview.traffic).toEqual(true);
	});

	it('should be userLocation', () => {
		expect(mapview.userLocation).toEqual(true);
	});

	it('should be userLocationButton', () => {
		expect(mapview.userLocationButton).toEqual(true);
	});

	it('should be zOrderOnTop', () => {
		expect(mapview.zOrderOnTop).toEqual(true);
	});

	it('should be zoom', () => {
		if (ANDROID) {
			expect(mapview.zoom).toEqual(2);
		} else {
			expect(mapview.zoom).toEqual(jasmine.any(Function));
		}
	});

	it('should be zoomEnabled', () => {
		expect(mapview.zoomEnabled).toEqual(true);
	});
});
describe('Methods', function () {
	it('addAnnotation', () => {
		expect(mapview.addAnnotation).toEqual(jasmine.any(Function));
	});

	it('addAnnotations', () => {
		expect(mapview.addAnnotations).toEqual(jasmine.any(Function));
	});

	it('addCircle', () => {
		expect(mapview.addCircle).toEqual(jasmine.any(Function));
	});

	if (!ANDROID) {
		it('addCircles', () => {
			expect(mapview.addCircles).toEqual(jasmine.any(Function));
		});
	}

	it('addImageOverlay', () => {
		expect(mapview.addImageOverlay).toEqual(jasmine.any(Function));
	});

	it('addImageOverlays', () => {
		expect(mapview.addImageOverlays).toEqual(jasmine.any(Function));
	});

	it('addPolygon', () => {
		expect(mapview.addPolygon).toEqual(jasmine.any(Function));
	});

	it('addPolyline', () => {
		expect(mapview.addPolyline).toEqual(jasmine.any(Function));
	});

	it('addPolylines', () => {
		expect(mapview.addPolylines).toEqual(jasmine.any(Function));
	});

	it('addRoute', () => {
		expect(mapview.addRoute).toEqual(jasmine.any(Function));
	});

	if (!ANDROID) {
		it('animateCamera', () => {
			expect(mapview.animateCamera).toEqual(jasmine.any(Function));
		});
	}

	it('deselectAnnotation', () => {
		expect(mapview.deselectAnnotation).toEqual(jasmine.any(Function));
	});

	it('removeAllAnnotations', () => {
		expect(mapview.removeAllAnnotations).toEqual(jasmine.any(Function));
	});

	it('removeAllCircles', () => {
		expect(mapview.removeAllCircles).toEqual(jasmine.any(Function));
	});

	it('removeAllImageOverlays', () => {
		expect(mapview.removeAllImageOverlays).toEqual(jasmine.any(Function));
	});

	it('removeAllPolygons', () => {
		expect(mapview.removeAllPolygons).toEqual(jasmine.any(Function));
	});

	it('removeAllPolylines', () => {
		expect(mapview.removeAllPolylines).toEqual(jasmine.any(Function));
	});

	it('removeAnnotation', () => {
		expect(mapview.removeAnnotation).toEqual(jasmine.any(Function));
	});

	it('removeAnnotations', () => {
		expect(mapview.removeAnnotations).toEqual(jasmine.any(Function));
	});

	it('removeCircle', () => {
		expect(mapview.removeCircle).toEqual(jasmine.any(Function));
	});

	it('removeImageOverlay', () => {
		expect(mapview.removeImageOverlay).toEqual(jasmine.any(Function));
	});

	it('removePolygon', () => {
		expect(mapview.removePolygon).toEqual(jasmine.any(Function));
	});

	it('removePolyline', () => {
		expect(mapview.removePolyline).toEqual(jasmine.any(Function));
	});

	it('removeRoute', () => {
		expect(mapview.removeRoute).toEqual(jasmine.any(Function));
	});

	it('selectAnnotation', () => {
		expect(mapview.selectAnnotation).toEqual(jasmine.any(Function));
	});

	it('setClusterAnnotation', () => {
		expect(mapview.setClusterAnnotation).toEqual(jasmine.any(Function));
	});

	it('setLocation', () => {
		expect(mapview.setLocation).toEqual(jasmine.any(Function));
	});

	it('showAnnotations', () => {
		expect(mapview.showAnnotations).toEqual(jasmine.any(Function));
	});

	it('zoom', () => {
		if (ANDROID) {
			expect(mapview.zoom).toEqual(2);
		} else {
			expect(mapview.zoom).toEqual(jasmine.any(Function));
		}
	});
});
