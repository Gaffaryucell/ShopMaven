package com.gaffaryucel.e_ticaret.view.navigationview.ui.customermessages.messages

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gaffaryucel.e_ticaret.R
import com.gaffaryucel.e_ticaret.adapter.CustomerMessageAdapter
import com.gaffaryucel.e_ticaret.adapter.SuppliersMessagesAdapter
import com.gaffaryucel.e_ticaret.databinding.FragmentCustomerMessagesBinding
import com.gaffaryucel.e_ticaret.databinding.FragmentMessageOfCustomerBinding
import com.gaffaryucel.e_ticaret.view.navigationview.ui.customermessages.CustomerMessageModel
import com.gaffaryucel.e_ticaret.view.navigationview.ui.customermessages.CustomerMessagesViewModel
import com.google.firebase.auth.FirebaseAuth

class MessageOfCustomerFragment : Fragment() {

    private lateinit var viewModel: MessageOfCustomerViewModel

    private lateinit var binding : FragmentMessageOfCustomerBinding
    private var adapter = SuppliersMessagesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessageOfCustomerBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MessageOfCustomerViewModel::class.java)
        // TODO: Use the ViewModel
        adapter = SuppliersMessagesAdapter()
        binding.suppliermessagesrecyclerview.layoutManager =  LinearLayoutManager(requireContext())
        binding.suppliermessagesrecyclerview.adapter = adapter
        viewModel.getAllSuppliers()
        observeLiveData()
    }
    private fun observeLiveData(){
        viewModel.users.observe(viewLifecycleOwner, Observer {
            adapter.suppliers = it as ArrayList<CustomerMessageModel>
            adapter.notifyDataSetChanged()
        })
    }
}