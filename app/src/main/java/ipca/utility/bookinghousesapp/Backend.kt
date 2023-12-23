package ipca.utility.bookinghousesapp

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import io.jsonwebtoken.io.IOException
import io.swagger.client.apis.AuthApi
import io.swagger.client.apis.FeedbackApi
import io.swagger.client.apis.HouseApi
import io.swagger.client.apis.ReservationApi
import io.swagger.client.apis.UserApi
import java.time.LocalDateTime
import java.util.Date
import io.swagger.client.infrastructure.ClientException
import io.swagger.client.infrastructure.ServerException
import io.swagger.client.infrastructure.ApiClient
import io.swagger.client.models.EditProfile
import io.swagger.client.models.Feedback
import ipca.utility.bookinghousesapp.Backend.AUTHENTICATION_API
import ipca.utility.bookinghousesapp.Backend.BASE_API
import ipca.utility.bookinghousesapp.Models.House
import ipca.utility.bookinghousesapp.Models.Image
import ipca.utility.bookinghousesapp.Models.PostalCode
import ipca.utility.bookinghousesapp.Models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    fun fetchHouseDetail(idhouse: Int): LiveData<ResultWrapper<io.swagger.client.models.House>> =
    liveData(Dispatchers.IO) {
        emit( wrap { HouseApi(BASE_API).apiHouseIdGet(idhouse) })
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
                var userStatus = authApi.authStatus

                val sharedPreferences =
                    context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("access_token", token)
                editor.putInt("user_id", userId ?: -1)
                editor.putInt("user_type", userType ?: -1)
                editor.putBoolean("user_status", userStatus ?: false)
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
                val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
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
    fun CreateFeedback(
        lifecycleScope:
        LifecycleCoroutineScope,
        newClassification: Int,
        newComment: String,
        newReservation : Int,
        callback: (Boolean) -> Unit
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            println(newClassification)
            println(newComment)
            println(newReservation)
            //val reservation = ReservationApi("${BASE_API}").apiReservationIdGet(newReservation)

            FeedbackApi("${BASE_API}").apiFeedbackPost(Feedback(comment = newComment, classification = newClassification),newReservation)
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
            val authToken = sharedPreferences.getString("access_token", null)
            UserApi("${BASE_API}").apiUserUserIdPut(authToken,userId)

            lifecycleScope.launch(Dispatchers.Main) {
                callback(true)
            }

        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun UpdateUserProfile(
        context: Context,
        lifecycleScope: LifecycleCoroutineScope,
        newUserName: String,
        newUserEmail: String,
        newUserPhone: Int,
        callback: (Boolean) -> Unit
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt("user_id", 0)
            val authToken = sharedPreferences.getString("access_token", "")

            UserApi("${BASE_API}").apiUserUserIdProfilePut(authToken, userId, EditProfile(newUserName, newUserEmail, newUserPhone))

            lifecycleScope.launch(Dispatchers.Main) {
                callback(true)
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun UpdateUserAvatar(
        context: Context,
        lifecycleScope: LifecycleCoroutineScope,
        callback: (Boolean) -> Unit
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val authToken = sharedPreferences.getString("access_token", "")
            UserApi("${BASE_API}").apiUserAvatarPut(authToken)

            lifecycleScope.launch(Dispatchers.Main) {
                callback(true)
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun CancelReservation(
        lifecycleScope: LifecycleCoroutineScope,
        reservationId: Int,
        callback: (Boolean) -> Unit
    ) {
        lifecycleScope.launch(Dispatchers.IO) {

            ReservationApi("${BASE_API}").apiReservationReservationIdDeactivatePut(reservationId)

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
            val authToken = sharedPreferences.getString("access_token", null)
            val userApi = UserApi("${BASE_API}").apiUserUserIdGet(authToken, userId)
            lifecycleScope.launch(Dispatchers.Main) {
                callback(userApi)
            }

        }
    }


    fun fetchAllHousesSusp(lifecycleScope: LifecycleCoroutineScope, callback: (Array<io.swagger.client.models.House>) -> Unit) {
        lifecycleScope.launch(Dispatchers.IO) {
            val housesApi: Array<io.swagger.client.models.House> = HouseApi("${BASE_API}").apiHouseSuspGet()
            //val housesApi: Array<io.swagger.client.models.House> = HouseApi("${BASE_API}").apiHouseGet()
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

    fun createHouse(body: io.swagger.client.models.House, lifecycleScope: LifecycleCoroutineScope, callback: () -> Unit) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                HouseApi("${BASE_API}").apiHousePost(body)
                // Se a criação for bem-sucedida, você pode retornar true
                lifecycleScope.launch(Dispatchers.Main) {
                    callback()
                }
            } catch (e: Exception) {
                // Em caso de erro, retorne false ou trate conforme necessário
                // Aqui você pode imprimir uma mensagem de erro simplesmente assim:
                println("Erro ao criar a casa: ${e.message}")
            }
        }
    }

    fun fetchUserDetail(
        context : Context,
        lifecycleScope: LifecycleCoroutineScope,
        id_user: Int,
        callback: (io.swagger.client.models.User) -> Unit
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val authToken = sharedPreferences.getString("access_token", "")
            val userApi = UserApi("${BASE_API}").apiUserUserIdGet(authToken, id_user)
            lifecycleScope.launch(Dispatchers.Main) {
                println("USER Backend:")
                println(id_user)
                println(userApi)
                println(userApi.id_user)
                callback(userApi)
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun GetAllReservations(
        context: Context,
        lifecycleScope: LifecycleCoroutineScope,
        callback: (Array<io.swagger.client.models.Reservation>) -> Unit
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            val sharedPreferences =
                context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val authToken = sharedPreferences.getString("access_token", null)
            val reservationsApi = ReservationApi("${BASE_API}").apiReservationGet(authToken)


            lifecycleScope.launch(Dispatchers.Main) {
                callback(reservationsApi)
            }

        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun GetAllHouses(
        context: Context,
        lifecycleScope: LifecycleCoroutineScope,
        callback: (Array<io.swagger.client.models.House>) -> Unit
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            val sharedPreferences =
                context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val authToken = sharedPreferences.getString("access_token", null)
            val housesApi = HouseApi("${BASE_API}").apiHouseAllGet(authToken)


            lifecycleScope.launch(Dispatchers.Main) {
                callback(housesApi)
            }

        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun GetUserReservations(
        context: Context,
        lifecycleScope: LifecycleCoroutineScope,
        callback: (Array<io.swagger.client.models.Reservation>) -> Unit
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            val sharedPreferences =
                context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val authId = sharedPreferences.getInt("user_id", 0)
            val authToken = sharedPreferences.getString("access_token", null)

            val reservationApi = UserApi("${BASE_API}").apiUserReservationsIdGet(authToken, authId)


            lifecycleScope.launch(Dispatchers.Main) {
                callback(reservationApi)
            }

        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun GetUserHouses(
        context: Context,
        lifecycleScope: LifecycleCoroutineScope,
        callback: (Array<io.swagger.client.models.House>) -> Unit
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            val sharedPreferences =
                context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val authId = sharedPreferences.getInt("user_id", 0)
            val authToken = sharedPreferences.getString("access_token", null)
            println(authId)
            val houseApi = UserApi("${BASE_API}").apiUserHousesIdGet(authToken, authId)


            lifecycleScope.launch(Dispatchers.Main) {
                callback(houseApi)
            }

        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun DeleteHouse(
        context: Context,
        lifecycleScope: LifecycleCoroutineScope,
        houseId: Int,
        callback: (Boolean) -> Unit
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            val sharedPreferences =
                context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val authToken = sharedPreferences.getString("access_token", null)
            HouseApi("${BASE_API}").apiHouseStateDeleteIdPut(houseId)


            lifecycleScope.launch(Dispatchers.Main) {
                callback(true)
            }

        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun DeleteUser(
        context: Context,
        lifecycleScope: LifecycleCoroutineScope,
        userId: Int,
        callback: (Boolean) -> Unit
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            val sharedPreferences =
                context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val authToken = sharedPreferences.getString("access_token", null)
            UserApi("${BASE_API}").apiUserUserIdDelete(userId)


            lifecycleScope.launch(Dispatchers.Main) {
                callback(true)
            }

        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun DeactivateUser(
        context: Context,
        lifecycleScope: LifecycleCoroutineScope,
        userId: Int,
        callback: (Boolean) -> Unit
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            val sharedPreferences =
                context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val authToken = sharedPreferences.getString("access_token", null)
            UserApi("${BASE_API}").apiUserUserIdDeactivatePut(authToken, userId)


            lifecycleScope.launch(Dispatchers.Main) {
                callback(true)
            }

        }
    }



}




