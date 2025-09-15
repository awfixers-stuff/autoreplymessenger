package com.awfixer.autoreplymessenger.data.model

data class MmsPart(
        val seq: Int,
        val contentType: String,
        val filename: String?,
        val text: String?,
        val fileUri: String?
)

data class ParsedMms(
        val date: Long,
        val from: String,
        val subject: String?,
        val body: String?,
        val parts: List<MmsPart>
)
