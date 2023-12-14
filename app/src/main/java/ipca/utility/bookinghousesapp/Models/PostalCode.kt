package ipca.utility.bookinghousesapp.Models

import org.json.JSONObject

data class PostalCode(
    var postalCode : Int,
    var concelho : String,
    var district : String
) {

    companion object {
        fun fromJson(jsonObject: JSONObject): PostalCode {
            return PostalCode(
                jsonObject.getInt("postalCode"),
                jsonObject.getString("concelho"),
                jsonObject.getString("district")
            )
        }
    }
}
