const Map = require('ti.map');

const ANDROID = (Ti.Platform.osname === 'android');

describe('ti.map', function () {

	let win;
	let rootWindow;

	// Create and open a root window for the rest of the below child window tests to use as a parent.
	// We're not going to close this window until the end of this test suite.
	// // Note: Android needs this so that closing the last window won't back us out of the app.
	// beforeAll(function (finish) {
	// 	rootWindow = Ti.UI.createWindow();
	// 	rootWindow.addEventListener('open', function () {
	// 		finish();
	// 	});
	// 	rootWindow.open();
	// });

	// afterAll(function (finish) {
	// 	rootWindow.addEventListener('close', function () {
	// 		finish();
	// 	});
	// 	rootWindow.close();
	// });

	// afterEach(function (done) {
	// 	if (win) {
	// 		win.close();
	// 	}
	// 	win = null;

	// 	// timeout to allow window to close
	// 	setTimeout(() => {
	// 		done();
	// 	}, 500);
	// });

	describe('methods', () => {
		it('#createAnnotation()', () => {
			expect(Map.createAnnotation).toEqual(jasmine.any(Function));

			// win = Ti.UI.createWindow();

			const annotation = Map.createAnnotation({
				latitude: 37.3689,
				longitude: -122.0353,
				title: 'Mountain View',
				subtitle: 'Mountain View city',
			});

			expect(annotation).toEqual(jasmine.any(Object));
			expect(annotation.apiName).toEqual('Ti.Map.Annotation');

			// const view = Map.createView({
			// 	mapType: Map.NORMAL_TYPE,
			// 	region: { // Mountain View
			// 		latitude: 37.3689,
			// 		longitude: -122.0353,
			// 		latitudeDelta: 0.1,
			// 		longitudeDelta: 0.1
			// 	}
			// });
			// expect(view).toEqual(jasmine.any(Object));

			// view.addAnnotation(annotation);

			// win.add(view);
			// win.open();
		});

		// TDOO: Test actually adding an annotation to a view

		if (!ANDROID) {
			it('#createCirle()', () => {
				var circle = Map.createCircle({
					center: {
						latitude: -33.87365,
						longitude: 151.20689
					},
					radius: 1000,
					strokeWidth: 2,
					strokeColor: '#D2BE1F',
					fillColor: '#BFFFE725' // 75% opacity
				});

				expect(circle).toEqual(jasmine.any(Object));
				expect(circle.apiName).toEqual('Ti.Map.Circle');
			});
			it('#createPolygon()', () => {
				var polygon = Map.createPolygon({
					points: [
						[ 151.228290, -33.857280 ],
						[ 151.224428, -33.855427 ],
						[ 151.224170, -33.858991 ]
					],
					fillColor: '#F2FA0C',
					strokeColor: '#D4D93F',
					strokeWidth: 5,
					zIndex: 3 // should be on top of blue triangle on android
				});

				expect(polygon).toEqual(jasmine.any(Object));
				expect(polygon.apiName).toEqual('Ti.Map.Polygon');
			});

			it('#createPolyline()', () => {
				var polyline = Map.createPolyline({
					points: [
						{ latitude: -33.884717, longitude: 151.187993 },
						[ 151.203099, -33.882152 ],
						{ latitude: -33.886783, longitude: 151.218033 }
					],
					strokeColor: '#60FF0000',
					strokeWidth: 2,
					zIndex: 10
				});

				expect(polyline).toEqual(jasmine.any(Object));
				expect(polyline.apiName).toEqual('Ti.Map.Polyline');
			});
			it('#createSnapshotter()', () => {
				var snapshotter = Map.createSnapshotter({
					mapType: Map.HYBRID_TYPE,
					region: {
						latitude: 37.3382,
						longitude: -121.8863,
						latitudeDelta: 0.4,
						longitudeDelta: 0.4
					},
					size: {
						width: 300,
						height: 200
					}
				});

				expect(snapshotter).toEqual(jasmine.any(Object));
				expect(snapshotter.apiName).toEqual('Ti.Map.Snapshotter');
			});

			it('#createCamera()', () => {
				expect(Map.createCamera).toEqual(jasmine.any(Function));
			});
		}

		it('#createRoute()', () => {
			expect(Map.createRoute).toEqual(jasmine.any(Function));
		});

		it('#createView()', () => {
			expect(Map.createView).toEqual(jasmine.any(Function));

			// 	win = Ti.UI.createWindow();

			const view = Map.createView({
				mapType: Map.NORMAL_TYPE,
				region: { // Mountain View
					latitude: 37.3689,
					longitude: -122.0353,
					latitudeDelta: 0.1,
					longitudeDelta: 0.1
				}
			});

			expect(view).toEqual(jasmine.any(Object));
			expect(view.apiName).toEqual('Ti.Map.View');

		// 	win.add(view);
		// 	win.open();
		});

		// TODO: Test actually adding a view to a map

		if (ANDROID) {
			it('#isGooglePlayServicesAvailable()', () => {
				expect(Map.isGooglePlayServicesAvailable).toEqual(jasmine.any(Function));

				// TODO: Test on Windows and verify always returns false?
				const value = Map.isGooglePlayServicesAvailable();

				expect(value).toEqual(jasmine.any(Number));

				const possibleValues = [ Map.SERVICE_DISABLED, Map.SERVICE_INVALID, Map.SERVICE_MISSING, Map.SERVICE_VERSION_UPDATE_REQUIRED, Map.SUCCESS ];

				expect(possibleValues).toContain(value);
			});
		}
	});
});
