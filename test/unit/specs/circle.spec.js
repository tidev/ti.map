const Map = require('ti.map');

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

	it('should have valid radius', () => {
		expect(circle.radius).toEqual(1000);
	});

	it('should have valid strokeWidth', () => {
		expect(circle.strokeWidth).toEqual(2);
	});

	it('should have valid strokeColor', () => {
		expect(circle.strokeColor).toEqual(Map.D2BE1F);
	});

	it('should have valid fillColor', () => {
		expect(circle.fillColor).toEqual(Map.BFFFE725);
	});

});
