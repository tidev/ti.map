const Map = require('ti.map');
const ANDROID = (Ti.Platform.osname === 'android');

if (ANDROID) {
	describe('ti.map', () => {
		describe('#createTileOverlay()', () => {
			it('is a Function', () => {
				expect(Map.createTileOverlay).toEqual(jasmine.any(Function));
			});
		});
	});

	describe('ti.map.TileOverlay', () => {
		let overlay;
		beforeAll(() => {
			overlay = Map.createTileOverlay({
				service: Map.TILE_OVERLAY_TYPE_XYZ,
				url: 'http://stamen-tiles-a.a.ssl.fastly.net/watercolor/{z}/{x}/{y}.png'
			});
		});

		it('.apiName', () => {
			expect(overlay.apiName).toEqual('Ti.Map.TileOverlay');
		});

		it('.service should match given constant', () => {
			expect(overlay.service).toEqual(Map.TILE_OVERLAY_TYPE_XYZ);
		});

		it('.url should match given String', () => {
			expect(overlay.url).toEqual('http://stamen-tiles-a.a.ssl.fastly.net/watercolor/{z}/{x}/{y}.png');
		});

	});
}
