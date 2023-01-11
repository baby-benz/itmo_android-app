package com.example.myapplication.contract.task3.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    val id: String,
    val name: String,
    val price: Double,
    val producer: CompanyResponse,
    val supplier: CompanyResponse
) : java.io.Serializable