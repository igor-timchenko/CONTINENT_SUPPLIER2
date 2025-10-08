package ru.contlog.mobile.helper.repo

import android.util.Log
import kotlinx.serialization.json.Json
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.contlog.mobile.helper.model.ApiResponse
import ru.contlog.mobile.helper.model.AuthCheckSmsParams
import ru.contlog.mobile.helper.model.AuthGetSmsParams
import ru.contlog.mobile.helper.utils.await

object Api {
    const val TAG = "Contlog.Api"

    const val API_DOMAIN = ""
    const val API_ENDPOINT = "https://$API_DOMAIN"

    private val client = OkHttpClient()

    object Auth {
        suspend fun getSms(
            phoneNumber: String
        ): ApiResponse {
            try {
                val payload = Json.encodeToString(AuthGetSmsParams(phoneNumber))

                val body = FormBody.Builder()
                    .add("data", payload)
                    .build()

                val request = Request.Builder()
                    .url("$API_ENDPOINT/auth/get_sms")
                    .post(body)
                    .build()

                val call = client.newCall(request)
                val response = call.await()

                if (response.isSuccessful) {
                    val responseText = response.body.string()
                    val data = Json.decodeFromString<ApiResponse>(responseText)

                    return data
                }

                return ApiResponse(
                    status = false,
                    error = true,
                    message = "Запрос вернул ошибку: ${response.message}"
                )
            } catch (e: Exception) {
                Log.e(TAG, "getSms: Error sending request", e)

                return ApiResponse(
                    status = false,
                    error = true,
                    message = "Ошибка во время выполнения запроса: ${e.message ?: "неизвестная ошибка"}"
                )
            }
        }

        suspend fun checkSms(
            phoneNumber: String,
            code: String
        ): ApiResponse {
            try {
                val payload = Json.encodeToString(AuthCheckSmsParams(phoneNumber, code))

                val body = FormBody.Builder()
                    .add("data", payload)
                    .build()

                val request = Request.Builder()
                    .url("$API_ENDPOINT/auth/check_sms")
                    .post(body)
                    .build()

                val call = client.newCall(request)
                val response = call.await()

                if (response.isSuccessful) {
                    val responseText = response.body.string()
                    val data = Json.decodeFromString<ApiResponse>(responseText)

                    return data
                }

                return ApiResponse(
                    status = false,
                    error = true,
                    message = "Запрос вернул ошибку: ${response.message}"
                )
            } catch (e: Exception) {
                Log.e(TAG, "checkSms: Error sending request", e)

                return ApiResponse(
                    status = false,
                    error = true,
                    message = "Ошибка во время выполнения запроса: ${e.message ?: "неизвестная ошибка"}"
                )
            }
        }
    }
}