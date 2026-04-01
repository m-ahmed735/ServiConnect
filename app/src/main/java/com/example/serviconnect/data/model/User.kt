package com.example.serviconnect.data.model

data class User(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val role: String = "",
    val area: String = "",

    // Specific fields (null for seekers)
    val category: String? = null,
    val experience: String? = null,
    val bio: String? = null
)
