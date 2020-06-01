const Map = require('ti.map');
const ANDROID = (Ti.Platform.osname === 'android');

const route1 = Map.createRoute({
	points: [ {
		latitude: -33.87565,
		longitude: 151.20789
	} ],
	color: 'black'
});
const route2 = Map.createRoute({
	points: [ {
		latitude: -33.87565,
		longitude: 151.20789
	} ],
	level: Map.OVERLAY_LEVEL_ABOVE_LABELS,
	width: 12
});
describe('ti.map.Route', function () {
	describe('For route1', function () {
		it('should not undefined', () => {
			expect(route1).not.toEqual(undefined);
		});
		if (ANDROID) {
			it('should have valid center coordinate', () => {
				expect(route1.points).toEqual([ {
					latitude: -33.87565,
					longitude: 151.20789
				} ]);
			});
		}
		it('should have default altitude', () => {
			expect(route1.level).toEqual(Map.OVERLAY_LEVEL_ABOVE_ROADS);
		});

		it('should have valid color', () => {
			if (ANDROID) {
				expect(route1.color).toEqual('black');
			} else {
				expect(route1.color.name).toEqual('black');
			}
		});
	});

	describe('for route2', function () {
		it('should have valid altitude', () => {
			expect(route2.level).toEqual(Map.OVERLAY_LEVEL_ABOVE_LABELS);
		});

		it('should have valid width', () => {
			expect(route2.width).toEqual(12);
		});
	});
});
