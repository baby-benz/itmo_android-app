package com.example.myapplication.contract.task3.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CompanyResponse(
    val id: String,
    val name: String
)