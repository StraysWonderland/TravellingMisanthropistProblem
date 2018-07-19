package DTO;

public enum Highway {
    motorway("motorway", 1, 120),
    motorway_link("motorway_link", 2, 60),
    primary("primary", 3, 100),
    primary_link("primary_link", 4, 70),
    secondary("secondary", 5, 80),
    secondary_link("secondary_link", 6, 70),
    tertiary("tertiary", 7, 70),
    tertiary_link("tertiary_link", 8, 70),
    trunk("trunk", 9, 120),
    trunk_link("trunk_link", 10, 80),
    unclassified("unclassified", 11, 50),
    residential("residential", 12, 45),
    living_street("living_street", 13, 5),
    road("road", 14, 50),
    service("service", 15, 30),
    turning_circle("turning_circle", 16, 50),
    cycleway("cicleway", 17, 10),
    steps("steps", 18, 5),
    path("path", 19, 5),
    bridleway("bridleway", 20, 5),
    track("track", 21, 50),
    footway("footway",22,15);


    private String Name;
    private int Type;
    private int MaxSpeed;

    Highway(String name, int type, int maxSpeed) {
        Name = name;
        Type = type;
        MaxSpeed = maxSpeed;
    }

    public String getName() {
        return Name;
    }

    public int getType() {
        return Type;
    }

    public int getMaxSpeed() {
        return MaxSpeed;
    }
}