exports.title = 'Drawing';
exports.run = function (UI, Map) {
    var win = UI.createWindow();

    var rows = [
        {
            title: 'add poly1',
            run: function(){
                map.addPolygon(poly1);
            }
        },
        {
            title: 'remove poly1',
            run: function(){
                map.removePolygon(poly1);
            }
        },
        {
            title: 'remove all polygons',
            run: function(){
                map.removeAllPolygons();
            }
        },
        {
            title: 'add circle 2',
            run: function(){
                map.addCircle(circ2);
            }
        },
        {
            title: 'remove circle 2',
            run: function(){
                map.removeCircle(circ2);
            }
        },
        {
            title: 'remove all circles',
            run: function(){
                map.removeAllCircles();
            }
        },
        {
            title: 'add polyline',
            run: function(){
                map.addPolyline(polyline1);
            }
        },
        {
            title: 'remove polyline',
            run: function(){
                map.removePolyline(polyline1);
            }
        },
        {
            title: 'remove all polylines',
            run: function(){
                map.removeAllPolylines();
            }
        },
        {
            title: 'remove all shapes',
            run: function(){
                map.removeAllPolygons();
                map.removeAllPolylines();
                map.removeAllCircles();
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


    // == TEST POLYGONS == //

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
      strokeWidth: 1
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
      zIndex: 3 // should be on top of blue triangle on android
    }),

    // Test zIndex
    poly3 = Map.createPolygon({
      points: [
        {latitude: -33.854429, longitude: 151.214429},
        {latitude: -33.854928, longitude: 151.236101},
        {latitude: -33.866189, longitude: 151.232668}
      ],
      fillColor: "#BF5EB0DB", // 75 % opacity
      strokeColor: "#00679E",
      strokeWidth: 5,
      zIndex: 2
    });


    // == TEST CIRCLES == //
    var circ1 = Map.createCircle({
        center: { latitude: -33.87365, longitude: 151.20689 },
        radius: 1000,
        strokeWidth: 2,
        strokeColor: '#D2BE1F',
        fillColor: '#BFFFE725' // 75% opacity
    }),

    circ2 = Map.createCircle({
        center: [151.185220, -33.868997],
        radius: 250,
        strokeWidth: 1,
        strokeColor: '#AD5A00',
        fillColor: '#FF8400',
        zIndex: 2 // should be on top of circle 3 (purple) on android
    }),

    circ3 = Map.createCircle({
      center: [151.185220, -33.868997],
      radius: 500,
      strokeWidth: 1,
      strokeColor: '#52089C',
      fillColor: '#BF943BED' // purple
    });


    // == TEST POLYLINES == //

    var polyline1 = Map.createPolyline({
      points : [
        {latitude: -33.884717, longitude: 151.187993},
        [151.203099, -33.882152],
        {latitude: -33.886783, longitude: 151.218033}
      ],
      strokeColor: "#60FF0000",
      strokeWidth: 2,
      zIndex: 10
    });


    var map = Map.createView({
      userLocation: true,
      mapType: Map.NORMAL_TYPE,
      animate: true,
      polygons: [poly1, poly2, poly3],
      circles: [circ1, circ2, circ3],
      polylines: [polyline1],
      region: {latitude: -33.87365, longitude: 151.20689, latitudeDelta:0.05, longitudeDelta:0.05}, //Sydney
      top: '50%'
    });
    Ti.API.info("userLocation: " + map.userLocation);
    win.add(map);


    map.addEventListener('click', function(e) {
      Ti.API.info("Click Event: " + JSON.stringify(e));
    });

    win.open();
};
