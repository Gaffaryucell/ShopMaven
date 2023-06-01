package com.gaffaryucel.e_ticaret.view.navigationview.ui.signin

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.gaffaryucel.e_ticaret.R
import com.gaffaryucel.e_ticaret.databinding.ActivityNavigationDrawerBinding
import com.gaffaryucel.e_ticaret.databinding.FragmentSignInBinding
import com.gaffaryucel.e_ticaret.view.navigationview.NavigationDrawerActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class SignInFragment : Fragment() {

    private lateinit var binding : FragmentSignInBinding
    private lateinit var viewModel: SignInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)
        binding = FragmentSignInBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = FirebaseAuth.getInstance()
        if(user.currentUser != null){
            if (user.currentUser!!.isEmailVerified){
                viewModel.saveToken()
                val intent = Intent(requireActivity(),NavigationDrawerActivity::class.java)
                requireActivity().startActivity(intent)
                requireActivity().finish()
            }else{
                val alert = AlertDialog.Builder(context)
                    .setTitle("Verify Alert")
                    .setMessage("Your e-mail  address has not been verified please check your mailbox")
                    .setPositiveButton("ok", object : DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {

                        }
                    })
                    .create()
                alert.show()
            }
        }
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.signIn(email,password,requireContext())
        }
        binding.gotoSignUp.setOnClickListener {
            val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
            Navigation.findNavController(it).navigate(action)
        }
        observeLiveData()
    }
    fun observeLiveData() {
        viewModel.signInSucces.observe(viewLifecycleOwner, Observer { success ->
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