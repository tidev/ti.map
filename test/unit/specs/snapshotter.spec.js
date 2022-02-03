const Map = require('ti.map');
const IOS = (Ti.Platform.osname === 'iphone' || Ti.Platform.osname === 'ipad');

if (IOS) {
	describe('ti.map', () => {
		describe('#createSnapshotter()', () => {
			it('is a Function', () => {
				expect(Map.createSnapshotter).toEqual(jasmine.any(Function));
			});
		});
	});

	describe('ti.map.Snapshotter', () => {
		let snapshotter;
		beforeAll(() => {
			snapshotter = Map.createSnapshotter({
				mapType: Map.HYBRID_TYPE,
				region: {
					latitude: 37.3382,
					longitude: -121.8863,
					latitudeDelta: 0.4,
					longitudeDelta: 0.4
				},
				size: {
					width: 300,
					height: 200
				}
			});
		});

		it('.apiName', () => {
			expect(snapshotter.apiName).toEqual('Ti.Map.Snapshotter');
		});

		it('should have defined', () => {
			expect(snapshotter).not.toEqual(undefined);
		});

		it('should have function setMapType', () => {
			expect(snapshotter.setMapType).toEqual(jasmine.any(Function));
		});

		it('should have function setRegion', () => {
			expect(snapshotter.setRegion).toEqual(jasmine.any(Function));
		});

		it('should have function setSize', () => {
			expect(snapshotter.setSize).toEqual(jasmine.any(Function));
		});

		it('should  have function setShowsBuildings', () => {
			expect(snapshotter.setShowsBuildings).toEqual(jasmine.any(Function));
		});

		it('should have function setShowsPointsOfInterest', () => {
			expect(snapshotter.setShowsPointsOfInterest).toEqual(jasmine.any(Function));
		});

		it('should have function takeSnapshot', () => {
			expect(snapshotter.takeSnapshot).toEqual(jasmine.any(Function));
		});

		it('should be able take snapshot', done => {
			snapshotter.takeSnapshot({
				success: function (e) {
					expect(e.image).not.toEqual(undefined);
					done();
				},
				error: function (e) {
					done.fail(e);
				}
			});
		});
	});
}
