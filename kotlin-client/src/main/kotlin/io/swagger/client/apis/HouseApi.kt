/**
 * EzBookingAPI
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: v1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
package io.swagger.client.apis

import io.swagger.client.models.House
import io.swagger.client.models.ProblemDetails

import io.swagger.client.infrastructure.*
import io.swagger.client.models.Reservation

class HouseApi(basePath: kotlin.String = "/") : ApiClient(basePath) {

    /**
     * Obtém todas as Casas.
     *
     * @return kotlin.Array<House>
     */
    @Suppress("UNCHECKED_CAST")
    fun apiHouseAllGet(token: String?): kotlin.Array<House> {

        val headers = mutableMapOf(
            "Authorization" to "Bearer ${token}"
        )

        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/api/House/All",
            headers = headers
        )
        val response = request<kotlin.Array<House>>(
            localVariableConfig
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as kotlin.Array<House>
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }

    /**
     * Obtém Codigo da Porta de uma Casa
     * 
     * @param id O ID da Casa 
     * @return House
     */
    @Suppress("UNCHECKED_CAST")
    fun apiHouseCodDoorIdGet(id: kotlin.Int): House {
        val localVariableConfig = RequestConfig(
                RequestMethod.GET,
                "/api/House/CodDoor/{id}".replace("{" + "id" + "}", "$id")
        )
        val response = request<House>(
                localVariableConfig
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as House
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     * Obtém Casas com Filtro.
     * 
     * @param location A localização da Casa (Distrito) (optional)
     * @param guestsNumber O Nº de Pessoas (optional)
     * @param startDate Data Check-in (optional)
     * @param endDate Data Check-out (optional)
     * @return kotlin.Array<House>
     */
    @Suppress("UNCHECKED_CAST")
    fun apiHouseFilteredGet(location: kotlin.String? = null, guestsNumber: kotlin.Int? = null, checkedV: kotlin.Boolean? = null, startDate: java.time.LocalDateTime? = null, endDate: java.time.LocalDateTime? = null): kotlin.Array<House> {
        val localVariableQuery: MultiValueMap = mutableMapOf<kotlin.String, kotlin.collections.List<kotlin.String>>().apply {
            if (location != null) {
                put("location", listOf(location.toString()))
            }
            if (guestsNumber != null) {
                put("guestsNumber", listOf(guestsNumber.toString()))
            }
            if (checkedV != null) {
                put("checkedV", listOf(checkedV.toString()))
            }
            if (startDate != null) {
                put("startDate", listOf(startDate.toString()))
            }
            if (endDate != null) {
                put("endDate", listOf(endDate.toString()))
            }
        }
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/api/House/Filtered", query = localVariableQuery
        )
        val response = request<kotlin.Array<House>>(
            localVariableConfig
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as kotlin.Array<House>
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }

    /**
     * Obtem Reservas de uma Casa
     *
     * @param id O ID da Casa
     * @return kotlin.Array<Reservation>
     */
    @Suppress("UNCHECKED_CAST")
    fun apiHouseReservationsIdGet(token : String?,id: kotlin.Int): kotlin.Array<Reservation> {
        val headers = mutableMapOf(
            "Authorization" to "Bearer ${token}"
        )
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/api/House/Reservations/{id}".replace("{" + "id" + "}", "$id"),
            headers = headers
        )
        val response = request<kotlin.Array<Reservation>>(
            localVariableConfig
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as kotlin.Array<Reservation>
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     * Obtém todas as Casas Ativas.
     * 
     * @return kotlin.Array<House>
     */
    @Suppress("UNCHECKED_CAST")
    fun apiHouseGet(): kotlin.Array<House> {


        val localVariableConfig = RequestConfig(
                RequestMethod.GET,
                "/api/House"
        )
        val response = request<kotlin.Array<House>>(
                localVariableConfig
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as kotlin.Array<House>
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }


    /**
     * Apaga uma Casa
     * 
     * @param id O ID da Casa 
     * @return void
     */
    fun apiHouseIdDelete(id: kotlin.Int): Unit {
        val localVariableConfig = RequestConfig(
                RequestMethod.DELETE,
                "/api/House/{id}".replace("{" + "id" + "}", "$id")
        )
        val response = request<Any?>(
                localVariableConfig
        )

        return when (response.responseType) {
            ResponseType.Success -> Unit
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     * Obtém Uma Casa
     * 
     * @param id O ID da Casa 
     * @return House
     */
    @Suppress("UNCHECKED_CAST")
    fun apiHouseIdGet(id: kotlin.Int): House {
        val localVariableConfig = RequestConfig(
                RequestMethod.GET,
                "/api/House/{id}".replace("{" + "id" + "}", "$id")
        )
        val response = request<House>(
                localVariableConfig
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as House
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     * Altera Dados de uma casa.
     * 
     * @param id O ID da Casa 
     * @param body  (optional)
     * @return void
     */
    fun apiHouseIdPut(id: kotlin.Int, body: House? = null): Unit {
        val localVariableBody: kotlin.Any? = body
        val localVariableConfig = RequestConfig(
                RequestMethod.PUT,
                "/api/House/{id}".replace("{" + "id" + "}", "$id")
        )
        val response = request<Any?>(
                localVariableConfig, localVariableBody
        )

        return when (response.responseType) {
            ResponseType.Success -> Unit
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     * Cria uma Casa
     * 
     * @param body  (optional)
     * @return void
     */
    fun apiHousePost(body: House? = null): Unit {
        val localVariableBody: kotlin.Any? = body
        val localVariableConfig = RequestConfig(
                RequestMethod.POST,
                "/api/House"
        )
        val response = request<Any?>(
                localVariableConfig, localVariableBody
        )

        return when (response.responseType) {
            ResponseType.Success -> Unit
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     * Obtém Reservas de uma Casa
     * 
     * @param id O ID da Casa 
     * @return House
     */
    @Suppress("UNCHECKED_CAST")
    fun apiHouseReservationsIdGet(id: kotlin.Int): House {
        val localVariableConfig = RequestConfig(
                RequestMethod.GET,
                "/api/House/Reservations/{id}".replace("{" + "id" + "}", "$id")
        )
        val response = request<House>(
                localVariableConfig
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as House
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     * Altera Estado da casa para Apagada.
     * 
     * @param id O ID da Casa 
     * @return void
     */
    fun apiHouseStateDeleteIdPut(id: kotlin.Int): Unit {
        val localVariableConfig = RequestConfig(
                RequestMethod.PUT,
                "/api/House/stateDelete/{id}".replace("{" + "id" + "}", "$id")
        )
        val response = request<Any?>(
                localVariableConfig
        )

        return when (response.responseType) {
            ResponseType.Success -> Unit
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     * Altera Estado da casa para Aprovado.
     * 
     * @param id O ID da Casa 
     * @return void
     */
    fun apiHouseStateIdPut(id: kotlin.Int): Unit {
        val localVariableConfig = RequestConfig(
                RequestMethod.PUT,
                "/api/House/state/{id}".replace("{" + "id" + "}", "$id")
        )
        val response = request<Any?>(
                localVariableConfig
        )

        return when (response.responseType) {
            ResponseType.Success -> Unit
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     * Obtém Casas Suspensas
     * 
     * @return kotlin.Array<House>
     */
    @Suppress("UNCHECKED_CAST")
    fun apiHouseSuspGet(): kotlin.Array<House> {
        val localVariableConfig = RequestConfig(
                RequestMethod.GET,
                "/api/House/susp"
        )
        val response = request<kotlin.Array<House>>(
                localVariableConfig
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as kotlin.Array<House>
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
}