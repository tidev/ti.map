exports.title = 'Polygons';

exports.run = function(UI, Map) {
    var win = UI.createWindow(exports.title);

    var rows = [
        {
            title: 'add poly1',
            run: function(){
                map.addPolygon(poly1);
            }
        },
        {
            title: 'rm poly1',
            run: function(){
                map.removePolygon(poly1);
            }
        },
        {
            title: 'rm all polygons',
            run: function(){
                map.removeAllPolygons();
            }
        },
        {
            title: 'add circle',
            run: function(){
                map.addCircle(circle);
            }
        },
        {
            title: 'rm circle',
            run: function(){
                map.removeCircle(circle);
            }
        },
        {
            title: 'add polyline',
            run: function(){
                map.addPolyline(polyline);
            }
        },
        {
            title: 'rm polyline',
            run: function(){
                map.removePolyline(polyline);
            }
        },
        {
            title: 'rm all shapes',
            run: function(){
                map.removeAllPolygons();
                map.removeAllPolylines();
                map.removeAllCircles();
            }
        },
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

    // Test points, holes, opacity, stroke width
    var poly1 = Map.createPolygon({
      points: [
        {latitude: -33.855534, longitude: 151.200266},
        {latitude: -33.859098, longitude: 151.230994},
        {latitude: -33.877698, longitude: 151.225072},
        {latitude: -33.875418, longitude: 151.201554}
      ],
      holes: [
        [
          {latitude: -33.870002, longitude: 151.210395},
          [151.211939, -33.869503], // test array is valid
          {latitude: -33.865940, longitude: 151.212540},
          {latitude: -33.865084, longitude: 151.211682},
          {latitude: -33.866439, longitude: 151.210738}
        ],
        [
          {latitude: -33.858652, longitude: 151.204429},
          {latitude: -33.858946, longitude: 151.205803},
          {latitude: -33.860095, longitude: 151.205298},
          {latitude: -33.860487, longitude: 151.204097},
          {latitude: -33.859133, longitude: 151.204054}
        ]
      ],
      fillColor: "rgba(237,5,42,75)",
      strokeColor: "#912911",
      strokeWidth: 10
    }),

    // Test counter-clockwise point definition, zIndex, points array
    poly2 = Map.createPolygon({
      points: [
        [151.228290, -33.857280],
        [151.224428, -33.855427],
        [151.224170, -33.858991]
      ],
      fillColor: "#F2FA0C",
      strokeColor: "#D4D93F",
      strokeWidth: 5,
      zIndex: 3
    }),

    // Test zIndex
    poly3 = Map.createPolygon({
      points: [
        {latitude: -33.854429, longitude: 151.214429},
        {latitude: -33.854928, longitude: 151.236101},
        {latitude: -33.866189, longitude: 151.232668}
      ],
      fillColor: "#5EB0DB",
      strokeColor: "#00679E",
      strokeWidth: 5,
      zIndex: 2
    });


    var map = Map.createView({
        userLocation: true,
        mapType: Map.NORMAL_TYPE,
        animate: true,
        polygons: [poly1, poly2, poly3],
        region: {latitude: -33.87365, longitude: 151.20689, latitudeDelta:0.05, longitudeDelta:0.05}, //Sydney
        top: '50%'
    });
    Ti.API.info("userLocation: " + map.userLocation);
    win.add(map);

    var polyline = Map.createPolyline({
        points : [
          {latitude: -33.854419, longitude: 151.214429},
          [151.224428, -33.855427],
          {latitude: -33.877698, longitude: 151.225072}
        ],
        color : "#60FF0000",
        width : 5.0,
        zIndex : 10
    });
    map.addPolyline(polyline);

    var circle = Map.createCircle({
        center : { latitude: -33.87365, longitude: 151.20689 },
        radius : 1000,
        borderWidth : '2dp',
        borderColor : '#40D2BE1F',
        backgroundColor : '#20FFE725',
        opacity : 0.3,
        zIndex: 0
    });
    map.addCircle(circle);

    map.addEventListener('click', function(e) {
        Ti.API.info("Source: " + e.clicksource + ", Latitude: " + e.latitude + " Longitude: " + e.longitude);
    });
};
