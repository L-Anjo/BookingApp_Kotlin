package ipca.utility.bookinghousesapp

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import io.swagger.client.apis.AuthApi
import io.swagger.client.apis.HouseApi
import io.swagger.client.apis.UserApi
import java.time.LocalDateTime
import java.util.Date
import io.swagger.client.infrastructure.ClientException
import io.swagger.client.infrastructure.ServerException
import ipca.utility.bookinghousesapp.Backend.AUTHENTICATION_API
import ipca.utility.bookinghousesapp.Backend.BASE_API
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import java.io.IOException
import java.util.Objects

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Error(val code: Int? = null, val error: String? = null) :
        ResultWrapper<Nothing>()

    object NetworkError : ResultWrapper<Nothing>()

    inline fun onSuccess(action: (value: T) -> Unit): ResultWrapper<T> {
        if (this is Success) action(value)
        return this
    }

    inline fun onError(action: (error: Error) -> Unit): ResultWrapper<T> {
        if (this is Error) action(this)
        return this
    }

    inline fun onNetworkError(action: () -> Unit): ResultWrapper<T> {
        if (this is NetworkError) action()
        return this
    }
}

object Backend {

    internal const val BASE_API = "http://10.0.2.2:7105"
    internal const val AUTHENTICATION_API = "http://10.0.2.2:5159"
    //private const val PATH_HOUSES = "House"

    suspend fun <T> wrap(apiCall: suspend () -> T): ResultWrapper<T> {
        return try {
            ResultWrapper.Success(apiCall())
        } catch (throwable: Throwable) {
            Log.e("Repository", throwable.toString())
            when (throwable) {
                is IOException -> ResultWrapper.NetworkError
                else -> {
                    ResultWrapper.Error(0, throwable.message)
                }
            }
        }
    }

    fun fetchHouseDetail(): LiveData<ResultWrapper<io.swagger.client.models.House>> =
    liveData(Dispatchers.IO) {
        emit( wrap { HouseApi(BASE_API).apiHouseIdGet(1) })
    }

    fun fetchAllHouses(): LiveData<ResultWrapper<Array<io.swagger.client.models.House>>> =
        liveData(Dispatchers.IO) {
            emit( wrap { HouseApi(BASE_API).apiHouseGet() })
        }

    fun filterHouses(location: String?,guestsNumber: Int?,checkedV: Boolean?, startDate: LocalDateTime?,endDate: LocalDateTime?):
            LiveData<ResultWrapper<Array<io.swagger.client.models.House>>> =
        liveData(Dispatchers.IO) {
            emit( wrap { HouseApi(BASE_API).apiHouseFilteredGet(location,guestsNumber,checkedV,startDate,endDate) })
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
        context: Context,
        lifecycleScope: LifecycleCoroutineScope,
        callback: (Array<io.swagger.client.models.User>) -> Unit
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val authToken = sharedPreferences.getString("access_token", "")
            val userApi = UserApi("${BASE_API}").apiUserGet(authToken)
            lifecycleScope.launch(Dispatchers.Main) {
                callback(userApi)
            }

        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun fetchUserDetail(
        context: Context,
        lifecycleScope: LifecycleCoroutineScope,
        callback: (io.swagger.client.models.User) -> Unit
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt("user_id", 0)
            val userApi = UserApi("${BASE_API}").apiUserUserIdGet(userId)
            lifecycleScope.launch(Dispatchers.Main) {
                callback(userApi)
            }

        }
    }

}




