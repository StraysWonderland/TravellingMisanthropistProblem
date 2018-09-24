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
        map.stopLocate()

        marker = new L.marker(e.latlng, {draggable : true});
        marker.addTo(map);
    } else {
        marker.setLatLng(e.latlng);
    }
    if(selectedMarker !== undefined){
        document.getElementById("rankedArticles").style.display='block';
        $("#description").text("");
        var id = selectedMarker.id
        map.removeLayer(selectedMarker)
        var newMarker;
        if(selectedArticles.has(id)){
            newMarker = L.marker([ selectedLat, selectedLon ], {icon: greenIcon})
        }else{
            newMarker = L.marker([ selectedLat, selectedLon ])
        }
        newMarker.addTo(markerGroup).bindTooltip(articles[id]['title']);
        newMarker.id = id;
        selectedMarker = undefined;
    }

    document.getElementById("headline").href = "https://www.wikipedia.de/";
    document.getElementById("thumbnail").src = "https://upload.wikimedia.org/wikipedia/commons/8/80/Wikipedia-logo-v2.svg";
});