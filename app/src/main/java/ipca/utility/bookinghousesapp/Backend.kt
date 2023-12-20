package ipca.utility.bookinghousesapp

import android.annotation.SuppressLint
import androidx.lifecycle.LifecycleCoroutineScope
import io.swagger.client.apis.HouseApi
import ipca.utility.bookinghousesapp.Models.House
import ipca.utility.bookinghousesapp.Models.Image
import ipca.utility.bookinghousesapp.Models.PostalCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.util.Objects

object Backend {

    internal const val BASE_API = "http://10.0.2.2:7105"

    @SuppressLint("SuspiciousIndentation")
    fun fetchHouseDetail(lifecycleScope: LifecycleCoroutineScope, callback: (io.swagger.client.models.House)->Unit) {
        lifecycleScope.launch(Dispatchers.IO) {
            val houseApi = HouseApi("${BASE_API}").apiHouseIdGet(1)
                lifecycleScope.launch(Dispatchers.Main) {
                    callback(houseApi)
                }
            }
        }

    fun fetchAllHouses(lifecycleScope: LifecycleCoroutineScope, callback: (Array<io.swagger.client.models.House>)->Unit) {
        lifecycleScope.launch(Dispatchers.IO) {
            val houseListApi: Array<io.swagger.client.models.House> = HouseApi("${BASE_API}").apiHouseGet()
            lifecycleScope.launch(Dispatchers.Main) {
                callback(houseListApi)
            }
        }
    }


    }


