
exports.title = 'Camera';
exports.run = function(UI, Map) {
    var win = UI.createWindow();
    
    var rows = [
        {
            title: 'Set Camera',
            run: function(){
                map.camera = Map.createCamera({
                    centerCoordinate: {
                        latitude: -33.87365, longitude: 151.20689
                    },
                    altitude: 3000,
                    pitch: 40
                }); 
            }
        },
        {
            title: 'Zoom out',
            run: function(){
                // Camera will not exist if run on pre iOS 7
                if (map.camera) {
                    map.camera.altitude = map.camera.altitude*2;
                }
            }
        },
        {
            title: 'Go to Melbourne animated',
            run: function(){
                
                map.animateCamera({
                    camera: midpointCam,
                    duration: 1500
                }, function() {
                    // This will be run once the first animation completes.
                    map.animateCamera({
                        camera: melbourneCam,
                        duration: 1500
                    });
                });
            }
        },
        {
            title: 'Go to Sydney animated',
            run: function(){
                map.animateCamera({
                    camera: midpointCam,
                    duration: 1500
                }, function() {
                    // This will be run once the first animation completes.
                    map.animateCamera({
                        camera: sydneyCam,
                        duration: 1500
                    });
                });
            }
        }
    ];
    
    // Location Cameras
    var sydneyCam = Map.createCamera({
        altitude: 244, 
        centerCoordinate: {
            longitude: 151.2152523799932,
            latitude: -33.85666173702788
        }, 
        heading: -131.1528177444374, 
        pitch: 61.2794189453125
    });
    
    var melbourneCam = Map.createCamera({
        altitude: 14877, centerCoordinate: {
            longitude: 144.95771473524832,
            latitude: -37.82064895691708
        }, 
        heading: 0, 
        pitch: 0
    });
    
    // The midpoint between Sydney and Melbourne will make the animation look nicer.
    var midpointCam = Map.createCamera({
        altitude: 1074069, 
        centerCoordinate: {
            longitude: 148.01178350216657,
            latitude: -36.048428214025066
        }, 
        heading: 0, 
        pitch: 0
    });
    
    // Table View
    var tableView = Ti.UI.createTableView({
        top: '10%',
        bottom: '50%',
        data: rows
    });
    win.add(tableView);
    tableView.addEventListener('click', function(e) {
        rows[e.index].run && rows[e.index].run();
    });
    
    // Map
    var map = Map.createView({
        userLocation: true,
        mapType: Map.NORMAL_TYPE,
        animate: true,
        region: {latitude: -33.87365, longitude: 151.20689, latitudeDelta: 0.02, longitudeDelta: 0.02 }, //Sydney
        top: '50%'
    });
    win.add(map);
    
    // Check camera properties on location change to find the current values
    map.addEventListener('regionchanged', function(e) {
        // We don't want to know the location when the change was animated, we told it where to animate to.
        var cam;
        if (!e.animated && (cam = map.camera)) {
            Ti.API.info('Camera Properties --> {altitude: '+cam.altitude+
            ', centerCoordinate: '+JSON.stringify(cam.centerCoordinate)+
            ', heading: '+cam.heading+
            ', pitch: '+cam.pitch+
            '}');
        }
    });
    
    win.open();
}
