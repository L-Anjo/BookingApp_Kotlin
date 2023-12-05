package ipca.utility.bookinghousesapp

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

object Backend {

    private const val BASE_API = "http://localhost:7227/api/"
    private const val PATH_HOUSES = "House"

    private val client = OkHttpClient()

    fun fetchHouses(lifecycleScope: LifecycleCoroutineScope, callback:(ArrayList<House> )->Unit ) {
        lifecycleScope.launch(Dispatchers.IO) {

            val request = Request.Builder()
                .url("${BASE_API}${PATH_HOUSES}")
                .build()

            client.newCall(request).execute().use { response ->
                val result = response.body!!.string()

                val jsonArray = JSONArray(result)
                val houses = arrayListOf<House>()
                for (index in 0..jsonArray.length()-1){
                    val jsonHouse = jsonArray.getJSONObject(index)

                    val house = House.fromJson(jsonHouse)
                    houses.add(house)

                }

                lifecycleScope.launch(Dispatchers.Main) {
                    callback(houses)
                }
            }
        }
    }

}