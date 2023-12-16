package ipca.utility.bookinghousesapp.Models

import org.json.JSONObject

data class StatusHouse (

    val id: Int,
    val name: String
) {

    companion object {
        fun fromJson(jsonObject: JSONObject): StatusHouse {
            return StatusHouse(
                jsonObject.getInt("id"),
                jsonObject.getString("name"),
            )
        }
    }
}
