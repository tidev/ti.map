exports.title = 'Properties';
exports.run = function(UI, Map) {
    var win = UI.createWindow(exports.title);

    var rows = [{
            hasChild: true,
            title: 'pitchEnabled: true',
            run: function(e) {
                map.pitchEnabled = !map.pitchEnabled;
                // Display the current value of the property in the row
                tableView.data[0].rows[e.index].title = rows[e.index].title.split(':')[0] + ': ' + map.pitchEnabled;
            }
        },
        {
            hasChild: true,
            title: 'rotateEnabled: true',
            run: function(e) {
                map.rotateEnabled = !map.rotateEnabled;
                // Display the current value of the property in the row
                tableView.data[0].rows[e.index].title = rows[e.index].title.split(':')[0] + ': ' + map.rotateEnabled;
            }
        },
        {
            hasChild: true,
            title: 'showsBuildings: true',
            run: function(e) {
                map.showsBuildings = !map.showsBuildings;
                // Display the current value of the property in the row
                tableView.data[0].rows[e.index].title = rows[e.index].title.split(':')[0] + ': ' + map.showsBuildings;
            }
        },
        {
            hasChild: true,
            title: 'showsPointsOfInterest: true',
            run: function(e) {
                map.showsPointsOfInterest = !map.showsPointsOfInterest;
                // Display the current value of the property in the row
                tableView.data[0].rows[e.index].title = rows[e.index].title.split(':')[0] + ': ' + map.showsPointsOfInterest;
            }
        },
    ];

    // Table View
    var tableView = Ti.UI.createTableView({
        top: 0,
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
        region: {
            latitude: -33.87365,
            longitude: 151.20689,
            latitudeDelta: 0.1,
            longitudeDelta: 0.1
        },
        top: '50%',
        pitchEnabled: true,
        rotateEnabled: true,
        showsBuildings: true,
        showsPointsOfInterest: true
    });

    win.add(map);
}
