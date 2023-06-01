package com.gaffaryucel.e_ticaret.view.navigationview.ui.profile

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.gaffaryucel.e_ticaret.EntryActivity
import com.gaffaryucel.e_ticaret.MainActivity
import com.gaffaryucel.e_ticaret.R
import com.gaffaryucel.e_ticaret.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding : FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
        viewModel.getProfilInfo(FirebaseAuth.getInstance().currentUser!!.uid)
        binding.logoutTextView.setOnClickListener{
            val intent = Intent(requireActivity(),EntryActivity::class.java)
            FirebaseAuth.getInstance().signOut()
            requireActivity().finish()
            requireActivity().startActivity(intent)
        }
        if (FirebaseAuth.getInstance().currentUser!!.displayName == "supplier"){
            binding.createProduct.visibility = View.VISIBLE
            binding.incomingOrders.visibility = View.VISIBLE
            binding.customermessagestextview.visibility = View.VISIBLE
            binding.createProduct.setOnClickListener{
                val action = ProfileFragmentDirections.actionNavProfileToCreateProductFragment()
                Navigation.findNavController(it).navigate(action)
            }
            binding.incomingOrders.setOnClickListener{
                val action = ProfileFragmentDirections.actionNavProfileToIncomingOrderFragment()
                Navigation.findNavController(it).navigate(action)
            }
            binding.customermessagestextview.setOnClickListener{
                val action = ProfileFragmentDirections.actionNavProfileToCustomerMessagesFragment()
                Navigation.findNavController(it).navigate(action)
            }
        }
        binding.profileLayout.setOnClickListener{
            val action = ProfileFragmentDirections.actionNavProfileToProfileDetailsFragment()
            Navigation.findNavController(it).navigate(action)
        }
        binding.messages.setOnClickListener{
            val action = ProfileFragmentDirections.actionNavProfileToMessageOfCustomerFragment()
            Navigation.findNavController(it).navigate(action)
        }
        observeLiveData()
    }
    private fun observeLiveData(){
        viewModel.user.observe(viewLifecycleOwner, Observer {
            binding.emailTextView.text = it.eMail
            binding.nameTextView.text = it.userName
            if(it.photo != null){
                Glide.with(requireContext()).load(it.photo).into(binding.profileImageView)
            }else{
                binding.profileImageView.setImageResource(R.drawable.profile)
            }
        })
    }
}