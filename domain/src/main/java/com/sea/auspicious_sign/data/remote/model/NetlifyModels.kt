package com.sea.auspicious_sign.data.remote.model

data class UploadResponse(
    val blobKey: String,
    val jobId: String
)

data class ProcessRequest(
    val blobKey: String,
    val dataType: String
)

data class ResultResponse(
    val status: String,   // "processing", "completed", "failed"
    val result: String? = null
)
