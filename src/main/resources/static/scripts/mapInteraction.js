var marker = L.marker();
var markerDest = L.marker([48.77391, 9.17408]).addTo(map);
var latlngs = [
    [17.385044, 78.486671],
    [16.506174, 80.648015]
];
var path = L.polyline(latlngs);

map.locate({setView: true}).on('locationfound', function (e) {
    marker = new L.marker(e.latlng, {draggable: true});
    map.addLayer(marker);
});

map.on('click', function (e) {
    map.removeLayer(path);
    marker.setLatLng(e.latlng).addTo(map);
});

function PathFunction(e) {
    var pointList = [marker.getLatLng(), markerDest.getLatLng()];
    path = L.polyline(pointList);
    path.addTo(map);
}
