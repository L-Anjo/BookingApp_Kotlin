package ipca.utility.bookinghousesapp.Models

import org.json.JSONObject

data class Images (

    val image: String,
    val formato: String
) {

    companion object {
        fun fromJson(jsonObject: JSONObject): Images {
            return Images(
                jsonObject.getString("image"),
                jsonObject.getString("formato"),
            )
        }
    }
}