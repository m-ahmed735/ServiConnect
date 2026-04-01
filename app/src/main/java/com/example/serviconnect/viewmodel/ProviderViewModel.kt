package com.example.serviconnect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviconnect.data.model.ServiceRequest
import com.example.serviconnect.data.repository.ServiceRepository
import com.example.serviconnect.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ProviderViewModel(private val repository: ServiceRepository) : ViewModel() {

    private val _incomingRequests = MutableLiveData<Resource<List<ServiceRequest>>>()
    val incomingRequests: LiveData<Resource<List<ServiceRequest>>> = _incomingRequests

    private val _updateStatus = MutableLiveData<Resource<Unit>>()
    val updateStatus: LiveData<Resource<Unit>> = _updateStatus

    fun fetchRequests() {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        _incomingRequests.value = Resource.Loading
        viewModelScope.launch {
            _incomingRequests.value = repository.getIncomingRequests(currentUid)
        }
    }
    fun updateStatus(requestId: String, status: String) {
        _updateStatus.value = Resource.Loading
        viewModelScope.launch {
            val result = repository.updateRequestStatus(requestId, status)
            _updateStatus.value = result
            if (result is Resource.Success) {
                fetchRequests() // Refresh the list after update
            }
        }
    }
}