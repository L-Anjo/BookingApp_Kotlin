package ipca.utility.bookinghousesapp

import androidx.lifecycle.LifecycleCoroutineScope
import ipca.utility.bookinghousesapp.Models.House
import ipca.utility.bookinghousesapp.Models.Image
import ipca.utility.bookinghousesapp.Models.PostalCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

object Backend {

    private const val BASE_API = "http://10.0.2.2:7105/api/"
    private const val PATH_HOUSES = "House/1"

    private val client = OkHttpClient()

    fun fetchHouseDetail(lifecycleScope: LifecycleCoroutineScope, callback:(House,PostalCode,ArrayList<Image>)->Unit ) {
        lifecycleScope.launch(Dispatchers.IO) {

            val request = Request.Builder()
                .url("${BASE_API}${PATH_HOUSES}")
                .build()

            client.newCall(request).execute().use { response ->
                val result = response.body!!.string()

                val jsonObject = JSONObject(result)
                val house = House.fromJson(jsonObject)

                val postalCodeObject = jsonObject.getJSONObject("postalCode")
                val postalCode = PostalCode.fromJson(postalCodeObject)

                val imageArray = jsonObject.getJSONArray("images")
                val imageList = ArrayList<Image>()

                for (i in 0 until imageArray.length()) {
                    val imageObject = imageArray.getJSONObject(i)
                    val image = Image.fromJson(imageObject)
                    imageList.add(image)
                }

                lifecycleScope.launch(Dispatchers.Main) {
                    callback(house,postalCode,imageList)
                }
                }
            }
        }
    }

