package edu.stevens.cs597.seatfinder

/**
 * Remembers which seats the user wants to be notified about.
 *
 * A seat is saved as "floorId-seatId" (for example "GS_F1-4"), because seat numbers
 * repeat between buildings: both Gateway South and Gateway North have a seat "1".
 *
 * This is a global object (like SeatSearchResults), so:
 *  - every page sees the same favorites, and
 *  - the favorites are NOT lost when the screen rotates.
 */
object FavoriteSeats {

    private val savedKeys = mutableSetOf<String>()

    private fun key(floorId: String, seatId: String) = "$floorId-$seatId"

    /** True if the user asked to be notified about this seat. */
    fun isFavorite(floorId: String, seatId: String): Boolean =
        key(floorId, seatId) in savedKeys

    /**
     * Saves the seat if it isn't saved yet, or removes it if it is.
     * Returns true if the seat is now saved, false if it was removed.
     */
    fun toggle(floorId: String, seatId: String): Boolean {
        val k = key(floorId, seatId)
        return if (savedKeys.remove(k)) {
            false
        } else {
            savedKeys.add(k)
            true
        }
    }
}
