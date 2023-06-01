package com.gaffaryucel.e_ticaret.view.navigationview.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gaffaryucel.e_ticaret.adapter.OrderAdapter
import com.gaffaryucel.e_ticaret.adapter.ProductAdapter
import com.gaffaryucel.e_ticaret.databinding.FragmentGalleryBinding
import com.gaffaryucel.e_ticaret.view.navigationview.ui.home.HomeViewModel
import com.gaffaryucel.e_ticaret.viewmodel.CustomerOrder
import com.gaffaryucel.e_ticaret.viewmodel.Product

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private lateinit var  viewModel : GalleryViewModel
    private var adapter = OrderAdapter()
    private var orderlist = ArrayList<CustomerOrder>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)
        viewModel.getOrdered()
        binding.ordersRecyclerView.layoutManager =  LinearLayoutManager(requireContext())
        binding.ordersRecyclerView.adapter = adapter
        observeLiveData()
    }

    fun observeLiveData(){
        viewModel.orders.observe(viewLifecycleOwner, Observer {
            if (it.size == 0){
                binding.ordertext.visibility = View.VISIBLE
            }else {
                binding.ordertext.visibility = View.INVISIBLE
                adapter.orderList = it as ArrayList<CustomerOrder>
                adapter.notifyDataSetChanged()
                orderlist = it
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}