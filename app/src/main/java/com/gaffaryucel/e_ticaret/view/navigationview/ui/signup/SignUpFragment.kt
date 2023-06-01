package com.gaffaryucel.e_ticaret.view.navigationview.ui.signup

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gaffaryucel.e_ticaret.R
import com.gaffaryucel.e_ticaret.databinding.FragmentSignUpBinding
import com.gaffaryucel.e_ticaret.view.navigationview.NavigationDrawerActivity
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {

    private lateinit var binding : FragmentSignUpBinding
    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        binding = FragmentSignUpBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
       binding.signupButton.setOnClickListener {
           val email = binding.emailEditText.text.toString()
           val password = binding.passwordEditText.text.toString()
           val password2  = binding.confirmPasswordEditText.text.toString()
           val type  = binding.userTypeSpinner.selectedItem.toString()
           viewModel.signUp(email,password,password2,requireContext(),type,requireActivity())
       }
        binding.gotoSignIn.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    fun observeLiveData() {
        viewModel.signUpSucces.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                val intent = Intent(requireActivity(),NavigationDrawerActivity::class.java)
                requireActivity().startActivity(intent)
                requireActivity().finish()
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            if (loading) {
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.INVISIBLE
            }
        })
        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            if (error) {
                binding.errorText.visibility = View.VISIBLE
            }else{
                binding.errorText.visibility = View.INVISIBLE
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            binding.errorText.text = errorMessage
        })
    }
}