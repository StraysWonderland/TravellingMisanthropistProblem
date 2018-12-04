var marker;
var markers = {};
var markerDest = L.marker([48.77391, 9.17408]).addTo(map);
var selectedMarker;
var selectedLat;
var selectedLon;
var latlngs = [
    [17.385044, 78.486671],
    [16.506174, 80.648015]
];
var path = L.polyline(latlngs);

map.locate({setView: true}).on('locationfound', function (e) {
    marker = new L.marker(e.latlng, {draggable: true});
    map.addLayer(marker);
});

function PathFunction(e) {
    map.removeLayer(path);
    var pointList = [marker.getLatLng(), markerDest.getLatLng()];
    path = L.polyline(pointList);
    path.addTo(map);
}

function GetPOIsInRangeFunction(e) {

}


map.on('click', function(e) {

    if (typeof (marker) === 'undefined') {
        map.stopLocate();
        marker = new L.marker(e.latlng, {draggable : false});
        marker.addTo(map);
    } else {
        marker.setLatLng(e.latlng);
    }
});