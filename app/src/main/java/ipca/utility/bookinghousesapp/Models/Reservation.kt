package ipca.utility.bookinghousesapp.Models

data class Reservation (

    val idReservation: kotlin.Int? = null,
    val initDate: java.time.LocalDateTime,
    val endDate: java.time.LocalDateTime,
    val guestsNumber: kotlin.Int,
    val user: User? = null,
    val house: House? = null,
    val reservationStates: ReservationStates? = null
) {
}