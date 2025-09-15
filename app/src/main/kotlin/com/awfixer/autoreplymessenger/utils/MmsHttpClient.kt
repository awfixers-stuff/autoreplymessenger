package com.awfixer.autoreplymessenger.utils

import android.content.Context
import com.awfixer.autoreplymessenger.data.model.ApnConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URL
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MmsHttpClient @Inject constructor(@ApplicationContext private val context: Context) {

    suspend fun sendMms(apn: ApnConfig, pduData: ByteArray): Result<HttpResponse> =
            withContext(Dispatchers.IO) {
                try {
                    val url = URL(apn.mmsc)
                    val connection = url.openConnection() as HttpURLConnection

                    // Set proxy if available
                    if (!apn.mmsProxy.isNullOrEmpty() && apn.mmsPort != null) {
                        val proxy =
                                Proxy(Proxy.Type.HTTP, InetSocketAddress(apn.mmsProxy, apn.mmsPort))
                        connection.setRequestProperty("http.proxyHost", apn.mmsProxy)
                        connection.setRequestProperty("http.proxyPort", apn.mmsPort.toString())
                    }

                    // Configure connection
                    connection.requestMethod = "POST"
                    connection.doOutput = true
                    connection.connectTimeout = 30000 // 30 seconds
                    connection.readTimeout = 30000
                    connection.useCaches = false

                    // Set MMS headers
                    connection.setRequestProperty("Content-Type", "application/vnd.wap.mms-message")
                    connection.setRequestProperty("User-Agent", "Android-MMS/2.0")
                    connection.setRequestProperty("X-MMS-Message-Type", "m-send-req")
                    connection.setRequestProperty("X-MMS-Transaction-ID", generateTransactionId())
                    connection.setRequestProperty("X-MMS-Version", "1.0")
                    connection.setRequestProperty("X-MMS-MMS-Version", "1.0")

                    // Authentication if needed
                    if (!apn.user.isNullOrEmpty() && !apn.password.isNullOrEmpty()) {
                        val auth =
                                android.util.Base64.encodeToString(
                                        "${apn.user}:${apn.password}".toByteArray(),
                                        android.util.Base64.NO_WRAP
                                )
                        connection.setRequestProperty("Authorization", "Basic $auth")
                    }

                    // Write PDU data
                    connection.outputStream.use { it.write(pduData) }

                    // Get response
                    val responseCode = connection.responseCode
                    val responseMessage = connection.responseMessage ?: ""

                    connection.disconnect()

                    Result.success(HttpResponse(responseCode, responseMessage))
                } catch (e: IOException) {
                    Result.failure(e)
                }
            }

    private fun generateTransactionId(): String {
        return "T" + System.currentTimeMillis().toString()
    }

    data class HttpResponse(val code: Int, val message: String) {
        val isSuccessful: Boolean
            get() = code in 200..299
    }
}
