package com.example.serviconnect.ui.seeker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.serviconnect.R
import com.example.serviconnect.data.repository.ServiceRepository
import com.example.serviconnect.databinding.FragmentHomeBinding

class HomeFragment: Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = ServiceRepository()
        val categories = repository.getCategories()

        val adapter = CategoryAdapter(categories) { category ->
            val bundle = Bundle().apply { putString("categoryName",category.name) }
            findNavController().navigate(R.id.action_homeFragment_to_providerListFragment, bundle)
        }

        binding.rvCategories.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}