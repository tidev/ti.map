exports.title = 'Clustering (iOS 11+)';
exports.run = function(UI, Map) {
    var win = UI.createWindow(exports.title);
    var annotations = [];
     
    for (var i = 0; i < 10; i++) {
      annotations.push(Map.createAnnotation({
        // Configure basic annotation
        title: 'Title',
        subtitle: 'subtitle',

        // Configure iOS 11+ cluster
        clusterIdentifier: 'abc',
        collisionMode: Map.ANNOTATION_VIEW_COLLISION_MODE_RECTANGLE,
        draggable: true,
        latitude: Math.random() * 10 + 40,
        longitude: Math.random() * 10,

        // Configure iOS 11+ marker-annotations
        showAsMarker: true,
        markerGlyphText: '# ' + i.toString(),
        markerColor: 'blue',
        markerGlyphColor: 'green',
        markerGlyphImage: 'icon-default.png',
        markerSelectedGlyphImage: 'icon-selected.png',
        markerTitleVisibility: Map.FEATURE_VISIBILITY_VISIBLE,
        markerSubtitleVisibility: Map.FEATURE_VISIBILITY_HIDDEN,
        annotationDisplayPriority: Map.FEATURE_DISPLAY_PRIORITY_DEFAULT_LOW
      }));
    }
     
    var mapview = Map.createView({
      annotations: annotations,
      rotateEnabled: true,
      compassEnabled: true,
      showsScale: false,
      mapType: Map.MUTED_STANDARD_TYPE,
      showsPointsOfInterest: false,
      userLocation: true,
     
    });
     
    mapview.addEventListener('clusterstart', function(e) {
      Ti.API.info('clustering started');
      
      var clusterAnnotation = Map.createAnnotation({
        customView: createMapIcon(e.memberAnnotations.length),
        title: 'Title1',
        subtitle: 'subtitle1',
      });

      mapview.setClusterAnnotation({
        annotation: clusterAnnotation,
        memberAnnotations: e.memberAnnotations
      });
    });


    function createMapIcon(number) {
      var view = Ti.UI.createView({
        height: 34,
        width: 34,
        borderRadius: 17,
        backgroundColor: 'white',
        borderColor: 'black'
      });
     
      view.add(Ti.UI.createLabel({
        text: number
      }));

      return view;
    };

    win.add(mapview);
}
