package com.gaffaryucel.e_ticaret.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gaffaryucel.e_ticaret.databinding.ProductRowBinding
import com.gaffaryucel.e_ticaret.model.ProductModel
import com.gaffaryucel.e_ticaret.view.navigationview.ui.home.HomeFragmentDirections
import com.gaffaryucel.e_ticaret.viewmodel.CustomerOrder
import com.gaffaryucel.e_ticaret.viewmodel.Product

class ProductAdapter :  RecyclerView.Adapter<ProductAdapter.ProductHolder>() {
    class ProductHolder(val binding : ProductRowBinding):RecyclerView.ViewHolder(binding.root){

    }

    var productList = ArrayList<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val binding = ProductRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        println(productList.get(0).imageUrl)
        return ProductHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        println("bind")
        val myProduct = productList[position]
        holder.binding.apply {
            product = myProduct
            executePendingBindings()
        }
        Glide.with(holder.itemView.context)
            .load(myProduct.imageUrl)
            .into(holder.binding.productImage)
        holder.itemView.setOnClickListener{
            val action = HomeFragmentDirections.actionNavHomeToProductDetailsFragment(myProduct.id.toString(),"home")
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        println("in it ")
        return productList.size
    }
    fun changeproductModelwithCustomerOrder(order : CustomerOrder) : Product{
        return Product(
            id = order.id,
            name = order.name,
            description = order.description,
            category = order.category,
            price = order.price,
            stock = order.stock,
            imageUrl = order.imageUrl,
            supplier = order.supplier,
        )
    }
}