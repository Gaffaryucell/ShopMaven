package com.gaffaryucel.e_ticaret.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.gaffaryucel.e_ticaret.R
import com.gaffaryucel.e_ticaret.databinding.FragmentProductDetailsBinding
import com.gaffaryucel.e_ticaret.view.navigationview.ui.home.HomeViewModel
import com.gaffaryucel.e_ticaret.viewmodel.Product
import com.gaffaryucel.e_ticaret.viewmodel.ProductDetailsViewModel


class ProductDetailsFragment : Fragment() {
    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var viewModel: HomeViewModel
    private var product1 = Product()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        val productId = arguments?.getString("productid")
        val where = arguments?.getString("from")
        if (where.equals("basket")){
            binding.addBasketButton.visibility = View.INVISIBLE
        }else{
            binding.addBasketButton.visibility = View.VISIBLE
        }
        viewModel.getProducts()
        observeLiveData(productId.toString())
        binding.addBasketButton.setOnClickListener {
            viewModel.addInBasket(product1)
        }
    }
    fun observeLiveData(productId : String){
            viewModel.products.observe(viewLifecycleOwner, Observer {
                for (myproduct in it) {
                    if (myproduct.id == productId) {
                        viewModel.oneProduct.value = myproduct
                        product1 = myproduct
                        viewModel.getSupplireName(product1.supplier.toString())
                        break // eşleşme bulundu, döngüyü sonlandır
                    }
                }
                binding.apply {
                    product = product1
                    executePendingBindings()
                }
                Glide.with(requireContext()).load(product1.imageUrl).into(binding.productImage)
            })
        viewModel.name.observe(viewLifecycleOwner, Observer {
            binding.productSupplier.text = it
        })
    }
}