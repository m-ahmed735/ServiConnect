package com.example.serviconnect.data.model

data class ServiceRequest(
    val id: String = "",
    val seekerId: String = "",
    val providerId: String = "",
    val seekerName: String = "",
    val category: String = "",
    val description: String = "",
    val status: String = "PENDING", // PENDING, ACCEPTED, COMPLETED, CANCELLED
    val timestamp: Long = System.currentTimeMillis()
)
