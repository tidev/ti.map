const Map = require('ti.map');
const IOS = (Ti.Platform.osname === 'iphone' || Ti.Platform.osname === 'ipad');

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
			} ],
			fillColor: '#F2FA0C',
			strokeColor: '#D4D93F',
			strokeWidth: 5,
			zIndex: 2
		});
	});

	it('.apiName', () => {
		expect(polygon.apiName).toEqual('Ti.Map.Polygon');
	});

	it('.points should match given points', () => {
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
		} ]);
	});

	it('.strokeWidth should match given Number', () => {
		expect(polygon.strokeWidth).toEqual(5);
	});

	it('.strokeColor should match given String', () => {
		if (IOS) {
			// FIXME: iOS should return the string if that's what was set!
			expect(polygon.strokeColor.toHex()).toEqual('#D4D93F');
		} else {
			expect(polygon.strokeColor).toEqual('#D4D93F');
		}
	});

	it('.fillColor should match given String', () => {
		if (IOS) {
			// FIXME: iOS should return the string if that's what was set!
			expect(polygon.fillColor.toHex()).toEqual('#F2FA0C');
		} else {
			expect(polygon.fillColor).toEqual('#F2FA0C');
		}
	});

	it('.zIndex should match given Number', () => {
		expect(polygon.zIndex).toEqual(2);
	});

});
