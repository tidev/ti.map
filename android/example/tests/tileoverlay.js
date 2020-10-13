exports.title = 'Tile Overlays';

const Sydney = {
    latitude: -33.87365,
    longitude: 151.20689,
    latitudeDelta: 0.1,
    longitudeDelta: 0.1
};
const Paris = {
    latitude: 48.864716,
    longitude: 2.349014,
    latitudeDelta: 0.1,
    longitudeDelta: 0.1
};
const Vienna = {
    latitude: 48.210033,
    longitude: 16.363449,
    latitudeDelta: 0.1,
    longitudeDelta: 0.1
};
const Slovakia = {
    latitude: 48.148598,
    longitude: 17.107748,
    latitudeDelta: 0.1,
    longitudeDelta: 0.1
};
const Stockholm = {
    latitude: 59.334591,
    longitude: 18.063240,
    latitudeDelta: 0.1,
    longitudeDelta: 0.1
};
const Edinburgh = {
    latitude: 55.953251,
    longitude: -3.188267,
    latitudeDelta: 0.1,
    longitudeDelta: 0.1
};
const Singapore = {
    latitude: 1.290270,
    longitude: 103.851959,
    latitudeDelta: 0.1,
    longitudeDelta: 0.1
};
const Amsterdam = {
    latitude: 52.377956,
    longitude: 4.897070,
    latitudeDelta: 0.1,
    longitudeDelta: 0.1
};
const NewYork = {
    latitude: 40.730610,
    longitude: -73.935242,
    zoom: 7 // max zoom for NASA/JusticeMap is around 8/9
};

exports.run = function(UI, Map) {
    const win = UI.createWindow(exports.title);

    const map = Map.createView({
        region: Sydney,
        top: '50%',
        enableZoomControls: true
    });

    let overlay;
    function switchOverlay(newOverlay) {
        if (overlay) {
            map.removeTileOverlay(overlay);
        }
        if (newOverlay.name.startsWith('GeoportailFrance')) {
            map.setLocation(Paris);
        } else if (newOverlay.name.startsWith('BasemapAT')) {
            map.setLocation(Vienna);
        } else if (newOverlay.name.startsWith('FreeMapSK')) {
            map.setLocation(Slovakia);
        } else if (newOverlay.name.startsWith('Hydda')) {
            map.setLocation(Stockholm);
        } else if (newOverlay.name.startsWith('NLS')) {
            map.setLocation(Edinburgh);
        } else if (newOverlay.name.startsWith('OneMapSG')) {
            map.setLocation(Singapore);
        } else if (newOverlay.name.startsWith('nlmaps')) {
            map.setLocation(Amsterdam);
        } else if (newOverlay.name.startsWith('JusticeMap') || newOverlay.name.startsWith('NASAGIBS')) {
            map.setLocation(NewYork);
        }
        overlay = newOverlay;
        map.addTileOverlay(overlay);
    }

    const DB = Map.createLeafletProviders();
    const names = DB.getAllNames();
    // FIXME: Sort ignoring case
    const rows = names.sort().map(name => {
        return {
            title: name,
            color: 'black',
            run: () => {
                // TODO: Can we get the merged max/min zoom levels for the given provider to determine zoom level?
                switchOverlay(Map.createTileOverlay({
                    service: Map.TILE_OVERLAY_TYPE_XYZ,
                    name,
                    debuglevel: 2
                }));
            }
        };
    });

    const tableView = Ti.UI.createTableView({
        top: 0,
        bottom: '50%',
        data: rows
    });
    win.add(tableView);
    tableView.addEventListener('click', function(e) {
        rows[e.index].run && rows[e.index].run();
    });

    win.add(map);
};
