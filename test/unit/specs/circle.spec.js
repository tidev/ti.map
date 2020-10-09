const Map = require('ti.map');

var circle = Map.createCircle({
	center: {
		latitude: -33.87365,
		longitude: 151.20689
	},
	radius: 1000,
	strokeWidth: 2,
	strokeColor: Map.D2BE1F,
	fillColor: Map.BFFFE725
});

describe('ti.map.Circle', function () {
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
