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

import io.swagger.client.models.Feedback
import io.swagger.client.models.ProblemDetails

import io.swagger.client.infrastructure.*
import io.swagger.client.models.User

class FeedbackApi(basePath: kotlin.String = "/") : ApiClient(basePath) {

    /**
     * Apaga um feedback
     * 
     * @param feedbackId ID do feedback 
     * @return void
     */
    fun apiFeedbackFeedbackIdDelete(feedbackId: kotlin.Int): Unit {
        val localVariableConfig = RequestConfig(
                RequestMethod.DELETE,
                "/api/Feedback/{feedbackId}".replace("{" + "feedbackId" + "}", "$feedbackId")
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
     * Verifica se a reserva tem feedbacks
     *
     * @param reservationId ID da reserva
     * @return kotlin.Boolean
     */
    @Suppress("UNCHECKED_CAST")
    fun apiFeedbackReservationIdFeedbacksGet(reservationId: kotlin.Int): kotlin.Boolean {
        val localVariableConfig = RequestConfig(
            RequestMethod.GET,
            "/api/Feedback/{reservationId}/Feedbacks".replace("{" + "reservationId" + "}", "$reservationId")
        )
        val response = request<kotlin.Boolean>(
            localVariableConfig
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as kotlin.Boolean
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     * Obtém um feedback
     * 
     * @param feedbackId O ID do feedback 
     * @return Feedback
     */
    @Suppress("UNCHECKED_CAST")
    fun apiFeedbackFeedbackIdGet(feedbackId: kotlin.Int): Feedback {
        val localVariableConfig = RequestConfig(
                RequestMethod.GET,
                "/api/Feedback/{feedbackId}".replace("{" + "feedbackId" + "}", "$feedbackId")
        )
        val response = request<Feedback>(
                localVariableConfig
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as Feedback
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     * Atualiza um feedback
     * 
     * @param feedbackId ID do feedback 
     * @param body  (optional)
     * @return void
     */
    fun apiFeedbackFeedbackIdPut(feedbackId: kotlin.Int, body: Feedback? = null): Unit {
        val localVariableBody: kotlin.Any? = body
        val localVariableConfig = RequestConfig(
                RequestMethod.PUT,
                "/api/Feedback/{feedbackId}".replace("{" + "feedbackId" + "}", "$feedbackId")
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
     * Obtém todos os feedbacks.
     * 
     * @return kotlin.Array<Feedback>
     */
    @Suppress("UNCHECKED_CAST")
    fun apiFeedbackGet(): kotlin.Array<Feedback> {
        val localVariableConfig = RequestConfig(
                RequestMethod.GET,
                "/api/Feedback"
        )
        val response = request<kotlin.Array<Feedback>>(
                localVariableConfig
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as kotlin.Array<Feedback>
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     * Cria um feedback
     *
     * @param body  (optional)
     * @param reservationId  (optional)
     * @return void
     */
    fun apiFeedbackPost(body: Feedback? = null, reservationId: kotlin.Int? = null): Unit {
        val localVariableBody: kotlin.Any? = body
        val localVariableQuery: MultiValueMap = mutableMapOf<kotlin.String, kotlin.collections.List<kotlin.String>>().apply {
            if (reservationId != null) {
                put("reservationId", listOf(reservationId.toString()))
            }
        }
        val localVariableConfig = RequestConfig(
            RequestMethod.POST,
            "/api/Feedback", query = localVariableQuery
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

}
