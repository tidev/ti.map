const Map = require('ti.map');
const ANDROID = (Ti.Platform.osname === 'android');

if (!ANDROID) {
	describe('ti.map', () => {
		describe('#createCamera()', () => {
			it('is a Function', () => {
				expect(Map.createCamera).toEqual(jasmine.any(Function));
			});
		});
	});

	describe('ti.map.Camera', () => {
		let camera;
		beforeAll(() => {
			camera = Map.createCamera({
				eyeCoordinate: {
					latitude: -33.87365,
					longitude: 151.20689
				},
				heading: 20,
				centerCoordinate: {
					latitude: -33.87365,
					longitude: 151.20689
				},
				altitude: 3000,
				pitch: 40
			});
		});

		it('should be defined', () => {
			expect(camera).not.toEqual(undefined);
		});

		it('should have valid center coordinate', () => {
			expect(camera.centerCoordinate).toEqual({
				latitude: -33.87365,
				longitude: 151.20689
			});
		});

		it('should have valid altitude', () => {
			expect(camera.altitude).toEqual(3000);
		});

		it('should have valid pitch', () => {
			expect(camera.pitch).toEqual(40);
		});

		it('should have valid eyeCoordinate', () => {
			expect(camera.eyeCoordinate).toEqual({
				latitude: -33.87365,
				longitude: 151.20689
			});
		});

		it('should have valid heading', () => {
			expect(camera.heading).toEqual(20);
		});
	});
}
