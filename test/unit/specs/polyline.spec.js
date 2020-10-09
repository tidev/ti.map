const Map = require('ti.map');

const ANDROID = (Ti.Platform.osname === 'android');
const IOS = (Ti.Platform.osname === 'iphone' || Ti.Platform.osname === 'ipad');

describe('ti.map', () => {
	describe('#createPolyline()', () => {
		it('is a Function', () => {
			expect(Map.createPolyline).toEqual(jasmine.any(Function));
		});
	});
});

describe('ti.map.Polyline', () => {
	let polyline;
	beforeAll(() => {
		polyline = Map.createPolyline({
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
			strokeColor: '#60FF0000',
			strokeWidth: 2,
			zIndex: 10
		});
	});

	it('.apiName', () => {
		expect(polyline.apiName).toEqual('Ti.Map.Polyline');
	});

	it('.points should match given Points', () => {
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

	it('.strokeWidth should match given Number', () => {
		expect(polyline.strokeWidth).toEqual(2);
	});

	it('.strokeColor should match given String', () => {
		if (IOS) {
			// FIXME: iOS should return the string if that's what was set!
			expect(polyline.strokeColor.toHex()).toEqual('#60FF0000');
		} else {
			expect(polyline.strokeColor).toEqual('#60FF0000');
		}
	});

	it('.zIndex should match given Number', () => {
		expect(polyline.zIndex).toEqual(10);
	});

	if (ANDROID) {
		describe('.jointType', () => {
			it('defaults to POLYLINE_JOINT_DEFAULT', () => {
				expect(polyline.jointType).toEqual(Map.POLYLINE_JOINT_DEFAULT);
			});
		});
	}
});
