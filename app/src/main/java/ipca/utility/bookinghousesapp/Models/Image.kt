package ipca.utility.bookinghousesapp.Models

import org.json.JSONObject

data class Image(
    var image : String
) {
    companion object {
        fun fromJson(jsonObject: JSONObject): Image {
            return Image(
                jsonObject.getString("image"),
            )
        }
    }
}