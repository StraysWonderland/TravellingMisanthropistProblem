var polyline;

var markerCount = 0;
var maxMarkers = 10;

var pathMarker1;
var pathMarker2;
var pathMarker3;

var markerGroup = L.featureGroup().addTo(map);

var targetIndex;
var linecolor = '#2823dd';
var sampleMessage;

var markerGroup = L.featureGroup().addTo(map);

// foursquare api properties
var numberOfRetrievedPOIS;
var nearbyVenues = [];

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
    var marker = new L.marker(e.latlng, {draggable: true});
    map.addLayer(marker);
});


function CalculateSamplePath(e) {
    var startNodeCoords = [pathMarker1.getLatLng().lat, pathMarker1.getLatLng().lng];
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
            if (polyline != undefined) {
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
    lat = pathMarker1.getLatLng().lat;
    lng = pathMarker1.getLatLng().lng;

    $.ajax({
        type: "GET",
        url: 'https://api.foursquare.com/v2/venues/explore?client_id=NBCYTRL4YF5U05GCVWPFMEDRVLGKMHFHOPWKYEHUVLR2DPAM&client_secret=TSO0EFXRC0ILJ04GYX1T5KWHPWQETT3MB2UTSLV005LUONHK&v=20180323&limit=25&ll=' + lat + "," + lng + '&query=coffee',
        async: true,
        dataType: 'jsonp',
        success: function (data) {
            alert(data);

            numberOfRetrievedPOIS = data.response.groups[0].items.length;
            var foundItems = data.response.groups[0].items;

            for (var i = 0; i < numberOfRetrievedPOIS; i++) {
                nearbyVenues.push(foundItems[i].venue);
            }


            for (var i = 0; i < numberOfRetrievedPOIS; i++) {
                var venue = nearbyVenues[i];
                var lat = venue.location.lat;
                var lng = venue.location.lng;

                var marker = L.marker([lat, lng], {icon: redIcon}, {Tooltip: venue.name});

                marker.addTo(markerGroup);
                marker.bindPopup(venue.name + " " + venue.location.formattedAddress);
                marker.id = venue.id;
                map.addLayer(marker);
            }
            /*  for (i = 0; i < numberOfRetrievedPOIS; i++) {
                  var coords = foundItems[i];
                  if (coords != undefined) {
                      var marker = L.marker([ coords.lat, coords.lon ])
                      marker.addTo(markerGroup).bindTooltip(articles[articleID]['title']);
                      marker.id = articleID;
                      markers[articleID] = marker;
                  }else{
                      console.log(articleID)
                  }
              }*/
            console.log(data.response.groups[0].items);
        }
    });

    console.log(" GET POIs Called");
}


function generateRoundTripBetweenMarkers(e) {
    var markerlats = [];
    var markerlngs = [];

    for (var i = 0; i < 10; i++) {
        var venue = nearbyVenues[i];
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
            if (polyline != undefined) {
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

/* CONTEXT MENU */
/* VARIABLES */
var menu = document.querySelector("#context-menu");
var menuState = 0;
var active = "context-menu--active";

/* CONTEXT MENU FUNCTION */
(function () {

    "use strict";

    var taskItems = document.querySelectorAll(".task");

    for (var i = 0, len = taskItems.length; i < len; i++) {
        var taskItem = taskItems[i];
        contextMenuListener(taskItem);
    }

    function contextMenuListener(el) {
        el.addEventListener("contextmenu", function (e) {
            e.preventDefault();
            toggleMenuOn();
        });
    }

    function toggleMenuOn() {
        if (menuState !== 1) {
            menuState = 1;
            menu.classList.add(active);
        }
    }
})();

map.on('click', function (e) {

    if (typeof (pathMarker1) === 'undefined') {
        map.stopLocate();
        pathMarker1 = new L.marker(e.latlng, {draggable: true});
        pathMarker1.addTo(map);

    } else if (typeof(pathMarker2) === 'undefined') {
        pathMarker2 = new L.marker(e.latlng, {draggable: true});
        pathMarker2.addTo(map);
    } else if (typeof(pathMarker3) === 'undefined') {
        pathMarker3 = new L.marker(e.latlng, {draggable: true});
        pathMarker3.addTo(map);
    } else {
        pathMarker3 = new L.marker(e.latlng, {draggable: true});
    }
});