
exports.title = 'Annotations';
exports.run = function(UI, Map) {
    var win = UI.createWindow();
    
    var rows = [
        {
            title: 'Go Mt. View',
            run: function(){
                map.region = {latitude: 37.3689, longitude: -122.0353, latitudeDelta: 0.1, longitudeDelta: 0.1 }; //Mountain View
            }
        },
        {
            title: 'add anno3',
            run: function(){
                map.addAnnotation(anno3);
            }
        },
        {
            title: 'rm anno3',
            run: function(){
                map.removeAnnotation(anno3);
            }
        },
        {
            title: 'add anno1, 2, 4',
            run: function(){
                map.annotations = [anno, anno2, anno4];
            }
        },
        {
            title: 'rm all',
            run: function(){
                map.removeAllAnnotations();
            }
        },
        {
            title: 'remove annos: Sydney, anno2',
            run: function(){
                Ti.API.info(anno.getTitle());
                map.removeAnnotations(["Sydney", anno2]);
            }
        },
        {
            title: 'select anno2',
            run: function(){
                map.selectAnnotation(anno2);
            }
        },
        {
            title: 'desel anno2',
            run: function(){
                map.deselectAnnotation(anno2);
            }
        },
        {
            title: 'modify anno2',
            run: function(){
                anno2.title = "Hello";
                anno2.subtitle = "Hi there.";
                anno2.longitude = 151.27689;
            }
        }
    ];
    
    var tableView = Ti.UI.createTableView({
        top: '10%',
        bottom: '50%',
        data: rows
    });
    win.add(tableView);
    tableView.addEventListener('click', function(e) {
        rows[e.index].run && rows[e.index].run();
    });
    
    var anno = Map.createAnnotation({latitude: -33.87365, image: 'map_pin.png', longitude: 151.20689, title: "Sydney", subtitle: "Sydney is quite chill", draggable: true});
    var anno2 = Map.createAnnotation({latitude: -33.86365, pincolor: Map.ANNOTATION_GREEN, longitude: 151.21689, title: "Anno2", subtitle: "This is anno2", draggable: true});
    var anno3 = Map.createAnnotation({latitude: -33.85365, longitude: 151.20689, title: "Anno3", subtitle: "This is anno3", draggable: false});
    var anno4 = Map.createAnnotation({latitude: -33.86365, longitude: 151.22689, title: "Anno4", subtitle: "This is anno4", draggable: true});
    
    Ti.API.info ("Latitude:" + anno.latitude);
    Ti.API.info ("Title:" + anno.title);
    
    var map = Map.createView({
        userLocation: true,
        mapType: Map.NORMAL_TYPE,
        animate: true,
        annotations: [anno, anno2, anno4],
        region: {latitude: -33.87365, longitude: 151.20689, latitudeDelta: 0.1, longitudeDelta: 0.1 }, //Sydney
        top: '50%'
    });
    Ti.API.info("userLocation: " + map.userLocation);
    win.add(map);
    
    map.addEventListener('click', function(e) {
        Ti.API.info("Latitude: " + e.latitude);
        Ti.API.info("Source: " + e.clicksource);
    });
    
    win.open();
}
