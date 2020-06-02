exports.title = 'Annotations';
exports.run = function(UI, Map) {
    var win = UI.createWindow(exports.title);

    var rows = [{
            hasChild: true,
            title: 'Go Mt. View',
            run: function() {
                map.region = {
                    latitude: 37.3689,
                    longitude: -122.0353,
                    latitudeDelta: 0.1,
                    longitudeDelta: 0.1
                }; //Mountain View
            }
        },
        {
            hasChild: true,
            title: 'animate annotation',
            run: function() {
                var newLocation = [-33.09,151.03];
                anno2.animate(newLocation);
            }
        },
        {
            hasChild: true,
            title: 'rotate annotation',
            run: function() {
                var angle = 90;
                anno4.rotate(angle);
            }
        },
        {
            hasChild: true,
            title: 'add anno3',
            run: function() {
                map.addAnnotation(anno3);
            }
        },
        {
            hasChild: true,
            title: 'rm anno3',
            run: function() {
                map.removeAnnotation(anno3);
            }
        },
        {
            hasChild: true,
            title: 'add anno1, 2, 4',
            run: function() {
                map.annotations = [anno, anno2, anno4];
            }
        },
        {
            hasChild: true,
            title: 'rm all',
            run: function() {
                map.removeAllAnnotations();
            }
        },
        {
            hasChild: true,
            title: 'remove annos: Sydney, anno2',
            run: function() {
                Ti.API.info(anno.getTitle());
                map.removeAnnotations(['Sydney', anno2]);
            }
        },
        {
            hasChild: true,
            title: 'select anno2',
            run: function() {
                map.selectAnnotation(anno2);
            }
        },
        {
            hasChild: true,
            title: 'desel anno2',
            run: function() {
                map.deselectAnnotation(anno2);
            }
        },
        {
            hasChild: true,
            title: 'modify anno2',
            run: function() {
                anno2.title = 'Hello';
                anno2.subtitle = 'Hi there.';
                anno2.longitude = 151.27689;
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
    var anno = Map.createAnnotation({
        latitude: -33.87365,
        image: 'map_pin.png',
        longitude: 151.20689,
        title: 'Sydney',
        subtitle: 'Sydney is quite chill',
        draggable: true
    });
    var anno2 = Map.createAnnotation({
        latitude: -33.86365,
        pincolor: Map.ANNOTATION_GREEN,
        longitude: 151.21689,
        title: 'Anno2',
        subtitle: 'This is anno2',
        draggable: true
    });
    var anno3 = Map.createAnnotation({
        latitude: -33.85365,
        longitude: 151.20689,
        title: 'Anno3',
        subtitle: 'This is anno3',
        draggable: false
    });
    var anno4 = Map.createAnnotation({
        latitude: -33.86365,
        longitude: 151.22689,
        title: 'Anno4',
        subtitle: 'This is anno4',
        draggable: true
    });
    Ti.API.info('Latitude:' + anno.latitude);
    Ti.API.info('Title:' + anno.title);

    var map = Map.createView({
        userLocation: true,
        mapType: Map.NORMAL_TYPE,
        animate: true,
        annotations: [anno, anno2, anno4],
        region: {
            latitude: -33.86365,
            longitude: 151.21689,
            latitudeDelta: 0.1,
            longitudeDelta: 0.1
        }, //Sydney
        top: '50%'
    });

    Ti.API.info('userLocation: ' + map.userLocation);

    map.addEventListener('click', function(e) {
        Ti.API.info('Latitude: ' + e.latitude);
        Ti.API.info('Source: ' + e.clicksource);
    });

    win.add(map);
}
