const Map = require('ti.map');

const ANDROID = (Ti.Platform.osname === 'android');
if (ANDROID) {

	let streetView = Map.createStreetViewPanorama({
		panning: true,
		position: {
			latitude: 22.0,
			longitude: 10.0
		},
		streetNames: true,
		userNavigation: true,
		zoom: true
	});

	describe('Map.StreetViewPanorama', function () {
		it('should have valid panning value', () => {
			expect(streetView.panning).toEqual(true);
		});

		it('should have valid position value', () => {
			expect(streetView.position).toEqual(jasmine.any(Object));
		});

		it('should have valid streetNames value', () => {
			expect(streetView.streetNames).toEqual(true);
		});

		it('should have valid userNavigation value', () => {
			expect(streetView.userNavigation).toEqual(true);
		});

		it('should have valid zoom value', () => {
			expect(streetView.zoom).toEqual(true);
		});
	});
}
