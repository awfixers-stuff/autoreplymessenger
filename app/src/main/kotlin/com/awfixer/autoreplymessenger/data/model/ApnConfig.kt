autoreplymessenger/app/src/main/kotlin/com/awfixer/autoreplymessenger/data/model/ApnConfig.kt
package com.awfixer.autoreplymessenger.data.model

data class ApnConfig(
    val name: String,
    val mmsc: String,
    val mmsProxy: String?,
    val mmsPort: Int?,
    val apn: String,
    val user: String?,
    val password: String?,
    val authType: Int?
)
