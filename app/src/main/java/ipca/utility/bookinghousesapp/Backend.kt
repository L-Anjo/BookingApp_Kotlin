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
    //private const val PATH_HOUSES = "House"

    //private val client = OkHttpClient()

    @SuppressLint("SuspiciousIndentation")
    fun fetchHouseDetail(lifecycleScope: LifecycleCoroutineScope, callback: (io.swagger.client.models.House)->Unit) {
        lifecycleScope.launch(Dispatchers.IO) {
            val houseApi = HouseApi("${BASE_API}").apiHouseIdGet(1)
            lifecycleScope.launch(Dispatchers.Main) {
                callback(houseApi)
            }


            /*client.newCall(request).execute().use { response ->
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

         */


        }
    }

    //-----
    fun fetchAllHousesSusp(lifecycleScope: LifecycleCoroutineScope, callback: (Array<io.swagger.client.models.House>) -> Unit) {
        lifecycleScope.launch(Dispatchers.IO) {
            val housesApi: Array<io.swagger.client.models.House> = HouseApi("${BASE_API}").apiHouseSuspGet()
            lifecycleScope.launch(Dispatchers.Main) {
                callback(housesApi)
                // log para verificar os ids
                housesApi.forEach { house ->
                    println("House ID: ${house.id_house}")
                }
            }

        }
    }

    fun updateHouseStateApproved(id: Int, lifecycleScope: LifecycleCoroutineScope, callback: () -> Unit) {
        lifecycleScope.launch(Dispatchers.IO) {
            HouseApi("${BASE_API}").apiHouseStateIdPut(id)
            lifecycleScope.launch(Dispatchers.Main) {
                callback()
            }

        }
    }

    fun deleteHouseById(id: Int, lifecycleScope: LifecycleCoroutineScope, callback: () -> Unit) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                HouseApi("${BASE_API}").apiHouseIdDelete(id)
                lifecycleScope.launch(Dispatchers.Main) {
                    callback()
                }
            } catch (e: Exception) {
                // Tratar erros, como por exemplo, notificar o utilizador ou registar o erro
                // Podes imprimir uma mensagem de erro simplesmente assim:
                println("Erro ao apagar a casa: ${e.message}")
            }
        }
    }

}