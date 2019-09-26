FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD TravellingMisanthropistProblem.jar tmpApp.jar
ADD de.osm.edges TravellingMisanthropistProblem/ressoures/de.osm.edges
ADD de.osm.nodes TravellingMisanthropistProblem/ressoures/de.osm.nodes
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Xmx16g", "-jar","/tmpApp.jar"]
