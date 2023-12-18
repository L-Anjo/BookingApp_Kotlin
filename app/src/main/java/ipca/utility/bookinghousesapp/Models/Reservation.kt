package ipca.utility.bookinghousesapp.Models

import java.util.Date

data class Reservation (

    val idReservation: kotlin.Int? = null,
    val initDate: String,
    val endDate: String,
    val guestsNumber: kotlin.Int,
    val user: User? = null,
    val house: House? = null,
    val reservationStates: ReservationStates? = null
) {
}