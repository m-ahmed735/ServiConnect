package com.example.serviconnect.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.serviconnect.R
import com.example.serviconnect.data.repository.AuthRepository
import com.example.serviconnect.databinding.FragmentLoginBinding
import com.example.serviconnect.utils.Constants
import com.example.serviconnect.utils.Resource
import com.example.serviconnect.utils.toast
import com.example.serviconnect.viewmodel.AuthViewModel
import com.example.serviconnect.viewmodel.AuthViewModelFactory

class LoginFragment: Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(AuthRepository())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.tilEmail.editText?.text.toString().trim()
            val password = binding.tilPassword.editText?.text.toString().trim()

            if(email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email,password)
            } else {
                toast("Please Enter Credentials")
            }
        }

        binding.tvGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment2_to_registerFragment)
        }

        setUpObservers()
    }

    private fun setUpObservers() {
        viewModel.loginStatus.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnLogin.isEnabled = false
                    binding.tvGoToRegister.isEnabled = false
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val role = resource.data

                    if(role == Constants.ROLE_SEEKER) {
                        findNavController().navigate(R.id.action_loginFragment2_to_homeFragment)
                    } else {
                        findNavController().navigate(R.id.action_loginFragment2_to_dashboardFragment)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnLogin.isEnabled = true
                    binding.tvGoToRegister.isEnabled = true
                    toast(resource.message)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}