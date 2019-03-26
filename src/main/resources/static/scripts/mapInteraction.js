var polyline;

var targetIndex;
var linecolor = '#2823dd';
var sampleMessage;

var locationMarker;

var markerGroup = L.featureGroup().addTo(map);
markerGroup.on("contextmenu", groupRightClick);
// foursquare api properties
var numberOfRetrievedPOIS;
var nearbyVenues = [];
var selectedVenues = [];


var redIcon = new L.Icon({
    iconUrl: 'https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-red.png',
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41]
});

var greenIcon = new L.Icon({
    iconUrl: 'https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-green.png',
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41]
});

map.locate({setView: true}).on('locationfound', function (e) {
    locationMarker = new L.marker(e.latlng, {draggable: true}).addTo(map);
});

$("#map").bind('contextmenu', function (e) {
    return false;
});


function CalculateSamplePath(e) {
    var startNodeCoords = [locationMarker.getLatLng().lat, locationMarker.getLatLng().lng];
    var targetNodeCoords = [pathMarker2.getLatLng().lat, pathMarker2.getLatLng().lng];

    var urlString = "/shortestPathFromTo/" + startNodeCoords + "/" + targetNodeCoords;
    $.ajax({
        type: "GET",
        url: urlString,
        timeout: 20000,
        success: function (response) {
            var latlngs = response.split(",").map(function (e) {
                return e.split("_").map(Number);
            });
            if (polyline !== undefined) {
                map.removeLayer(polyline)
            }
            polyline = L.polyline(latlngs, {
                color: linecolor
            }).addTo(map);
            map.fitBounds(polyline.getBounds());
        },
        error: function () {
            sampleMessage = "target Index failed"
        }
    });
}

function GetPOIsInRangeFunction(e) {
    var lat = locationMarker.getLatLng().lat;
    var lng = locationMarker.getLatLng().lng;
    console.log(" GET POIs Called on lat: " + lat + " lng: " + lng);

    var clientID = "NBCYTRL4YF5U05GCVWPFMEDRVLGKMHFHOPWKYEHUVLR2DPAM";
    var clientSecret = "TSO0EFXRC0ILJ04GYX1T5KWHPWQETT3MB2UTSLV005LUONHK";
    var venueLimit = 25;  // limit is given to safe money, since number of free calls is limited. lel
    var radius = 1500;
    // Spielhalle, Weihnacthsmarkt, Nachtleben, Brennerei, Volksfest, Biershop, Gamer Cafee
    var categories = "4bf58dd8d48988d1e1931735,52f2ab2ebcbc57f1066b8b3b,4d4b7105d754a06376d81259,4e0e22f5a56208c4ea9a85a0,4eb1daf44b900d56c88a4600,5370f356bcbc57f1066c94c2,4bf58dd8d48988d18d941735";
    $.ajax({
        type: "GET",
        url: 'https://api.foursquare.com/v2/venues/explore?client_id=' + clientID +
        '&client_secret=' + clientSecret +
        '&v=20180323' +
        '&limit=' + venueLimit +
        '&radius=' + radius +
        '&ll=' + lat + "," + lng +
        '&categoryId=' + categories,
        async: true,
        dataType: 'jsonp',
        success: function (data) {

            numberOfRetrievedPOIS = data.response.groups[0].items.length;
            var foundItems = data.response.groups[0].items;

            reset();

            for (var i = 0; i < numberOfRetrievedPOIS; i++) {
                nearbyVenues.push(foundItems[i].venue);
            }

            for (var i = 0; i < numberOfRetrievedPOIS; i++) {
                var venue = nearbyVenues[i];
                var lat = venue.location.lat;
                var lng = venue.location.lng;

                var marker = new L.marker([lat, lng], {icon: redIcon}, {tooltip: venue.name});

                marker.addTo(markerGroup);
                marker.bindPopup(venue.name + "<br>" + venue.location.formattedAddress);
                marker.id = i;
                map.addLayer(marker);
            }
            console.log(nearbyVenues);
        }
    });
}


function generateRoundTripBetweenMarkers(e) {
    var markerlats = [];
    var markerlngs = [];

    markerlats.push(locationMarker.getLatLng().lat);
    markerlngs.push(locationMarker.getLatLng().lng);

    for (var i = 0; i < selectedVenues.length; i++) {
        var venue = selectedVenues[i];
        markerlats.push(venue.location.lat);
        markerlngs.push(venue.location.lng);
    }

    var urlString = "/generateRoundtrip/" + markerlats + "/" + markerlngs;
    $.ajax({
        type: "GET",
        url: urlString,
        timeout: 20000,
        success: function (response) {
            var latlngs = response.split(",").map(function (e) {
                return e.split("_").map(Number);
            });
            if (polyline !== undefined) {
                map.removeLayer(polyline)
            }
            polyline = L.polyline(latlngs, {
                color: linecolor
            }).addTo(map);
            map.fitBounds(polyline.getBounds());

        },
        error: function () {
            sampleMessage = "target Index failed";
            console.log(sampleMessage)
        }
    });
}

function groupRightClick(event) {
    var marker = event.layer;
    var id = event.layer.id;

    marker.setIcon(greenIcon);
    /*    map.removeLayer(marker);
        var coords = nearbyVenues[id].getLatLng();
        var newMarker = L.marker([coords.lat, coords.lng]);
        newMarker.addTo(markerGroup);
        newMarker.id = id;*/

    var selectedVenue = nearbyVenues[id];
    selectedVenues.push(selectedVenue);

    if (selectedVenues.length > 1 && selectedVenues.length < 23) {
        document.getElementById("calcRountTrip").style.visibility = "visible";
    } else {
        document.getElementById("calcRountTrip").style.visibility = "hidden";
    }


}

map.on('click', function (e) {
    if (typeof (locationMarker) === 'undefined') {
        map.stopLocate();
        locationMarker = new L.marker(e.latlng, {draggable: true});
        locationMarker.addTo(map);

    } else {
        locationMarker.setLatLng(e.latlng).update();
    }
});

function reset() {
    markerGroup.clearLayers();
    nearbyVenues = [];
    selectedVenues = [];
    if (polyline !== undefined) {
        map.removeLayer(polyline)
    }
}