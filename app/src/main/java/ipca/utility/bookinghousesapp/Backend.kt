package ipca.utility.bookinghousesapp

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

object Backend {

    private const val BASE_API = "http://10.0.2.2:7105/api/"
    private const val PATH_HOUSES = "House/1"

    private val client = OkHttpClient()

    fun fetchHouseDetail(lifecycleScope: LifecycleCoroutineScope, callback:(House)->Unit ) {
        lifecycleScope.launch(Dispatchers.IO) {

            val request = Request.Builder()
                .url("${BASE_API}${PATH_HOUSES}")
                .build()

            client.newCall(request).execute().use { response ->
                val result = response.body!!.string()

                val jsonArray = JSONArray(result)
                if (jsonArray.length() > 0) {

                    val jsonHouse = jsonArray.getJSONObject(0)
                    val house = House.fromJson(jsonHouse)


                lifecycleScope.launch(Dispatchers.Main) {
                    callback(house)
                }
                }
            }
        }
    }

}