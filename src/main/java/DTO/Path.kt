package DTO

class Path {
    var latitude: Double = 0.toDouble()
    var longitude: Double = 0.toDouble()
    private var isMarker: Boolean = false
    lateinit var type: String
    lateinit var name: String
    lateinit var iconUrl: String
    var poiId: Int = 0
    var distance: Double = 0.toDouble()

    constructor()

    constructor(geoCoordinates: DoubleArray, poiId: Int, isMarker: Boolean = false, type: String = "", name: String = "", distance: Double = 0.0) {
        this.latitude = geoCoordinates[0]
        this.longitude = geoCoordinates[1]
        this.isMarker = isMarker
        this.type = type
        this.name = name
        //  this.iconUrl = (!type.equals("")) ? Utils.getIconUrl(type) : "";
        this.poiId = poiId
        this.distance = when {
            isMarker -> distance
            else -> 0.0
        }
    }

    fun getIsMarker(): Boolean {
        return isMarker
    }

    fun setIsMarker(isMarker: Boolean) {
        this.isMarker = isMarker
    }
}


