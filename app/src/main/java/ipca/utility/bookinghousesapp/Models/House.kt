package ipca.utility.bookinghousesapp.Models

import androidx.annotation.Nullable
import org.json.JSONObject

data class House(
    var id_house : Int,
    var name : String,
    var doorNumber : Int,
    var floorNumber : Int,
    var price : Double?,
    var priceYear: Double?,
    var guestsNumber: Int,
    var rooms:     Int,
    var road: String,
    var codDoor: Int?,
    var sharedRoom: Boolean
){

    companion object{
        fun fromJson(jsonObject: JSONObject) : House {
            return House(
                jsonObject.getInt("id_house"),
                jsonObject.getString("name"),
                jsonObject.getInt("doorNumber"),
                jsonObject.getInt("floorNumber"),
                jsonObject.getInt("price").toDouble()?:null,
                jsonObject.optDouble("priceyear",0.0),
                jsonObject.getInt("guestsNumber"),
                jsonObject.getInt("rooms"),
                jsonObject.getString("road"),
                jsonObject.optInt("codDoor")?: null,
                jsonObject.getBoolean("sharedRoom")

            )
        }
    }

}
