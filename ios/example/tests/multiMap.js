exports.title = 'Multi Maps';
exports.run = function(UI, Map) {
    var win = UI.createWindow(exports.title);

    var map1 = Map.createView({
        userLocation: true,
        mapType: Map.NORMAL_TYPE,
        animate: true,
        region: {
            latitude: -33.87365,
            longitude: 151.20689,
            latitudeDelta: 0.1,
            longitudeDelta: 0.1
        },
        height: '50%',
        top: 0,
        left: 0,
        width: '50%'
    });
    var map2 = Map.createView({
        userLocation: true,
        mapType: Map.SATELLITE_TYPE,
        animate: true,
        region: {
            latitude: -33.87365,
            longitude: 151.20689,
            latitudeDelta: 0.1,
            longitudeDelta: 0.1
        },
        height: '50%',
        top: 0,
        right: 0,
        width: '50%'
    });
    var map3 = Map.createView({
        userLocation: true,
        mapType: Map.HYBRID_TYPE,
        animate: true,
        region: {
            latitude: -33.87365,
            longitude: 151.20689,
            latitudeDelta: 0.1,
            longitudeDelta: 0.1
        },
        height: '50%',
        bottom: 0,
        left: 0,
        width: '50%'
    });
    var map4 = Map.createView({
        userLocation: true,
        mapType: Map.TERRAIN_TYPE, // TERRAIN_TYPE is not supported by iOS, but it doesn't hurt to set it
        animate: true,
        region: {
            latitude: -33.87365,
            longitude: 151.20689,
            latitudeDelta: 0.1,
            longitudeDelta: 0.1
        },
        height: '50%',
        bottom: 0,
        right: 0,
        width: '50%'
    });

    win.add(map1);
    win.add(map2);
    win.add(map3);
    win.add(map4);
}
