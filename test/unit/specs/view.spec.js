const Map = require('ti.map');

describe('ti.map', () => {
	describe('#createView()', () => {
		it('is a Function', () => {
			expect(Map.createView).toEqual(jasmine.any(Function));
		});
	});
});

describe('ti.map.View', () => {
	let view;
	beforeAll(() => {
		view = Map.createView({
			mapType: Map.NORMAL_TYPE,
			region: { // Mountain View
				latitude: 37.3689,
				longitude: -122.0353,
				latitudeDelta: 0.1,
				longitudeDelta: 0.1
			}
		});
	});

	it('.apiName is Ti.MapView', () => {
		expect(view.apiName).toEqual('Ti.Map.View');
	});

	// TODO: Test actually adding a view to a map
});
