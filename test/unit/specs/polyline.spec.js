const Map = require('ti.map');

var polyline = Map.createPolyline({
	points: [ {
		latitude: -33.884717,
		longitude: 151.187993
	},
	[ 151.203099, -33.882152 ],
	{
		latitude: -33.886783,
		longitude: 151.218033
	}
	],
	strokeColor: Map.D2BE1F,
	strokeWidth: 2,
	zIndex: 10
});

describe('ti.map.Polyline', function () {
	it('should have valid points', () => {
		expect(polyline.points).toEqual([ {
			latitude: -33.884717,
			longitude: 151.187993
		},
		[ 151.203099, -33.882152 ],
		{
			latitude: -33.886783,
			longitude: 151.218033
		}
		]);
	});

	it('should have valid strokeWidth', () => {
		expect(polyline.strokeWidth).toEqual(2);
	});

	it('should have valid strokeColor', () => {
		expect(polyline.strokeColor).toEqual(Map.D2BE1F);
	});

	it('should have valid zIndex', () => {
		expect(polyline.zIndex).toEqual(10);
	});

});
