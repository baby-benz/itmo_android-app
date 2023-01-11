package com.example.myapplication.contract.task3.data

import android.util.Log
import io.ktor.client.*
import com.example.myapplication.contract.task3.Task3Contract
import com.example.myapplication.contract.task3.Task3CreateProductCardContract
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import com.example.myapplication.contract.task3.Task3ProductCardContract
import com.example.myapplication.contract.task3.data.dto.CompanyResponse
import com.example.myapplication.contract.task3.data.dto.ProductRequest
import com.example.myapplication.contract.task3.data.dto.ProductResponse
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

object Task3Repository : Task3Contract.Model, Task3ProductCardContract.Model, Task3CreateProductCardContract.Model {
    private val httpClient = HttpClient(CIO) {
        install(Logging) {
            logger = CustomAndroidHttpLogger
            level = LogLevel.BODY
        }
        install(ContentNegotiation) {
            json()
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 4000L
            connectTimeoutMillis = 4000L
            socketTimeoutMillis = 4000L
        }
        engine {
            https {
                trustManager = object : X509TrustManager {
                    override fun checkClientTrusted(
                        p0: Array<out X509Certificate>?,
                        p1: String?
                    ) {
                    }

                    override fun checkServerTrusted(
                        p0: Array<out X509Certificate>?,
                        p1: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate>? = null
                }
            }
        }
        defaultRequest {
            url {
                parameters.append("forCard", "1")
                protocol = URLProtocol.HTTP
                host = "192.168.2.114"
                port = 8080
            }
        }
    }

    private object CustomAndroidHttpLogger : Logger {
        private const val logTag = "CustomAndroidHttpLogger"

        override fun log(message: String) {
            Log.i(logTag, message)
        }
    }

    override suspend fun getProducts(): List<ProductResponse> = withContext(Dispatchers.IO) {
        try {
            httpClient.get("products").body()
        } catch (ex: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${ex.response.status.description}")
            emptyList()
        } catch (ex: ClientRequestException) {
            // 4xx - responses
            println("Error: ${ex.response.status.description}")
            emptyList()
        } catch (ex: ServerResponseException) {
            // 5xx - response
            println("Error: ${ex.response.status.description}")
            emptyList()
        }
    }

    override suspend fun getProductsPaginated(): List<ProductResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getProducers(): List<CompanyResponse> = withContext(Dispatchers.IO) {
        try {
            httpClient.get("producers").body()
        } catch (ex: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${ex.response.status.description}")
            emptyList()
        } catch (ex: ClientRequestException) {
            // 4xx - responses
            println("Error: ${ex.response.status.description}")
            emptyList()
        } catch (ex: ServerResponseException) {
            // 5xx - response
            println("Error: ${ex.response.status.description}")
            emptyList()
        }
    }

    override suspend fun getSuppliers(): List<CompanyResponse> = withContext(Dispatchers.IO) {
        try {
            httpClient.get("suppliers").body()
        } catch (ex: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${ex.response.status.description}")
            emptyList()
        } catch (ex: ClientRequestException) {
            // 4xx - responses
            println("Error: ${ex.response.status.description}")
            emptyList()
        } catch (ex: ServerResponseException) {
            // 5xx - response
            println("Error: ${ex.response.status.description}")
            emptyList()
        }
    }

    override suspend fun saveProduct(productToSave: ProductRequest): ProductResponse =
        withContext(Dispatchers.IO) {
            try {
                httpClient.post("product") {
                    contentType(ContentType.Application.Json)
                    setBody(productToSave)
                }.body()
            } catch (ex: RedirectResponseException) {
                // 3xx - responses
                println("Error: ${ex.response.status.description}")
                throw RuntimeException("Exception 300")
            } catch (ex: ClientRequestException) {
                // 4xx - responses
                println("Error: ${ex.response.status.description}")
                throw RuntimeException("Exception 400")
            } catch (ex: ServerResponseException) {
                // 5xx - response
                println("Error: ${ex.response.status.description}")
                throw RuntimeException("Exception 500")
            }
        }

    override suspend fun updateProduct(
        id: String,
        productToUpdate: ProductRequest
    ): ProductResponse = withContext(Dispatchers.IO) {
        try {
            httpClient.put("product") {
                url {
                    appendPathSegments(id)
                }
                contentType(ContentType.Application.Json)
                setBody(productToUpdate)
            }.body()
        } catch (ex: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${ex.response.status.description}")
            throw RuntimeException("Exception 300")
        } catch (ex: ClientRequestException) {
            // 4xx - responses
            println("Error: ${ex.response.status.description}")
            throw RuntimeException("Exception 400")
        } catch (ex: ServerResponseException) {
            // 5xx - response
            println("Error: ${ex.response.status.description}")
            throw RuntimeException("Exception 500")
        }
    }

    override suspend fun deleteProduct(id: String) = withContext(Dispatchers.IO) {
        try {
            httpClient.delete("product") {
                url {
                    appendPathSegments(id)
                }
            }.body()
        } catch (ex: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${ex.response.status.description}")
        } catch (ex: ClientRequestException) {
            // 4xx - responses
            println("Error: ${ex.response.status.description}")
        } catch (ex: ServerResponseException) {
            // 5xx - response
            println("Error: ${ex.response.status.description}")
        }
    }

    override fun close() = httpClient.close()
}