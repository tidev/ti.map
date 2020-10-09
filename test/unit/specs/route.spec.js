const Map = require('ti.map');
const ANDROID = (Ti.Platform.osname === 'android');

describe('ti.map', () => {
	describe('#createRoute()', () => {
		it('is a Function', () => {
			expect(Map.createRoute).toEqual(jasmine.any(Function));
		});
	});
});

describe('ti.map.Route', () => {
	let route1;
	let route2;

	beforeAll(() => {
		route1 = Map.createRoute({
			points: [ {
				latitude: -33.87565,
				longitude: 151.20789
			} ],
			color: 'black'
		});
		route2 = Map.createRoute({
			points: [ {
				latitude: -33.87565,
				longitude: 151.20789
			} ],
			level: Map.OVERLAY_LEVEL_ABOVE_LABELS,
			width: 12
		});
	});

	it('instace should be defined', () => {
		expect(route1).not.toEqual(undefined);
	});

	if (ANDROID) {
		it('.points should match values passed ot factory method', () => {
			expect(route1.points).toEqual([ {
				latitude: -33.87565,
				longitude: 151.20789
			} ]);
		});
	}

	describe('.level', () => {
		it('defaults to OVERLAY_LEVEL_ABOVE_ROADS', () => {
			expect(route1.level).toEqual(Map.OVERLAY_LEVEL_ABOVE_ROADS);
		});

		it('matches value from factory method', () => {
			expect(route2.level).toEqual(Map.OVERLAY_LEVEL_ABOVE_LABELS);
		});
	});

	it('.color matches value from factory method', () => {
		if (ANDROID) {
			expect(route1.color).toEqual('black');
		} else {
			expect(route1.color.name).toEqual('black');
		}
	});

	it('.width matches value from factory method', () => {
		expect(route2.width).toEqual(12);
	});
});
