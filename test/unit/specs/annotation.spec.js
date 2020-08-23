const Map = require('ti.map');

var anno = Map.createAnnotation({
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

describe('ti.map.Annotation', function () {
	it('should have valid annotationDisplayPriority', () => {
		expect(anno.annotationDisplayPriority).toEqual(Map.FEATURE_DISPLAY_PRIORITY_REQUIRE);
	});

	it('should have valid canShowCallout', () => {
		expect(anno.canShowCallout).toEqual(true);
	});

	it('should have valid centerOffset', () => {
		expect(anno.centerOffset).toEqual(jasmine.any(Object));
	});

	it('should have valid clusterIdentifier', () => {
		expect(anno.clusterIdentifier).toEqual('clusterIdentifier');
	});

	it('should have valid collisionMode', () => {
		expect(anno.collisionMode).toEqual(Map.ANNOTATION_VIEW_COLLISION_MODE_RECTANGLE);
	});

	it('should have valid customView', () => {
		expect(anno.customView).toEqual(jasmine.any(Object));
	});

	it('should have valid draggable', () => {
		expect(anno.draggable).toEqual(false);
	});

	it('should have valid hidden', () => {
		expect(anno.hidden).toEqual(false);
	});

	it('should have valid image', () => {
		expect(anno.image).toEqual('map_pin.png');
	});

	it('should have valid latitude', () => {
		expect(anno.latitude).toEqual(-33.87365);
	});

	it('should have valid leftButton', () => {
		expect(anno.leftButton).toEqual('leftButton');
	});

	it('should have valid leftView', () => {
		expect(anno.leftView).toEqual(jasmine.any(Object));
	});

	it('should have valid longitude', () => {
		expect(anno.longitude).toEqual(151.20689);
	});

	it('should have valid markerAnimatesWhenAdded', () => {
		expect(anno.markerAnimatesWhenAdded).toEqual(false);
	});

	it('should have valid markerColor', () => {
		expect(anno.markerColor).toEqual('black');
	});

	it('should have valid markerGlyphColor', () => {
		expect(anno.markerGlyphColor).toEqual('red');
	});

	it('should have valid markerGlyphImage', () => {
		expect(anno.markerGlyphImage).toEqual('map_pin.png');
	});

	it('should have valid markerSubtitleVisibility', () => {
		expect(anno.markerSubtitleVisibility).toEqual(0);
	});

	it('should have valid markerTitleVisibility', () => {
		expect(anno.markerTitleVisibility).toEqual(0);
	});

	it('should have valid pincolor', () => {
		expect(anno.pincolor).toEqual(jasmine.any(Number));
	});

	it('should have valid previewContext', () => {
		expect(anno.previewContext).toEqual(undefined);
	});

	it('should have valid rightButton', () => {
		expect(anno.rightButton).toEqual(jasmine.any(Object));
	});

	it('should have valid rightView', () => {
		expect(anno.rightView).toEqual(jasmine.any(Object));
	});

	it('should have valid showAsMarker', () => {
		expect(anno.showAsMarker).toEqual(false);
	});

	it('should have valid showInfoWindow', () => {
		expect(anno.showInfoWindow).toEqual(false);
	});

	it('should have valid subtitle', () => {
		expect(anno.subtitle).toEqual('Sydney is quite chill');
	});

	it('should have valid subtitleid', () => {
		expect(anno.subtitleid).toEqual('subtitleid');
	});

	it('should have valid title ', () => {
		expect(anno.title).toEqual('Sydney');
	});

	it('should have valid titleid', () => {
		expect(anno.titleid).toEqual('titleid');
	});
});

