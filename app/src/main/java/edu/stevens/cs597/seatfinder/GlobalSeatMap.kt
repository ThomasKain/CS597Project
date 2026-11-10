package edu.stevens.cs597.seatfinder

data class Building(
    val id: String,
    val name: String,
    val floors: List<Floor>
)

data class Floor(
    val id: String,
    val buildingId: String,
    val level: Int,
    val label: String,
    val mapImage: String,
    val noiseLevel: Int,
    val seats: List<Seat>
)

data class Seat(
    val id: String,
    val floorId: String,
    val isAvailable: Boolean,
    val isFavorite: Boolean
)

object SeatSearchResults {
    var matches: List<List<List<Seat>>> = emptyList()
}

object GlobalSeatMap {
    private const val GS = "GS"
    private const val GS_1 = "GS_F1"
    private const val GS_2 = "GS_F2"

    private const val GN = "GN"
    private const val GN_1 = "GN_F1"
    private const val GN_2 = "GN_F2"

    val buildings: List<Building> = listOf(
        Building(
            id = GS,
            name = "Gateway South",
            floors = listOf(
                Floor(
                    id = GS_1,
                    buildingId = GS,
                    level = 1,
                    label = "Gateway South 1st Floor",
                    mapImage = "gateway_south_floor1",
                    noiseLevel = 2,
                    seats = (1..10).map { seatNumber ->
                        Seat(
                            id = seatNumber.toString(),
                            floorId = GS_1,
                            isAvailable = seatNumber in 4..6,
                            isFavorite = false
                        )
                    }
                ),
                Floor(
                    id = GS_2,
                    buildingId = GS,
                    level = 4,
                    label = "Gateway South 2nd Floor",
                    mapImage = "gateway_south_floor2",
                    noiseLevel = 2,
                    seats = (11..20).map { seatNumber ->
                        Seat(
                            id = seatNumber.toString(),
                            floorId = GS_2,
                            isAvailable = seatNumber in 11..12 || seatNumber in 18..19,
                            isFavorite = false
                        )
                    }
                ),

            )
        ),
        Building(
            id = GN,
            name = "Gateway North",
            floors = listOf(
                Floor(
                    id = GN_1,
                    buildingId = GN,
                    level = 1,
                    label = "Gateway North 1st Floor",
                    mapImage = "gateway_north_floor1",
                    noiseLevel = 5,
                    seats = (1..10).map { seatNumber ->
                        Seat(
                            id = seatNumber.toString(),
                            floorId = GN_1,
                            isAvailable = seatNumber in 1..3,
                            isFavorite = false
                        )
                    }
                ),
                Floor(
                    id = GN_2,
                    buildingId = GN,
                    level = 4,
                    label = "Gateway North 2nd Floor",
                    mapImage = "gateway_north_floor2",
                    noiseLevel = 3,
                    seats = (11..20).map { seatNumber ->
                        Seat(
                            id = seatNumber.toString(),
                            floorId = GN_2,
                            isAvailable = seatNumber in 14..18,
                            isFavorite = false
                        )
                    }
                ),

                )
        )
    )
}