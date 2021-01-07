const Map = require('ti.map');
const IOS = (Ti.Platform.osname === 'iphone' || Ti.Platform.osname === 'ipad');

describe('ti.map', () => {
	describe('#createCircle()', () => {
		it('is a Function', () => {
			expect(Map.createCircle).toEqual(jasmine.any(Function));
		});
	});
});

describe('ti.map.Circle', function () {
	let circle;
	beforeAll(() => {
		circle = Map.createCircle({
			center: {
				latitude: -33.87365,
				longitude: 151.20689
			},
			radius: 1000,
			strokeWidth: 2,
			strokeColor: '#D2BE1F',
			fillColor: '#BFFFE725' // 75% opacity
		});
	});

	it('.apiName', () => {
		expect(circle.apiName).toEqual('Ti.Map.Circle');
	});

	it('.radius should match given Number', () => {
		expect(circle.radius).toEqual(1000);
	});

	it('.strokeWidth should match given Number', () => {
		expect(circle.strokeWidth).toEqual(2);
	});

	it('.strokeColor should match given String', () => {
		if (IOS) {
			// FIXME: iOS should return the string if that's what was set!
			expect(circle.strokeColor.toHex()).toEqual('#D2BE1F');
		} else {
			expect(circle.strokeColor).toEqual('#D2BE1F');
		}
	});

	it('.fillColor should match given String', () => {
		if (IOS) {
			// FIXME: iOS should return the string if that's what was set!
			expect(circle.fillColor.toHex()).toEqual('#BFFFE725');
		} else {
			expect(circle.fillColor).toEqual('#BFFFE725');
		}
	});

	it('.center.latitude and .center.longitude should match given Number', () => {
		expect(circle.center.latitude).toEqual(-33.87365);
		expect(circle.center.longitude).toEqual(151.20689);
	});

});
