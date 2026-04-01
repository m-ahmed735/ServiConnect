package com.example.serviconnect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviconnect.data.model.ServiceRequest
import com.example.serviconnect.data.model.User
import com.example.serviconnect.data.repository.ServiceRepository
import com.example.serviconnect.utils.Resource
import kotlinx.coroutines.launch

class SeekerViewModel(private val repository: ServiceRepository) : ViewModel() {
    private val _providers = MutableLiveData<Resource<List<User>>>()
    val providers: LiveData<Resource<List<User>>> = _providers

    private val _providerDetails = MutableLiveData<Resource<User>>()
    val providerDetails: LiveData<Resource<User>> = _providerDetails

    // NEW: For tracking the "Send Request" status
    private val _requestStatus = MutableLiveData<Resource<Unit>>()
    val requestStatus: LiveData<Resource<Unit>> = _requestStatus

    fun fetchProviders(category: String) {
        _providers.value = Resource.Loading
        viewModelScope.launch {
            _providers.value = repository.getProvidersByCategory(category)
        }
    }

    fun getProviderDetails(uid: String) {
        _providerDetails.value = Resource.Loading
        viewModelScope.launch {
            _providerDetails.value = repository.getProviderDetails(uid)
        }
    }

    // NEW: Submit the service request
    fun sendRequest(request: ServiceRequest) {
        _requestStatus.value = Resource.Loading
        viewModelScope.launch {
            _requestStatus.value = repository.submitRequest(request)
        }
    }
}