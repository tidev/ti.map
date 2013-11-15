
exports.title = 'Properties';
exports.run = function(UI, Map) {
    var win = UI.createWindow();
    
    var rows = [
        {
            title: 'pitchEnabled: true',
            run: function(e){
                map.pitchEnabled = !map.pitchEnabled;
                // Display the current value of the property in the row
                tableView.data[0].rows[e.index].title = rows[e.index].title.split(":")[0] + ": " + map.pitchEnabled;
            }
        },
        {
            title: 'rotateEnabled: true',
            run: function(e){
                map.rotateEnabled = !map.rotateEnabled;
                // Display the current value of the property in the row
                tableView.data[0].rows[e.index].title = rows[e.index].title.split(":")[0] + ": " + map.rotateEnabled;
            }
        },
        {
            title: 'showsBuildings: true',
            run: function(e){
                map.showsBuildings = !map.showsBuildings;
                // Display the current value of the property in the row
                tableView.data[0].rows[e.index].title = rows[e.index].title.split(":")[0] + ": " + map.showsBuildings;
            }
        },
        {
            title: 'showsPointsOfInterest: true',
            run: function(e){
                map.showsPointsOfInterest = !map.showsPointsOfInterest;
                // Display the current value of the property in the row
                tableView.data[0].rows[e.index].title = rows[e.index].title.split(":")[0] + ": " + map.showsPointsOfInterest;
            }
        },
    ];
    
    // Table View
    var tableView = Ti.UI.createTableView({
        top: '10%',
        bottom: '50%',
        data: rows
    });
    win.add(tableView);
    tableView.addEventListener('click', function(e) {
        rows[e.index].run && rows[e.index].run(e);
    });
    
    // Map
    var map = Map.createView({
        userLocation: true,
        mapType: Map.NORMAL_TYPE,
        animate: true,
        region: {latitude: -33.87365, longitude: 151.20689, latitudeDelta: 0.02, longitudeDelta: 0.02 }, //Sydney
        top: '50%',
        pitchEnabled: true,
        rotateEnabled: true,
        showsBuildings: true,
        showsPointsOfInterest: true
    });
    win.add(map);
    
    win.open();
}
