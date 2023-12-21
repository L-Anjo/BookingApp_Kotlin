package ipca.utility.bookinghousesapp

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import io.swagger.client.apis.AuthApi
import io.swagger.client.apis.HouseApi
import io.swagger.client.apis.UserApi
import java.time.LocalDateTime
import java.util.Date
import io.swagger.client.infrastructure.ClientException
import io.swagger.client.infrastructure.ServerException
import io.swagger.client.infrastructure.ApiClient
import ipca.utility.bookinghousesapp.Models.House
import ipca.utility.bookinghousesapp.Models.Image
import ipca.utility.bookinghousesapp.Models.PostalCode
import ipca.utility.bookinghousesapp.Models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.util.Objects

object Backend {

    internal const val BASE_API = "http://10.0.2.2:7105"
    internal const val AUTHENTICATION_API = "http://10.0.2.2:5159"
    //private const val PATH_HOUSES = "House"

    //private val client = OkHttpClient()

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

    fun filterHouses(lifecycleScope: LifecycleCoroutineScope,location: String?,guestsNumber: Int?, startDate: LocalDateTime?,endDate: LocalDateTime?, callback: (Array<io.swagger.client.models.House>)->Unit) {
        lifecycleScope.launch(Dispatchers.IO) {
            val housesFiltredApi: Array<io.swagger.client.models.House> = HouseApi("${BASE_API}").apiHouseFilteredGet(location,guestsNumber,startDate,endDate)
            lifecycleScope.launch(Dispatchers.Main) {
                callback(housesFiltredApi)
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun login(
        context: Context,
        lifecycleScope: LifecycleCoroutineScope,
        email: String,
        password: String,
        callback: (Boolean) -> Unit
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val authApi = AuthApi("${AUTHENTICATION_API}")
                authApi.apiAuthPost(email, password)
                var token = authApi.authToken
                var userId = authApi.authUserId
                var userType = authApi.authUserType

                val sharedPreferences =
                    context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("access_token", token)
                editor.putInt("user_id", userId ?: -1)
                editor.putInt("user_type", userType ?: -1)
                editor.apply()

                lifecycleScope.launch(Dispatchers.Main) {
                    callback(true)
                }
            } catch (e: ClientException) {
                Log.e("LoginActivity", "Client error on login: ${e.message}")
                lifecycleScope.launch(Dispatchers.Main) {
                    callback(false)
                }
            } catch (e: ServerException) {
                Log.e("LoginActivity", "Server error on login: ${e.message}")
                lifecycleScope.launch(Dispatchers.Main) {
                    callback(false)
                }
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun logout(
        context: Context,
        lifecycleScope: LifecycleCoroutineScope,
        callback: () -> Unit
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val sharedPreferences =
                    context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                val authToken = sharedPreferences.getString("access_token", null)
                if (authToken != null) {
                    AuthApi("${AUTHENTICATION_API}").apiAuthPostLogout(authToken)
                }

                lifecycleScope.launch(Dispatchers.Main) {
                    callback()
                }
            } catch (e: Exception) {
                // Handle exceptions more generically
                Log.e("LogoutActivity", "Error during logout: ${e.message}")
                lifecycleScope.launch(Dispatchers.Main) {
                    callback()
                }
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun CreateUser(
        lifecycleScope:
        LifecycleCoroutineScope,
        name: String,
        email: String,
        password: String,
        phone: Int,
        callback: (Boolean) -> Unit
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            UserApi("${BASE_API}").apiUserPost(name, email, password, phone)
            lifecycleScope.launch(Dispatchers.Main) {
                callback(true)
            }


        }
    }


    @SuppressLint("SuspiciousIndentation")
    fun UpdateUser(
        context: Context,
        lifecycleScope: LifecycleCoroutineScope,
        name: String,
        email: String,
        phone: Int,
        password: String,
        token: String,
        staus: Boolean,
        callback: (Boolean) -> Unit
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt("user_id", 0)
            UserApi("${BASE_API}").apiUserUserIdPut(userId)

            lifecycleScope.launch(Dispatchers.Main) {
                callback(true)
            }

        }
    }


    @SuppressLint("SuspiciousIndentation")
    fun GetAllUsers(
        lifecycleScope: LifecycleCoroutineScope,
        callback: (Array<io.swagger.client.models.User>) -> Unit
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            val userApi = UserApi("${BASE_API}").apiUserGet()
            lifecycleScope.launch(Dispatchers.Main) {
                callback(userApi)
            }

        }
    }
}




