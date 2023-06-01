package com.gaffaryucel.e_ticaret.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gaffaryucel.e_ticaret.databinding.OrderRowBinding
import com.gaffaryucel.e_ticaret.databinding.ProductRowBinding
import com.gaffaryucel.e_ticaret.view.navigationview.ui.gallery.GalleryFragmentDirections
import com.gaffaryucel.e_ticaret.view.navigationview.ui.home.HomeFragmentDirections
import com.gaffaryucel.e_ticaret.viewmodel.CustomerOrder
import com.gaffaryucel.e_ticaret.viewmodel.Product

class OrderAdapter :  RecyclerView.Adapter<OrderAdapter.OrderHolder>() {

    class OrderHolder(val binding : OrderRowBinding) : RecyclerView.ViewHolder(binding.root)
    var orderList = ArrayList<CustomerOrder>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        val binding = OrderRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OrderHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        val myProduct = orderList[position]
        println("orders : "+myProduct.name)
        println("orders : "+myProduct.status)
        holder.binding.apply {
            order = myProduct
            executePendingBindings()
        }
        Glide.with(holder.itemView.context)
            .load(myProduct.imageUrl)
            .into(holder.binding.productImage)
        holder.itemView.setOnClickListener{
            val action = GalleryFragmentDirections.actionNavGalleryToOrderDetailsForCustomer(myProduct.id.toString())
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

}