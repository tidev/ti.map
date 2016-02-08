
exports.title = 'Annotations';
exports.run = function(UI, Map) {
    var window = UI.createWindow(),
        annotations = [];

    var mapView = Map.createView({
        clusterCellSizeFactor: 0.5,
        clusterTintColor: "red",
        clusterTextColor: "white",
    });

    for(var i = 1; i <= 10000;i++) {
        annotations.push(Map.createAnnotation({
            latitude: 52.695348 +i,
            longitude: 7.299401 + Math.sin(i),
            title: "Annotation " + i
        }));
    }

    mapView.setAnnotations(annotations);
    mapView.cluster();

    window.add(mapView);
    window.open();
}
