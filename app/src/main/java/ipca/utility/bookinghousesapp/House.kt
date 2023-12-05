package ipca.utility.bookinghousesapp

data class House(
    var id_house : Int,
    var name : String,
    var doorNumber : Int,
    var floorNumber : Int,
    var price : Double?,
    var priceyear: Double?,
    var guestsNumber: Int,
    var road: String,
    var propertyAssessment: String,
    var codDoor: Int?,
    var sharedRoom: Boolean,
    var localidade : String,
    var stars : Int
)
