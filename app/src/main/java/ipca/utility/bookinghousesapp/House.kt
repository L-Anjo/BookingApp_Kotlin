package ipca.utility.bookinghousesapp

import org.json.JSONObject

data class House(
    var id_house : Int,
    var name : String,
    var doorNumber : Int,
    var floorNumber : Int,
    var price : Double?,
    var priceyear: Double?,
    var guestsNumber: Int,
    var road: String,
    var codDoor: Int?,
    var sharedRoom: Boolean,
    var postalCode : String
){

    companion object{
        fun fromJson(jsonObject: JSONObject) : House {
            return House(
                jsonObject["id_house"         ] as Int,
                jsonObject["name"       ] as String,
                jsonObject["doorNumber"        ] as Int,
                jsonObject["floorNumber"  ] as Int,
                jsonObject["price"  ] as Double,
                jsonObject["priceyear"  ] as Double,
                jsonObject["guestsNumber"  ] as Int,
                jsonObject["road"  ] as String,
                jsonObject["codDoor"  ] as Int,
                jsonObject["sharedRoom"  ] as Boolean,
                jsonObject["postalCode"  ] as String

            )
        }
    }

}
