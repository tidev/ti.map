const Map = require('ti.map');

describe('ti.map', () => {
	describe('#createImageOverlay()', () => {
		it('is a Function', () => {
			expect(Map.createImageOverlay).toEqual(jasmine.any(Function));
		});
	});
});

describe('ti.map.ImageOverlay', () => {
	let overlay;
	beforeAll(() => {
		const flowerJPGFile = Ti.Filesystem.getFile(Ti.Filesystem.resourcesDirectory, 'images', 'flower.jpg');
		const image = flowerJPGFile.read();
		overlay = Map.createImageOverlay({
			image,
			boundsCoordinate: {
				topLeft: {
					latitude: -33.87365,
					longitude: 151.20689
				},
				bottomRight: {
					latitude: -33.97365,
					longitude: 151.10689
				}
			}
		});
	});

	it('.image as Ti.Blob', () => {
		// TODO: What should we validate here?
		expect(overlay).not.toEqual(undefined);
	});

	it('.boundsCoordinate should match given Object', () => {
		// TODO: What should we validate here?
		expect(overlay.boundsCoordinate).toEqual({
			topLeft: {
				latitude: -33.87365,
				longitude: 151.20689
			},
			bottomRight: {
				latitude: -33.97365,
				longitude: 151.10689
			}
		});
	});

	// TODO: Actually add overlay to a Map.View!
});
