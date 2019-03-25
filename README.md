# TravellingMisanthropistProblem


Web application that, given a location, computes a round trip along nearby amenities based on a specified ranking.

## PARSER
For parsing refer to following git repository:
   https://github.com/StraysWonderland/TMP_Parser
   
To run parser, place a pbf file in the root directory and name it "target.osm.pbf"
Run the jar file preferably by running the command:
   >  java  -jar  -Xmx=16G TMPParser.jar
   
Alternatively, launch intelliJ and run the application ( set jav heapspace accordingly via Help -> customVMoptions -> Xmxs )

### Created Files
The parser will create to files under TMPParser\ressources.
- Rename these files to "de.osm.edges" and "de.osm.nodes".
- Copy the files to the project directory of the actual application and place them in TravellingMisanthropistProblem\ressources. 
( two already parsed files for bw can be found here as well )

## HOW TO START THE PROJECT:
### Method 1: IDE
open the "TravelingMisanthropistProblem" Folder in IntelliJ-Idea and Run it.
### Method 2: JAR
- open the "TravelingMisanthropistProblem" Folder in Explorer,
- Open cmd window in that Folder,
- type and run following command:

    > ./mvnw spring-boot:run -Drun.jvmArguments="-Xmx16G" -Drun.profiles=dev


## Using the Project
once the project has started,
open a browser and navigate to "http://localhost:8080/"

Either allow locating via gps or click anywhere on the map to set your current location.

### TSP
First, press the "get nearby bars" button to retrieve nearby amenities.
A set of markers will be placed on the map.
Left click each marker to show a popup containing information about selected bar.
Right click a marker to select it as desired target of the roundtrip.
Once you have selected more than one marker, you can press the now appearing "calculate roundtrip" and the route will be displayed.

#### Ranked-TSP
instead of selecting bars by hand, use the "Misanthropist-Roundtrip" button to generate a tour to the least visited bars, ranked via foursquare.

### dijkstra.
Place an additional marker via corresponding button, then press "calculate path" to display shortest path between both markers 

### calculate Path:
calculates the shortest path between the first two markers

### calculate RoundTrip
calculates a TSP-Solution between the three markers

## Additional Notes:
- The project contains resource files to handle Baden-Würrtemberg.
- If instead of germany, BW should be used ( when less ram is available ), rename the existing files ( in ./ressources) to "de.osm.nodes" and "de.osm.edges".

## Implementation Details
- Java for Backend
- Leaflet for map visualisation
- Javascript for map interaction
- Java Springboot to bundle into web-application
- Map-Data from OpenStreetMaps
- Data for POIs from Foursquare API

# Project Goal:
Retrieve amenities nearby.
Calculate TSP for selected amenities.
OR
Calculate TSP by selecting Bars with lowest rating across all possible closeby bars.
