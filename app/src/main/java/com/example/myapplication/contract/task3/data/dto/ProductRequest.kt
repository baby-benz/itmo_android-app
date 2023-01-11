package com.example.myapplication.contract.task3.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductRequest(
    val name: String,
    val price: Double,
    val producerId: String,
    val supplierId: String
)
