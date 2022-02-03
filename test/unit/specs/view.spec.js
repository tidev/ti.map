const Map = require('ti.map');
const ANDROID = (Ti.Platform.osname === 'android');

describe('ti.map', () => {
	describe('#createView()', () => {
		it('is a Function', () => {
			expect(Map.createView).toEqual(jasmine.any(Function));
		});
	});
});

describe('ti.map.View', () => {
	let mapview;
	beforeAll(() => {
		const mountainView = Map.createAnnotation({
			latitude: 37.390749,
			longitude: -122.081651,
			title: 'Appcelerator Headquarters',
			subtitle: 'Mountain View, CA',
			pincolor: Map.ANNOTATION_RED,
			myid: 1, // Custom property to uniquely identify this annotation.
		});

		mapview = Map.createView({
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
	});

	it('.apiName is Ti.MapView', () => {
		expect(mapview.apiName).toEqual('Ti.Map.View');
	});

	describe('methods', () => {
		it('#addAnnotation() is a Function', () => {
			expect(mapview.addAnnotation).toEqual(jasmine.any(Function));
		});

		it('#addAnnotations() is a Function', () => {
			expect(mapview.addAnnotations).toEqual(jasmine.any(Function));
		});

		it('#addCircle() is a Function', () => {
			expect(mapview.addCircle).toEqual(jasmine.any(Function));
		});

		if (!ANDROID) {
			// FIXME: add to android for parity
			it('#addCircles() is a Function', () => {
				expect(mapview.addCircles).toEqual(jasmine.any(Function));
			});
		}

		it('#addImageOverlay() is a Function', () => {
			expect(mapview.addImageOverlay).toEqual(jasmine.any(Function));
		});

		it('#addImageOverlays() is a Function', () => {
			expect(mapview.addImageOverlays).toEqual(jasmine.any(Function));
		});

		it('#addPolygon() is a Function', () => {
			expect(mapview.addPolygon).toEqual(jasmine.any(Function));
		});

		it('#addPolyline() is a Function', () => {
			expect(mapview.addPolyline).toEqual(jasmine.any(Function));
		});

		it('#addPolylines() is a Function', () => {
			expect(mapview.addPolylines).toEqual(jasmine.any(Function));
		});

		it('#addRoute() is a Function', () => {
			expect(mapview.addRoute).toEqual(jasmine.any(Function));
		});

		if (!ANDROID) {
			// TODO: Add to Android for parity
			it('#animateCamera() is a Function', () => {
				expect(mapview.animateCamera).toEqual(jasmine.any(Function));
			});
		}

		it('#deselectAnnotation() is a Function', () => {
			expect(mapview.deselectAnnotation).toEqual(jasmine.any(Function));
		});

		it('#removeAllAnnotations() is a Function', () => {
			expect(mapview.removeAllAnnotations).toEqual(jasmine.any(Function));
		});

		it('#removeAllCircles() is a Function', () => {
			expect(mapview.removeAllCircles).toEqual(jasmine.any(Function));
		});

		it('#removeAllImageOverlays() is a Function', () => {
			expect(mapview.removeAllImageOverlays).toEqual(jasmine.any(Function));
		});

		it('#removeAllPolygons() is a Function', () => {
			expect(mapview.removeAllPolygons).toEqual(jasmine.any(Function));
		});

		it('#removeAllPolylines() is a Function', () => {
			expect(mapview.removeAllPolylines).toEqual(jasmine.any(Function));
		});

		it('#removeAnnotation() is a Function', () => {
			expect(mapview.removeAnnotation).toEqual(jasmine.any(Function));
		});

		it('#removeAnnotations() is a Function', () => {
			expect(mapview.removeAnnotations).toEqual(jasmine.any(Function));
		});

		it('#removeCircle() is a Function', () => {
			expect(mapview.removeCircle).toEqual(jasmine.any(Function));
		});

		it('#removeImageOverlay() is a Function', () => {
			expect(mapview.removeImageOverlay).toEqual(jasmine.any(Function));
		});

		it('#removePolygon() is a Function', () => {
			expect(mapview.removePolygon).toEqual(jasmine.any(Function));
		});

		it('#removePolyline() is a Function', () => {
			expect(mapview.removePolyline).toEqual(jasmine.any(Function));
		});

		it('#removeRoute() is a Function', () => {
			expect(mapview.removeRoute).toEqual(jasmine.any(Function));
		});

		it('#selectAnnotation() is a Function', () => {
			expect(mapview.selectAnnotation).toEqual(jasmine.any(Function));
		});

		it('#setClusterAnnotation() is a Function', () => {
			expect(mapview.setClusterAnnotation).toEqual(jasmine.any(Function));
		});

		it('#setLocation() is a Function', () => {
			expect(mapview.setLocation).toEqual(jasmine.any(Function));
		});

		it('#showAnnotations() is a Function', () => {
			expect(mapview.showAnnotations).toEqual(jasmine.any(Function));
		});
	});

	describe('properties', () => {
		describe('.mapType', () => {
			it('matches value from factory method', () => {
				expect(mapview.mapType).toEqual(Map.NORMAL_TYPE);
			});

			// FIXME: Both platforms don't report default mapType value, but return undefined
			xit('defaults to NORMAL_TYPE', () => {
				expect(Map.createView({}).mapType).toEqual(Map.NORMAL_TYPE);
			});
		});

		it('.region matches value from factory method', () => {
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

		it('.animate matches value from factory method', () => {
			if (ANDROID) {
				expect(mapview.animate).toEqual(true);
			} else {
				// FIXME: iOS reports animate as method, presumably because parent Ti.UI.View has animate method that is clashing with animate property!
				expect(mapview.animate).toEqual(jasmine.any(Function));
			}
		});

		it('.regionFit is a Boolean', () => {
			expect(mapview.regionFit).toEqual(true);
		});

		it('.compassEnabled is a Boolean', () => {
			expect(mapview.compassEnabled).toEqual(true);
		});

		it('.enableZoomControls is a Boolean', () => {
			expect(mapview.enableZoomControls).toEqual(true);
		});

		it('.mapToolbarEnabled is a Boolean', () => {
			expect(mapview.mapToolbarEnabled).toEqual(true);
		});

		it('.maxZoomLevel is a Number', () => {
			// FIXME: Why the platform difference here?
			// TODO: Ensure it returns some constant value?
			if (ANDROID) {
				expect(mapview.maxZoomLevel).toEqual(0);
			} else {
				expect(mapview.maxZoomLevel).toEqual(2);
			}
		});

		it('.minZoomLevel is a Number', () => {
			// FIXME: Why the platform difference here?
			// TODO: Ensure it returns some constant value?
			if (ANDROID) {
				expect(mapview.minZoomLevel).toEqual(0);
			} else {
				expect(mapview.minZoomLevel).toEqual(1);
			}
		});

		it('.pitchEnabled is a Boolean', () => {
			expect(mapview.pitchEnabled).toEqual(true);
		});

		it('.rotateEnabled is a Boolean', () => {
			expect(mapview.rotateEnabled).toEqual(true);
		});

		it('.scrollEnabled is a Boolean', () => {
			expect(mapview.scrollEnabled).toEqual(true);
		});

		it('.showsBuildings is a Boolean', () => {
			expect(mapview.showsBuildings).toEqual(true);
		});

		it('.showsPointsOfInterest is a Boolean', () => {
			expect(mapview.showsPointsOfInterest).toEqual(true);
		});

		it('.showsScale is a Boolean', () => {
			expect(mapview.showsScale).toEqual(true);
		});

		it('.showsTraffic is a Boolean', () => {
			expect(mapview.showsTraffic).toEqual(true);
		});

		it('.traffic is a Boolean', () => {
			expect(mapview.traffic).toEqual(true);
		});

		it('.userLocation is a Boolean', () => {
			expect(mapview.userLocation).toEqual(true);
		});

		it('.userLocationButton is a Boolean', () => {
			expect(mapview.userLocationButton).toEqual(true);
		});

		it('.zOrderOnTop is a Boolean', () => {
			expect(mapview.zOrderOnTop).toEqual(true);
		});

		// FIXME: there's a name clash between the property and method!
		// Should probably add method to each, rename property to zoomLevel?
		it('.zoom is a Number', () => {
			if (ANDROID) {
				expect(mapview.zoom).toEqual(2);
			} else {
				expect(mapview.zoom).toEqual(jasmine.any(Function));
			}
		});

		it('.zoomEnabled is a Boolean', () => {
			expect(mapview.zoomEnabled).toEqual(true);
		});

		if (ANDROID) {
			describe('.minClusterSize', () => {
				it('is a Number', () => {
					expect(mapview.minClusterSize).toEqual(jasmine.any(Number));
				});

				it('defaults to 4', () => {
					expect(mapview.minClusterSize).toEqual(4);
				});
			});
		}
	});

	// TODO: Test actually adding a view to a map
});
