const ANDROID = (Ti.Platform.osname === 'android');
const IOS = (Ti.Platform.osname === 'iphone' || Ti.Platform.osname === 'ipad');

describe('ti.map', () => {
	let Map;
	it('can be required', () => {
		Map = require('ti.map');

		expect(Map).toBeDefined();
	});

	it('.apiName', () => {
		expect(Map.apiName).toBe('Ti.Map');
	});

	describe('constants', () => {

		describe('ANNOTATION_* colors', () => {
			it('ANNOTATION_GREEN', () => {
				expect(Map.ANNOTATION_GREEN).toEqual(jasmine.any(Number));
			});

			it('ANNOTATION_RED', () => {
				expect(Map.ANNOTATION_RED).toEqual(jasmine.any(Number));
			});

			it('ANNOTATION_YELLOW', () => {
				expect(Map.ANNOTATION_YELLOW).toEqual(jasmine.any(Number));
			});

			it('ANNOTATION_AZURE', () => {
				expect(Map.ANNOTATION_AZURE).toEqual(jasmine.any(Number));
			});

			it('ANNOTATION_BLUE', () => {
				expect(Map.ANNOTATION_BLUE).toEqual(jasmine.any(Number));
			});

			it('ANNOTATION_CYAN', () => {
				expect(Map.ANNOTATION_CYAN).toEqual(jasmine.any(Number));
			});

			it('ANNOTATION_MAGENTA', () => {
				expect(Map.ANNOTATION_MAGENTA).toEqual(jasmine.any(Number));
			});

			it('ANNOTATION_ORANGE', () => {
				expect(Map.ANNOTATION_ORANGE).toEqual(jasmine.any(Number));
			});

			it('ANNOTATION_PURPLE', () => {
				expect(Map.ANNOTATION_PURPLE).toEqual(jasmine.any(Number));
			});

			it('ANNOTATION_ROSE', () => {
				expect(Map.ANNOTATION_ROSE).toEqual(jasmine.any(Number));
			});

			it('ANNOTATION_VIOLET', () => {
				expect(Map.ANNOTATION_VIOLET).toEqual(jasmine.any(Number));
			});
		});

		describe('ANNOTATION_DRAG_STATE_*', () => {
			it('ANNOTATION_DRAG_STATE_END', () => {
				expect(Map.ANNOTATION_DRAG_STATE_END).toEqual(jasmine.any(Number));
			});

			it('ANNOTATION_DRAG_STATE_START', () => {
				expect(Map.ANNOTATION_DRAG_STATE_START).toEqual(jasmine.any(Number));
			});
		});

		if (IOS) {
			describe('OVERLAY_LEVEL_*', () => {
				it('OVERLAY_LEVEL_ABOVE_LABELS', () => {
					expect(Map.OVERLAY_LEVEL_ABOVE_LABELS).toEqual(jasmine.any(Number));
				});

				it('OVERLAY_LEVEL_ABOVE_ROADS', () => {
					expect(Map.OVERLAY_LEVEL_ABOVE_ROADS).toEqual(jasmine.any(Number));
				});
			});
		}

		if (ANDROID) {
			describe('SERVICE_*', () => {
				it('SERVICE_DISABLED', () => {
					expect(Map.SERVICE_DISABLED).toEqual(jasmine.any(Number));
				});

				it('SERVICE_INVALID', () => {
					expect(Map.SERVICE_INVALID).toEqual(jasmine.any(Number));
				});

				it('SERVICE_MISSING', () => {
					expect(Map.SERVICE_MISSING).toEqual(jasmine.any(Number));
				});

				it('SERVICE_VERSION_UPDATE_REQUIRED', () => {
					expect(Map.SERVICE_VERSION_UPDATE_REQUIRED).toEqual(jasmine.any(Number));
				});

				it('SUCCESS', () => {
					expect(Map.SUCCESS).toEqual(jasmine.any(Number));
				});
			});
		}

		describe('*_TYPE', () => {
			it('NORMAL_TYPE', () => {
				expect(Map.NORMAL_TYPE).toEqual(jasmine.any(Number));
			});

			it('SATELLITE_TYPE', () => {
				expect(Map.SATELLITE_TYPE).toEqual(jasmine.any(Number));
			});

			it('HYBRID_TYPE', () => {
				expect(Map.HYBRID_TYPE).toEqual(jasmine.any(Number));
			});

			if (ANDROID) {
				it('TERRAIN_TYPE', () => {
					expect(Map.TERRAIN_TYPE).toEqual(jasmine.any(Number));
				});
			}
		});
	});

	describe('methods', () => {
		if (ANDROID) {
			describe('#isGooglePlayServicesAvailable()', () => {
				it('is a Function', () => {
					expect(Map.isGooglePlayServicesAvailable).toEqual(jasmine.any(Function));
				});

				it('returns one of expected constant values', () => {
					const value = Map.isGooglePlayServicesAvailable();

					expect(value).toEqual(jasmine.any(Number));

					const possibleValues = [ Map.SERVICE_DISABLED, Map.SERVICE_INVALID, Map.SERVICE_MISSING, Map.SERVICE_VERSION_UPDATE_REQUIRED, Map.SUCCESS ];

					expect(possibleValues).toContain(value);
				});
			});
		}
	});
});
