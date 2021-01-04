exports.title = 'Lite Mode';

exports.run = function(UI, Map) {
    const win = UI.createWindow(exports.title);
    function createMap(liteMode) {
		const map = Map.createView({
			top: 10,
			right: 0,
			width: Ti.UI.FILL,
			height: 200,
			liteMode: liteMode,
			region: {
				zoom: 10,
				latitude: 46.893234,
				longitude: 1.346569,
			}
		});
		const an = Map.createAnnotation({
			title: 'Title',
			latitude: 46.893234,
			longitude: 1.346569,
		});
		map.addAnnotation(an);
		win.add(map);
	}

	createMap(true);	// false for normal mode
	win.open();
};
