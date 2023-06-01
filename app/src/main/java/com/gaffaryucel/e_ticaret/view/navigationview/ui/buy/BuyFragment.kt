package com.gaffaryucel.e_ticaret.view.navigationview.ui.buy

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.gaffaryucel.e_ticaret.R
import com.gaffaryucel.e_ticaret.adapter.OrderAdapter
import com.gaffaryucel.e_ticaret.databinding.FragmentBuyBinding
import com.gaffaryucel.e_ticaret.databinding.FragmentGalleryBinding
import com.gaffaryucel.e_ticaret.view.navigationview.ui.gallery.GalleryViewModel
import com.gaffaryucel.e_ticaret.viewmodel.CustomerOrder

class BuyFragment : Fragment() {

    private lateinit var viewModel: BuyViewModel
    private lateinit var binding : FragmentBuyBinding
    private var adapter = OrderAdapter()
    private var orderlist = ArrayList<CustomerOrder>()
    private var allproducts = ArrayList<CustomerOrder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBuyBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BuyViewModel::class.java)
        // TODO: Use the ViewModel
        binding.recyclerView2.layoutManager =  LinearLayoutManager(requireContext())
        binding.recyclerView2.adapter = adapter
        viewModel.getOrdered()
        viewModel.getCardName()
        viewModel.getAddressTitle()
        observeLiveData()
        binding.usercardnametext.setOnClickListener {
            val action = BuyFragmentDirections.actionBuyFragmentToCardFragment()
            Navigation.findNavController(it).navigate(action)
        }
        binding.textViewAddressInfo.setOnClickListener {
            val action = BuyFragmentDirections.actionBuyFragmentToAddressFragment()
            Navigation.findNavController(it).navigate(action)
        }
        binding.orderbutton.setOnClickListener {
            viewModel.finishOrder(orderlist)
        }
    }
    fun observeLiveData(){
        viewModel.orders.observe(viewLifecycleOwner, Observer {
            adapter.orderList = it as ArrayList<CustomerOrder>
            adapter.notifyDataSetChanged()
            orderlist = it
        })
        viewModel.allproduct.observe(viewLifecycleOwner, Observer {
            allproducts = it as ArrayList<CustomerOrder>
        })
        viewModel.cardname.observe(viewLifecycleOwner, Observer {
            if(it == null){
                binding.usercardnametext.text = "select a card"
            }else{
                binding.usercardnametext.text = "Card Name : $it"
            }
        })
        viewModel.address.observe(viewLifecycleOwner, Observer {
            if(it == null){
                binding.textViewAddressInfo.text = "select an address"
            }else {
                binding.textViewAddressInfo.text = "Address Title : $it"
            }
        })
    }
}