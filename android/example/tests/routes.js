
exports.title = 'Routes';
exports.run = function(UI, Map) {
    var win = UI.createWindow();
    var w = 5.0;
    
    var rows = [
        {
            title: '+1 width',
            run: function(){
                // iOS Note: changing the width of an existing route will only work on iOS 7+ 
                route.width = w + 1.0;
                w = w + 1.0;
            }
        },
        {
            title: 'change color',
            run: function(){
                // iOS Note: changing the color of an existing route will only work on iOS 7+ 
                route.color = 'red';
            }
        },
        {
            title: 'change routes',
            run: function(){
                map.removeRoute(route);
                route = Map.createRoute({
                    points: route2
                });
                map.addRoute(route);
            }
        },
        {
            title: 'remove route',
            run: function(){
                map.removeRoute(route);
            }
        },
        {
            title: 'add route',
            run: function(){
                map.addRoute(route);
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
    
    var map = Map.createView({
        userLocation: true,
        mapType: Map.NORMAL_TYPE,
        animate: true,
        region: {latitude: -33.87365, longitude: 151.20689, latitudeDelta: 0.02, longitudeDelta: 0.02 }, //Sydney
        top: '50%'
    });
    win.add(map);
    
    var route1 = [{latitude: -33.87365, longitude: 151.20689}, {latitude: -33.87469, longitude: 151.20689}, {latitude: -33.87375, longitude: 151.20589}];
    var route2 = [{latitude: -33.87565, longitude: 151.20789}, {latitude: -33.87469, longitude: 151.20689}, {latitude: -33.86375, longitude: 151.20589}];
    
    var route = Map.createRoute({
        points: route1,
        color: 'blue',
        width: 5.0
    });
    
    map.addRoute(route);
    
    win.open();
}
