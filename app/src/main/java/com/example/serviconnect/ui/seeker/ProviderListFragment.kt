package com.example.serviconnect.ui.seeker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.serviconnect.R
import com.example.serviconnect.data.repository.ServiceRepository
import com.example.serviconnect.databinding.FragmentProviderListBinding
import com.example.serviconnect.utils.Resource
import com.example.serviconnect.utils.hide
import com.example.serviconnect.utils.show
import com.example.serviconnect.utils.toast
import com.example.serviconnect.viewmodel.SeekerViewModel
import com.example.serviconnect.viewmodel.SeekerViewModelFactory

class ProviderListFragment : Fragment() {

    private var _binding: FragmentProviderListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SeekerViewModel by viewModels {
        SeekerViewModelFactory(ServiceRepository())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProviderListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get category passed from HomeFragment
        val categoryName = arguments?.getString("categoryName") ?: ""
        binding.tvHeader.text = "$categoryName Providers"

        viewModel.fetchProviders(categoryName)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.providers.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> binding.progressBar.show()
                is Resource.Success -> {
                    binding.progressBar.hide()
                    val adapter = ProviderAdapter(resource.data) { provider ->
                        // Navigate to detail screen
                        val bundle = Bundle().apply { putString("providerId", provider.uid) }
                        findNavController().navigate(R.id.action_providerListFragment_to_providerDetailFragment, bundle)
                    }
                    binding.rvProviders.adapter = adapter
                }
                is Resource.Error -> {
                    binding.progressBar.hide()
                    toast(resource.message)
                }
            }
        }
    }
}