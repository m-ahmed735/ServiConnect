package com.example.serviconnect.ui.seeker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.serviconnect.data.model.ServiceRequest
import com.example.serviconnect.data.model.User
import com.example.serviconnect.data.repository.ServiceRepository
import com.example.serviconnect.databinding.FragmentProviderDetailBinding
import com.example.serviconnect.utils.Resource
import com.example.serviconnect.utils.toast
import com.example.serviconnect.viewmodel.SeekerViewModel
import com.example.serviconnect.viewmodel.SeekerViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class ProviderDetailFragment : Fragment() {

    private var _binding: FragmentProviderDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SeekerViewModel by viewModels {
        SeekerViewModelFactory(ServiceRepository())
    }

    private var provider: User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProviderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val providerId = arguments?.getString("providerId") ?: ""

        viewModel.getProviderDetails(providerId) // Add this function to your SeekerViewModel

        binding.btnSendRequest.setOnClickListener {
            val desc = binding.tilDescription.editText?.text.toString().trim()
            if (desc.isNotEmpty()) {
                submitServiceRequest(desc)
            } else {
                toast("Please describe the job")
            }
        }

        setupObservers()
    }

    private fun submitServiceRequest(description: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        provider?.let { p ->
            val request = ServiceRequest(
                seekerId = currentUser?.uid ?: "",
                providerId = p.uid,
                seekerName = currentUser?.displayName ?: "User",
                category = p.category ?: "",
                description = description
            )
            viewModel.sendRequest(request) // Add this function to SeekerViewModel
        }
    }

    private fun setupObservers() {
        // Observe provider details
        viewModel.providerDetails.observe(viewLifecycleOwner) { resource ->
            if (resource is Resource.Success) {
                provider = resource.data
                binding.tvDetailName.text = provider?.fullName
                binding.tvDetailCategory.text = "Category: ${provider?.category}"
                binding.tvDetailExp.text = "Experience: ${provider?.experience} Years"
            }
        }

        // Observe request status
        viewModel.requestStatus.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> binding.btnSendRequest.isEnabled = false
                is Resource.Success<*> -> {
                    toast("Request sent successfully!")
                    findNavController().popBackStack() // Go back to the list
                }
                is Resource.Error -> {
                    binding.btnSendRequest.isEnabled = true
                    toast(resource.message)
                }
            }
        }
    }
}