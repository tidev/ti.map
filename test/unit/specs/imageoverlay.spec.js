const Map = require('ti.map');
const ANDROID = (Ti.Platform.osname === 'android');

describe('ti.map', () => {
	describe('#createImageOverlay()', () => {
		it('is a Function', () => {
			expect(Map.createImageOverlay).toEqual(jasmine.any(Function));
		});
	});
});

describe('ti.map.ImageOverlay', () => {
	it('.image as Ti.Blob', () => {
		const flowerJPGFile = Ti.Filesystem.getFile(Ti.Filesystem.resourcesDirectory, 'images', 'flower.jpg');
		const blob = flowerJPGFile.read();
		const overlay = Map.createImageOverlay({
			image: blob
		});

		// TODO: What should we validate here?
		expect(overlay).not.toEqual(undefined);
	});

	it('.boundsCoordinate', () => {
		const overlay = Map.createImageOverlay({
			boundsCoordinate: {
				topLeft: {
					latitude: -33.87365,
					longitude: 151.20689
				},
				bottomRight: {
					latitude: -33.77365,
					longitude: 151.10689
				}
			}
		});
		// TODO: What should we validate here?
		expect(overlay).not.toEqual(undefined);
	});

	// TODO: Actually add overlay to a Map.View!
});
