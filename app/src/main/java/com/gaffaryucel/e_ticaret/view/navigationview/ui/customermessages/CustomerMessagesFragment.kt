package com.gaffaryucel.e_ticaret.view.navigationview.ui.customermessages

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
import com.gaffaryucel.e_ticaret.adapter.OrderAdapter
import com.gaffaryucel.e_ticaret.databinding.FragmentBuyBinding
import com.gaffaryucel.e_ticaret.databinding.FragmentCustomerMessagesBinding
import com.gaffaryucel.e_ticaret.model.MessageModel
import com.gaffaryucel.e_ticaret.model.User
import com.gaffaryucel.e_ticaret.view.navigationview.ui.buy.BuyViewModel
import com.gaffaryucel.e_ticaret.viewmodel.CustomerOrder
import com.google.firebase.auth.FirebaseAuth

class CustomerMessagesFragment : Fragment() {


    private lateinit var viewModel: CustomerMessagesViewModel

    private lateinit var binding : FragmentCustomerMessagesBinding
    private var adapter = CustomerMessageAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomerMessagesBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CustomerMessagesViewModel::class.java)
        // TODO: Use the ViewModel
        adapter = CustomerMessageAdapter()
        binding.customermessagesrecyclerview.layoutManager =  LinearLayoutManager(requireContext())
        binding.customermessagesrecyclerview.adapter = adapter
        viewModel.getAllMessages(FirebaseAuth.getInstance().currentUser!!.uid)
        observeLiveData()
    }
    private fun observeLiveData(){
        viewModel.users.observe(viewLifecycleOwner, Observer {
            adapter.customerlist = it as ArrayList<CustomerMessageModel>
            adapter.notifyDataSetChanged()
        })
    }
}
class CustomerMessageModel(
    val id : String,
    val name : String
)