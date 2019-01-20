# TravellingMisanthropistProblem

Web application that, given a location, computes a round trip along nearby amenities based on a specified ranking.

## Implementation Details
- Java for Backend
- Leaflet for map visualisation
- Javascript for map interaction
- Java Springboot to bundle into web-application

- Map-Data from OpenStreetMaps

# HOW TO START THE PROJECT:
## Method 1: IDE
open the "TravelingMisanthropistProblem" Folder in IntelliJ-Idea and Run it.
## Method 2: JAR
- open the "TravelingMisanthropistProblem" Folder in Explorer,
- Open cmd window in that Folder,
- type and run following command:

    ./mvnw spring-boot:run 


# Using the Project
once the project has started,
open a browser and navigate to "http://localhost:8080/"


Place markers by left-clicking anywhere on the map.
Currently, you can place only 3 markers and only Baden-Württemberg is supported.
Move existing markers by dragging.
Once youve placed your markers, select one of the two buttons on the sidebar.

### calculate Path:
calculates the shortest path between the first two markers

### calculate RoundTrip
calculates a TSP-Solution between the three markers


## Still Missing in Current Project:
- Parse Germany.Pbf
- Retrieve Amenities near current location
- Calculate TSP between all selected Markers (more than 3 ofc.)

# Additional Notes:
- The project contains resource files to handle Baden-Würrtemberg.
- Germany will be handled at a later point, since when running germany in the ide, the java-heapspace needs to be allocated to more than 4GB.


# Project Goal:
Retrieve amenities nearby.
Calculate TSP for selected amenities.
OR
Calculate TSP by selecting Bars with lowest rating across all possible closeby bars.