package com.gaffaryucel.e_ticaret.view.navigationview.ui.orderdetailforcustomer

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.gaffaryucel.e_ticaret.R
import com.gaffaryucel.e_ticaret.databinding.FragmentOrderDetailsForCustomerBinding
import com.gaffaryucel.e_ticaret.viewmodel.CustomerOrder
import com.google.firebase.auth.FirebaseAuth

class OrderDetailsForCustomer : Fragment() {

    private lateinit var viewModel: OrderDetailsForCustomerViewModel
    private lateinit var binding: FragmentOrderDetailsForCustomerBinding
    private var order = CustomerOrder()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderDetailsForCustomerBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OrderDetailsForCustomerViewModel::class.java)
        // TODO: Use the ViewModel
        val myid =arguments?.getString("id")
        println("coming id : "+myid.toString())
        viewModel.getOrder(myid.toString())
        observeLiveData()
        binding.asksupplier.setOnClickListener {
            val customer = FirebaseAuth.getInstance().currentUser!!.uid
            val action = OrderDetailsForCustomerDirections.actionOrderDetailsForCustomerToMessageFragment(customer,order.supplier.toString(),"ask")
            Navigation.findNavController(it).navigate(action)
        }
    }
    private fun observeLiveData() {
        viewModel.incomingorder.observe(viewLifecycleOwner, Observer {
            binding.apply {
                product = it
                executePendingBindings()
            }
            order = it
            Glide.with(requireContext()).load(it.imageUrl).into(binding.productImage)
        })
    }
}