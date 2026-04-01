package com.example.serviconnect.data.repository

import com.example.serviconnect.R
import com.example.serviconnect.data.model.Category
import com.example.serviconnect.data.model.ServiceRequest
import com.example.serviconnect.data.model.User
import com.example.serviconnect.utils.Constants
import com.example.serviconnect.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ServiceRepository {
    private val db = FirebaseFirestore.getInstance()


    fun getCategories() : List<Category> {

        return listOf(
            Category("1", "Plumbing", R.drawable.ic_plumbing),
            Category("2", "Electric", R.drawable.ic_electric),
            Category("3", "Cleaning", R.drawable.ic_cleaning),
            Category("4", "Carpentry", R.drawable.ic_carpenting),
            Category("5", "Painting", R.drawable.ic_paint),
            Category("6", "House Help", R.drawable.ic_laundry),
            Category("7", "Solar Installation", R.drawable.ic_solar),
            Category("8", "Security", R.drawable.ic_security),
            Category("9", "Designing", R.drawable.ic_designing),
            Category("10", "Labour", R.drawable.ic_construction),
            Category("11", "Other", R.drawable.ic_other),
        )
    }

    suspend fun getProvidersByCategory(category: String): Resource<List<User>> {
       return try {
           val snapshot = db.collection(Constants.COLLECTION_USERS)
               .whereEqualTo("role",Constants.ROLE_PROVIDER)
               .whereEqualTo("category", category)
               .get()
               .await()

           val providers = snapshot.toObjects(User::class.java)
           Resource.Success(providers)
       } catch (e: Exception) {
           Resource.Error(e.localizedMessage ?:"Failed to load providers")
       }
    }

    // Fetch specific provider details
    suspend fun getProviderDetails(uid: String): Resource<User> {
        return try {
            val document = db.collection(Constants.COLLECTION_USERS).document(uid).get().await()
            val user = document.toObject(User::class.java) ?: throw Exception("User not found")
            Resource.Success(user)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error fetching details")
        }
    }

    // Submit a service request
    suspend fun submitRequest(request: ServiceRequest): Resource<Unit> {
        return try {
            val ref = db.collection(Constants.COLLECTION_REQUESTS).document()
            val finalRequest = request.copy(id = ref.id)
            ref.set(finalRequest).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Failed to send request")
        }
    }

    suspend fun getIncomingRequests(providerId: String): Resource<List<ServiceRequest>> {
        return try {
            val snapshot = db.collection(Constants.COLLECTION_REQUESTS)
                .whereEqualTo("providerId", providerId)
                .get()
                .await()

            val requests = snapshot.toObjects(ServiceRequest::class.java)
            // Sort by timestamp so newest requests appear at the top
            Resource.Success(requests.sortedByDescending { it.timestamp })
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Failed to load requests")
        }
    }

    suspend fun updateRequestStatus(requestId: String, newStatus: String): Resource<Unit> {
        return try {
            db.collection(Constants.COLLECTION_REQUESTS)
                .document(requestId)
                .update("status", newStatus)
                .await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Failed to update status")
        }
    }
}