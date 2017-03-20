exports.title = 'Routes';
exports.run = function(UI, Map) {
    var win = UI.createWindow(exports.title);
    var w = 5.0;

    var rows = [{
            hasChild: true,
            title: '+1 width',
            run: function() {
                route.width = w + 1.0;
                w = route.width;
            }
        },
        {
            hasChild: true,
            title: 'change color',
            run: function() {
                route.color = 'red';
            }
        },
        {
            hasChild: true,
            title: 'change routes',
            run: function() {
                map.removeRoute(route);
                w = 5.0;
                route = Map.createRoute({
                    points: route2
                });
                map.addRoute(route);
            }
        },
        {
            hasChild: true,
            title: 'remove route',
            run: function() {
                map.removeRoute(route);
            }
        },
        {
            hasChild: true,
            title: 'add route',
            run: function() {
                map.addRoute(route);
            }
        }
    ];

    var tableView = Ti.UI.createTableView({
        top: 0,
        bottom: '50%',
        data: rows
    });

    win.add(tableView);

    tableView.addEventListener('click', function(e) {
        rows[e.index].run && rows[e.index].run();
    });

    var map = Map.createView({
        userLocation: true,
        mapType: Map.NORMAL_TYPE,
        animate: true,
        region: {
            latitude: -33.87365,
            longitude: 151.20689,
            latitudeDelta: 0.1,
            longitudeDelta: 0.1
        },
        top: '50%'
    });

    win.add(map);

    var route1 = [{
        latitude: -33.87365,
        longitude: 151.20689
    }, {
        latitude: -33.87469,
        longitude: 151.20689
    }, {
        latitude: -33.87375,
        longitude: 151.20589
    }];
    var route2 = [{
        latitude: -33.87565,
        longitude: 151.20789
    }, {
        latitude: -33.87469,
        longitude: 151.20689
    }, {
        latitude: -33.86375,
        longitude: 151.20589
    }];

    var route = Map.createRoute({
        points: route1,
        color: 'blue',
        width: 5.0
    });

    map.addRoute(route);
}
