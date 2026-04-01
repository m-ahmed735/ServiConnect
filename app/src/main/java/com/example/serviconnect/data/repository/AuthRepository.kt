package com.example.serviconnect.data.repository

import com.example.serviconnect.data.model.User
import com.example.serviconnect.utils.Constants
import com.example.serviconnect.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun registerUser(user: User, password: String ): Resource<Unit> {
        return try {
            val result = auth.createUserWithEmailAndPassword(user.email,password).await()
            val uid = result.user?.uid ?: throw Exception("User ID is null")

            // Update Firebase Auth profile with full name
            val profileUpdates = userProfileChangeRequest {
                displayName = user.fullName
            }
            result.user?.updateProfile(profileUpdates)?.await()

            val userWithId = user.copy(uid = uid)
            db.collection(Constants.COLLECTION_USERS)
                .document(uid)
                .set(userWithId)
                .await()

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Registration Failed")
        }
    }

    suspend fun loginUser(email: String, password: String) : Resource<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email,password).await()
            val uid = result.user?.uid ?: throw Exception("User not found")

            // Fetch the role from Firebase
            val document = db.collection(Constants.COLLECTION_USERS).document(uid).get().await()
            val role = document.getString("role") ?: Constants.ROLE_SEEKER

            Resource.Success(role)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Login Failed")
        }
    }
}