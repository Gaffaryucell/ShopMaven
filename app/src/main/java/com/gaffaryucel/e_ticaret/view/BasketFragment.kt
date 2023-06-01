package com.gaffaryucel.e_ticaret.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.gaffaryucel.e_ticaret.R
import com.gaffaryucel.e_ticaret.adapter.BasketAdapter
import com.gaffaryucel.e_ticaret.adapter.CheckBoxListener
import com.gaffaryucel.e_ticaret.adapter.ProductAdapter
import com.gaffaryucel.e_ticaret.databinding.FragmentBasketBinding
import com.gaffaryucel.e_ticaret.viewmodel.BasketViewModel
import com.gaffaryucel.e_ticaret.viewmodel.CustomerOrder
import com.gaffaryucel.e_ticaret.viewmodel.Product


class BasketFragment : Fragment() , CheckBoxListener {
    private lateinit var binding: FragmentBasketBinding
    private lateinit var viewModel: BasketViewModel
    private var adapter = BasketAdapter(this)
    private var productList = ArrayList<CustomerOrder>()
    val checkedItems = mutableListOf<CustomerOrder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBasketBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(BasketViewModel::class.java)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = BasketAdapter(this)
        viewModel = ViewModelProvider(requireActivity()).get(BasketViewModel::class.java)
        binding.recyclerView.layoutManager =  LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        viewModel.getBasket()
        observeLiveData()
        binding.finishorderbtn.setOnClickListener {
            viewModel.finishOrder(checkedItems)
            val action = BasketFragmentDirections.actionNavBasketToBuyFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }
    fun observeLiveData(){
        viewModel.basket.observe(viewLifecycleOwner, Observer {
            if (it.size == 0){
                binding.basketText.visibility = View.VISIBLE
            }else{
                binding.basketText.visibility = View.INVISIBLE
                adapter.productList = it as ArrayList<CustomerOrder>
                adapter.notifyDataSetChanged()
                productList = it
            }
        })
    }

    override fun onItemChecked(item: CustomerOrder) {
        checkedItems.add(item)
    }

    override fun onItemUnchecked(item: CustomerOrder) {
        checkedItems.remove(item)
    }
}