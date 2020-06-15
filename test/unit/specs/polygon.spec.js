const Map = require('ti.map');

describe('ti.map', () => {
	describe('#createPolygon()', () => {
		it('is a Function', () => {
			expect(Map.createPolygon).toEqual(jasmine.any(Function));
		});
	});
});

describe('ti.map.Polygon', () => {
	let polygon;
	beforeAll(() => {
		polygon = Map.createPolygon({
			points: [ {
				latitude: -33.854429,
				longitude: 151.214429
			},
			{
				latitude: -33.854928,
				longitude: 151.236101
			},
			{
				latitude: -33.866189,
				longitude: 151.232668
			}
			],
			fillColor: '#F2FA0C',
			strokeColor: '#D4D93F',
			strokeWidth: 5,
			zIndex: 2
		});
	});

	it('.apiName', () => {
		expect(polygon.apiName).toEqual('Ti.Map.Polygon');
	});

	it('should have valid center', () => {
		expect(polygon.points).toEqual([ {
			latitude: -33.854429,
			longitude: 151.214429
		},
		{
			latitude: -33.854928,
			longitude: 151.236101
		},
		{
			latitude: -33.866189,
			longitude: 151.232668
		}
		]);
	});

	it('should have valid strokeWidth', () => {
		expect(polygon.strokeWidth).toEqual(5);
	});

	it('should have valid strokeColor', () => {
		expect(polygon.strokeColor).toEqual(Map.D2BE1F);
	});

	it('should have valid fillColor', () => {
		expect(polygon.fillColor).toEqual(Map.BFFFE725);
	});

	it('should have valid zIndex', () => {
		expect(polygon.zIndex).toEqual(2);
	});

});
