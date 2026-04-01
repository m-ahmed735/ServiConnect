package com.example.serviconnect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviconnect.data.model.User
import com.example.serviconnect.data.repository.AuthRepository
import com.example.serviconnect.utils.Resource
import kotlinx.coroutines.launch

class AuthViewModel(private var repository: AuthRepository): ViewModel() {

    private val _registrationStatus = MutableLiveData<Resource<Unit>>()
    private val _loginStatus = MutableLiveData<Resource<String>>()
    val registrationStatus: LiveData<Resource<Unit>> = _registrationStatus
    val loginStatus : LiveData<Resource<String>> = _loginStatus

    fun register(user: User, password: String) {
        _registrationStatus.value = Resource.Loading

        viewModelScope.launch {
            val result = repository.registerUser(user, password)
            _registrationStatus.value = result
        }
    }

    fun login(email: String, password: String) {
        _loginStatus.value = Resource.Loading

        viewModelScope.launch {
            val result = repository.loginUser(email, password)
            _loginStatus.value = result
        }
    }
}