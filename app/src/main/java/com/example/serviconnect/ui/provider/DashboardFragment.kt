package com.example.serviconnect.ui.provider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.serviconnect.data.repository.ServiceRepository
import com.example.serviconnect.databinding.FragmentDashboardBinding
import com.example.serviconnect.utils.Resource
import com.example.serviconnect.utils.toast
import com.example.serviconnect.viewmodel.ProviderViewModel
import com.example.serviconnect.viewmodel.ProviderViewModelFactory

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProviderViewModel by viewModels {
        ProviderViewModelFactory(ServiceRepository())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchRequests()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.incomingRequests.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> binding.providerProgressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.providerProgressBar.visibility = View.GONE

                    // Pass the data AND the lambda function for button clicks
                    val adapter = RequestAdapter(resource.data) { requestId, newStatus ->
                        // This is the onActionClick implementation
                        viewModel.updateStatus(requestId, newStatus)
                    }

                    binding.rvIncomingRequests.adapter = adapter
                }
                is Resource.Error -> {
                    binding.providerProgressBar.visibility = View.GONE
                    toast(resource.message)
                }
            }
        }

        viewModel.updateStatus.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> binding.providerProgressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.providerProgressBar.visibility = View.GONE
                    toast("Status updated successfully!")
                }
                is Resource.Error -> {
                    binding.providerProgressBar.visibility = View.GONE
                    toast(resource.message)
                }
            }
        }
    }
}