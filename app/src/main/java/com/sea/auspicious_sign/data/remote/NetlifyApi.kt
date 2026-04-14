package com.sea.auspicious_sign.data.remote

import com.sea.auspicious_sign.data.remote.model.ProcessRequest
import com.sea.auspicious_sign.data.remote.model.ResultResponse
import com.sea.auspicious_sign.data.remote.model.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Response

import retrofit2.http.*

interface NetlifyApi {
    @Multipart
    @POST("api/upload")
    suspend fun uploadData(
        @Part("type") type: String,
        @Part("metadata") metadata: Map<String, String>,
        @Part file: MultipartBody.Part
    ): UploadResponse

    @POST("api/process")
    suspend fun requestProcess(@Body request: ProcessRequest): UploadResponse  // 返回 jobId

    @GET("api/result/{jobId}")
    suspend fun getResult(@Path("jobId") jobId: String): ResultResponse

    @POST("api/mappings")
    suspend fun syncMapping(@Body mapping: Map<String, Any>): Response<Unit>

    @POST("api/sensor-data")
    suspend fun uploadSensorData(@Body batch: List<Any>): Response<Unit>

    // TODO: 根据实际后端 API 调整端点路径和请求/响应模型
}
