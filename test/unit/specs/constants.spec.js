let Map;

describe('ti.map', function () {

	it('can be required', () => {
		Map = require('ti.map');
		expect(Map).toBeDefined();
	});

	describe('constants', () => {
		it('apiName', () => {
			expect(Map.apiName).toBe('Ti.Map');
		});

		// Intentional skip, constant only for Android
		// it.iosMissing('ANNOTATION_AZURE', function () {
		// 	should(Map).have.constant('ANNOTATION_AZURE').which.is.a.Number;
		// });

		// // Intentional skip, constant only for Android
		// it.iosMissing('ANNOTATION_BLUE', function () {
		// 	should(Map).have.constant('ANNOTATION_BLUE').which.is.a.Number;
		// });

		// // Intentional skip, constant only for Android
		// it.iosMissing('ANNOTATION_CYAN', function () {
		// 	should(Map).have.constant('ANNOTATION_CYAN').which.is.a.Number;
		// });

		it('ANNOTATION_GREEN', function () {
			expect(Map.ANNOTATION_GREEN).toEqual(jasmine.any(Number));
		});

		// Intentional skip, constant only for Android
		// it.iosMissing('ANNOTATION_MAGENTA', function () {
		// 	should(Map).have.constant('ANNOTATION_MAGENTA').which.is.a.Number;
		// });

		// // Intentional skip, constant only for Android
		// it.iosMissing('ANNOTATION_ORANGE', function () {
		// 	should(Map).have.constant('ANNOTATION_ORANGE').which.is.a.Number;
		// });

		// // Intentional skip, constant only for iOS
		// it.iosMissing('ANNOTATION_PURPLE', function () {
		// 	should(Map).have.constant('ANNOTATION_PURPLE').which.is.a.Number;
		// });

		it('ANNOTATION_RED', function () {
			expect(Map.ANNOTATION_RED).toEqual(jasmine.any(Number));
		});

		// Intentional skip, constant only for Android
		// it.iosMissing('ANNOTATION_ROSE', function () {
		// 	should(Map).have.constant('ANNOTATION_ROSE').which.is.a.Number;
		// });

		// // Intentional skip, constant only for Android
		// it.iosMissing('ANNOTATION_VIOLET', function () {
		// 	should(Map).have.constant('ANNOTATION_VIOLET').which.is.a.Number;
		// });

		// // FIXME get working on iOS, says value is undefined, not a Number
		// it.iosBroken('ANNOTATION_YELLOW', function () {
		// 	should(Map).have.constant('ANNOTATION_YELLOW').which.is.a.Number;
		// });

		it('ANNOTATION_DRAG_STATE_END', function () {
			expect(Map.ANNOTATION_DRAG_STATE_END).toEqual(jasmine.any(Number));
		});

		it('ANNOTATION_DRAG_STATE_START', function () {
			expect(Map.ANNOTATION_DRAG_STATE_START).toEqual(jasmine.any(Number));
		});

		// Intentionally skip on Android, constant doesn't exist
		// it.androidMissing('OVERLAY_LEVEL_ABOVE_LABELS', function () {
		// 	should(Map).have.constant('OVERLAY_LEVEL_ABOVE_LABELS').which.is.a.Number;
		// });

		// // Intentionally skip on Android, constant doesn't exist
		// it.androidMissing('OVERLAY_LEVEL_ABOVE_ROADS', function () {
		// 	should(Map).have.constant('OVERLAY_LEVEL_ABOVE_ROADS').which.is.a.Number;
		// });

		// // Intentional skip, constant only for Android
		// it.iosMissing('SERVICE_DISABLED', function () {
		// 	should(Map).have.constant('SERVICE_DISABLED').which.is.a.Number;
		// });

		// // Intentional skip, constant only for Android
		// it.iosMissing('SERVICE_INVALID', function () {
		// 	should(Map).have.constant('SERVICE_INVALID').which.is.a.Number;
		// });

		// // Intentional skip, constant only for Android
		// it.iosMissing('SERVICE_MISSING', function () {
		// 	should(Map).have.constant('SERVICE_MISSING').which.is.a.Number;
		// });

		// // Intentional skip, constant only for Android
		// it.iosMissing('SERVICE_VERSION_UPDATE_REQUIRED', function () {
		// 	should(Map).have.constant('SERVICE_VERSION_UPDATE_REQUIRED').which.is.a.Number;
		// });

		// // Intentional skip, constant only for Android
		// it.iosMissing('SUCCESS', function () {
		// 	should(Map).have.constant('SUCCESS').which.is.a.Number;
		// });

		it('NORMAL_TYPE', function () {
			expect(Map.NORMAL_TYPE).toEqual(jasmine.any(Number));
		});

		it('SATELLITE_TYPE', function () {
			expect(Map.SATELLITE_TYPE).toEqual(jasmine.any(Number));
		});

		it('HYBRID_TYPE', function () {
			expect(Map.HYBRID_TYPE).toEqual(jasmine.any(Number));
		});

		// Intentional skip for iOS, constant only for Android
		// it.iosMissing('TERRAIN_TYPE', function () {
		// 	should(Map).have.constant('TERRAIN_TYPE').which.is.a.Number;
		// });
	});

});