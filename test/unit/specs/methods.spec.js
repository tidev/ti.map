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
		
				const value = Map.isGooglePlayServicesAvailable(); // TODO: Test on Windows and verify always returns false?
				expect(value).toEqual(jasmine.any(Number));

				const possibleValues = [ Map.SERVICE_DISABLED, Map.SERVICE_INVALID, Map.SERVICE_MISSING, Map.SERVICE_VERSION_UPDATE_REQUIRED, Map.SUCCESS ];
				expect(possibleValues).toContain(value);
			});
		}
	});
});