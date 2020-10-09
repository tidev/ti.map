const Map = require('ti.map');

describe('ti.map', () => {
	describe('#createAnnotation()', () => {
		it('is a Function', () => {
			expect(Map.createAnnotation).toEqual(jasmine.any(Function));
		});
	});
});

describe('ti.map.Annotation', () => {
	let annotation;
	beforeAll(() => {
		annotation = Map.createAnnotation({
			centerOffset: {
				x: 0,
				y: 0
			},
			canShowCallout: true,
			clusterIdentifier: 'clusterIdentifier',
			collisionMode: Map.ANNOTATION_VIEW_COLLISION_MODE_RECTANGLE,
			draggable: false,
			leftButton: 'leftButton',
			markerAnimatesWhenAdded: false,
			markerColor: 'black',
			markerGlyphColor: 'red',
			latitude: -33.87365,
			image: 'map_pin.png',
			longitude: 151.20689,
			title: 'Sydney',
			subtitle: 'Sydney is quite chill',
			titleid: 'titleid',
			subtitleid: 'subtitleid',
			showInfoWindow: false,
			showAsMarker: false,
			markerTitleVisibility: 0,
			markerSubtitleVisibility: 0,
			customView:  Ti.UI.createView({}),
			leftView: Ti.UI.createView({}),
			rightView: Ti.UI.createView({}),
			rightButton:  Ti.UI.createButton({}),
			markerGlyphImage: 'map_pin.png',
			pincolor: Map.ANNOTATION_GREEN,
			hidden: false
		});
	});

	it('.apiName', () => {
		expect(annotation.apiName).toEqual('Ti.Map.Annotation');
	});

	it('should have valid annotationDisplayPriority', () => {
		expect(annotation.annotationDisplayPriority).toEqual(Map.FEATURE_DISPLAY_PRIORITY_REQUIRE);
	});

	it('should have valid canShowCallout', () => {
		expect(annotation.canShowCallout).toEqual(true);
	});

	it('should have valid centerOffset', () => {
		expect(annotation.centerOffset).toEqual(jasmine.any(Object));
	});

	it('should have valid clusterIdentifier', () => {
		expect(annotation.clusterIdentifier).toEqual('clusterIdentifier');
	});

	it('should have valid collisionMode', () => {
		expect(annotation.collisionMode).toEqual(Map.ANNOTATION_VIEW_COLLISION_MODE_RECTANGLE);
	});

	it('should have valid customView', () => {
		expect(annotation.customView).toEqual(jasmine.any(Object));
	});

	it('should have valid draggable', () => {
		expect(annotation.draggable).toEqual(false);
	});

	it('should have valid hidden', () => {
		expect(annotation.hidden).toEqual(false);
	});

	it('should have valid image', () => {
		expect(annotation.image).toEqual('map_pin.png');
	});

	it('should have valid latitude', () => {
		expect(annotation.latitude).toEqual(-33.87365);
	});

	it('should have valid leftButton', () => {
		expect(annotation.leftButton).toEqual('leftButton');
	});

	it('should have valid leftView', () => {
		expect(annotation.leftView).toEqual(jasmine.any(Object));
	});

	it('should have valid longitude', () => {
		expect(annotation.longitude).toEqual(151.20689);
	});

	it('should have valid markerAnimatesWhenAdded', () => {
		expect(annotation.markerAnimatesWhenAdded).toEqual(false);
	});

	it('should have valid markerColor', () => {
		expect(annotation.markerColor).toEqual('black');
	});

	it('should have valid markerGlyphColor', () => {
		expect(annotation.markerGlyphColor).toEqual('red');
	});

	it('should have valid markerGlyphImage', () => {
		expect(annotation.markerGlyphImage).toEqual('map_pin.png');
	});

	it('should have valid markerSubtitleVisibility', () => {
		expect(annotation.markerSubtitleVisibility).toEqual(0);
	});

	it('should have valid markerTitleVisibility', () => {
		expect(annotation.markerTitleVisibility).toEqual(0);
	});

	it('should have valid pincolor', () => {
		expect(annotation.pincolor).toEqual(jasmine.any(Number));
	});

	it('should have valid previewContext', () => {
		expect(annotation.previewContext).toEqual(undefined);
	});

	it('should have valid rightButton', () => {
		expect(annotation.rightButton).toEqual(jasmine.any(Object));
	});

	it('should have valid rightView', () => {
		expect(annotation.rightView).toEqual(jasmine.any(Object));
	});

	it('should have valid showAsMarker', () => {
		expect(annotation.showAsMarker).toEqual(false);
	});

	it('should have valid showInfoWindow', () => {
		expect(annotation.showInfoWindow).toEqual(false);
	});

	it('should have valid subtitle', () => {
		expect(annotation.subtitle).toEqual('Sydney is quite chill');
	});

	it('should have valid subtitleid', () => {
		expect(annotation.subtitleid).toEqual('subtitleid');
	});

	it('should have valid title ', () => {
		expect(annotation.title).toEqual('Sydney');
	});

	it('should have valid titleid', () => {
		expect(annotation.titleid).toEqual('titleid');
	});
});

