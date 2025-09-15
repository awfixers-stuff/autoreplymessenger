package com.awfixer.autoreplymessenger.utils

import android.content.Context
import android.database.Cursor
import android.provider.Telephony
import android.telephony.SubscriptionManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.awfixer.autoreplymessenger.data.model.ApnConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApnResolver @Inject constructor(@ApplicationContext private val context: Context) {

    private val encryptedPrefs by lazy {
        val masterKey =
                MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        EncryptedSharedPreferences.create(
                context,
                "apn_cache",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    suspend fun resolveApn(): Result<ApnConfig> =
            withContext(Dispatchers.IO) {
                try {
                    // Try to get from cache first
                    val cachedApn = getCachedApn()
                    if (cachedApn != null) {
                        return@withContext Result.success(cachedApn)
                    }

                    // Query carriers
                    val apn = queryApnFromCarriers()
                    if (apn != null) {
                        cacheApn(apn)
                        return@withContext Result.success(apn)
                    }

                    // Fallback to default
                    val defaultApn = getDefaultApn()
                    cacheApn(defaultApn)
                    Result.success(defaultApn)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }

    private fun queryApnFromCarriers(): ApnConfig? {
        val uri = Telephony.Carriers.CONTENT_URI
        val projection =
                arrayOf(
                        Telephony.Carriers.NAME,
                        Telephony.Carriers.MMSC,
                        Telephony.Carriers.MMSPROXY,
                        Telephony.Carriers.MMSPORT,
                        Telephony.Carriers.APN,
                        Telephony.Carriers.USER,
                        Telephony.Carriers.PASSWORD,
                        Telephony.Carriers.AUTH_TYPE
                )

        // Get current subscription
        val subscriptionManager =
                context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as
                        SubscriptionManager
        val subscriptionId =
                subscriptionManager.activeSubscriptionInfoList?.firstOrNull()?.subscriptionId ?: -1

        val cursor: Cursor? =
                context.contentResolver.query(
                        uri,
                        projection,
                        "${Telephony.Carriers.TYPE} = ? AND ${Telephony.Carriers.SUBSCRIPTION_ID} = ?",
                        arrayOf("mms", subscriptionId.toString()),
                        null
                )

        cursor?.use {
            if (it.moveToFirst()) {
                val name = it.getString(it.getColumnIndexOrThrow(Telephony.Carriers.NAME))
                val mmsc = it.getString(it.getColumnIndexOrThrow(Telephony.Carriers.MMSC))
                val mmsProxy = it.getString(it.getColumnIndexOrThrow(Telephony.Carriers.MMSPROXY))
                val mmsPort = it.getInt(it.getColumnIndexOrThrow(Telephony.Carriers.MMSPORT))
                val apn = it.getString(it.getColumnIndexOrThrow(Telephony.Carriers.APN))
                val user = it.getString(it.getColumnIndexOrThrow(Telephony.Carriers.USER))
                val password = it.getString(it.getColumnIndexOrThrow(Telephony.Carriers.PASSWORD))
                val authType = it.getInt(it.getColumnIndexOrThrow(Telephony.Carriers.AUTH_TYPE))

                return ApnConfig(
                        name = name,
                        mmsc = mmsc,
                        mmsProxy = mmsProxy,
                        mmsPort = mmsPort,
                        apn = apn,
                        user = user,
                        password = password,
                        authType = authType
                )
            }
        }
        return null
    }

    private fun getDefaultApn(): ApnConfig {
        // Placeholder default APN; in real app, use carrier-specific defaults
        return ApnConfig(
                name = "Default MMS",
                mmsc = "http://mmsc.mobile.att.net",
                mmsProxy = "proxy.mobile.att.net",
                mmsPort = 80,
                apn = "wap.cingular",
                user = null,
                password = null,
                authType = null
        )
    }

    private fun getCachedApn(): ApnConfig? {
        val json = encryptedPrefs.getString("apn_config", null) ?: return null
        // Deserialize JSON to ApnConfig (use Gson or similar)
        // For simplicity, assume we have a method
        return deserializeApnConfig(json)
    }

    private fun cacheApn(apn: ApnConfig) {
        val json = serializeApnConfig(apn)
        encryptedPrefs.edit().putString("apn_config", json).apply()
    }

    // Placeholder serialization methods
    private fun serializeApnConfig(apn: ApnConfig): String {
        // Use Gson.toJson(apn)
        return "{}" // Dummy
    }

    private fun deserializeApnConfig(json: String): ApnConfig {
        // Use Gson.fromJson(json, ApnConfig::class.java)
        return getDefaultApn() // Dummy
    }
}
