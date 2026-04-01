package com.example.serviconnect.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.serviconnect.R
import com.example.serviconnect.data.model.User
import com.example.serviconnect.data.repository.AuthRepository
import com.example.serviconnect.databinding.FragmentRegisterBinding
import com.example.serviconnect.utils.Constants
import com.example.serviconnect.utils.Resource
import com.example.serviconnect.utils.toast
import com.example.serviconnect.viewmodel.AuthViewModel
import com.example.serviconnect.viewmodel.AuthViewModelFactory


class RegisterFragment : Fragment() {
    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(AuthRepository())
    }

    private var selectedRole = Constants.ROLE_SEEKER

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState: Bundle?) {
        setUpRoleToggle()
        setUpCategorySpinner()
        setUpObservers()

        binding.btnRegister.setOnClickListener {
            performRegistration()
        }
    }

    private fun setUpRoleToggle() {
        binding.toggleGroup.addOnButtonCheckedListener { _,checkedId, isChecked ->
            if (isChecked) {
                if (checkedId == R.id.btnProvider) {
                    selectedRole = Constants.ROLE_PROVIDER
                    binding.providerFields.visibility = View.VISIBLE
                }
                else {
                    selectedRole = Constants.ROLE_SEEKER
                    binding.providerFields.visibility = View.GONE
                }
            }
        }
    }

    private fun setUpCategorySpinner() {
        val categories = listOf("Plumbing", "Electric", "Cleaning", "Carpentry", "Painting",
            "House Help", "Solar Installation", "Security", "Designing","Labour", "Other"
            )

        val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1, categories)
        binding.actvCategory.setAdapter(adapter)
    }

    private fun performRegistration() {
        val name     = binding.tilName.editText?.text.toString().trim()
        val email    = binding.tilEmail.editText?.text.toString().trim()
        val password = binding.tilPassword.editText?.text.toString().trim()

        if(name.isEmpty() || email.isEmpty() || password.isEmpty() || password.length < 6) {
            toast("Please fill all details correctly (Password min six chars")
            return
        }

        val user = User(
            fullName = name,
            email = email,
            role = selectedRole,

            category   = if (selectedRole == Constants.ROLE_PROVIDER) binding.actvCategory.text.toString() else null,
            experience = if (selectedRole == Constants.ROLE_PROVIDER) binding.tilExperience.editText?.text.toString() else null
        )
        viewModel.register(user, password)
    }

    private fun setUpObservers() {
        viewModel.registrationStatus.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnRegister.isEnabled = false
                }

                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    toast("Registration Successful")

                    if (selectedRole == Constants.ROLE_PROVIDER) {
                        findNavController().navigate(R.id.action_registerFragment_to_dashboardFragment)
                    } else {
                        findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                    }
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnRegister.isEnabled = true
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

/*
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

 */