package com.example.serviconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.serviconnect.data.repository.ServiceRepository

class SeekerViewModelFactory(private val repository: ServiceRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SeekerViewModel::class.java)) {
            return SeekerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}