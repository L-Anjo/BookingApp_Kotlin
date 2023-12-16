package ipca.utility.bookinghousesapp.Models

import androidx.annotation.Nullable
import org.json.JSONObject
/*
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

}*/

data class House (

    val idHouse: kotlin.Int? = null,
    val name: kotlin.String,
    val doorNumber: kotlin.Int,
    val floorNumber: kotlin.Int,
    val price: kotlin.Double? = null,
    val priceyear: kotlin.Double? = null,
    val guestsNumber: kotlin.Int,
    val rooms: kotlin.Int? = null,
    val road: kotlin.String,
    val propertyAssessment: kotlin.String,
    val codDoor: kotlin.Int? = null,
    val sharedRoom: kotlin.Boolean,
    val postalCode: PostalCode,
    val statusHouse: StatusHouse? = null,
    val images: kotlin.Array<Images>? = null,
    val reservations: kotlin.Array<Reservation>? = null,
    val user: User? = null
) {

    companion object {

        fun fromJson(jsonObject: JSONObject): House {
            return House(
                jsonObject.getInt("id_house"),
                jsonObject.getString("name"),
                jsonObject.getInt("doorNumber"),
                jsonObject.getInt("floorNumber"),
                jsonObject.getDouble("price") ?: null,
                jsonObject.optDouble("priceyear", 0.0),
                jsonObject.getInt("guestsNumber"),
                jsonObject.getInt("rooms"),
                jsonObject.getString("road"),
                jsonObject.getString("propertyAssessment"),
                jsonObject.optInt("codDoor") ?: null,
                jsonObject.getBoolean("sharedRoom"),
                PostalCode.fromJson(jsonObject.optJSONObject("postalCode")),
                StatusHouse.fromJson(jsonObject.optJSONObject("statusHouse")),


            )
        }
    }
    }
