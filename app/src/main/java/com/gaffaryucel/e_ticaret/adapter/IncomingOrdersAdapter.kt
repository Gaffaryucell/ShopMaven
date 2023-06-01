package com.gaffaryucel.e_ticaret.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gaffaryucel.e_ticaret.databinding.OrderRowBinding
import com.gaffaryucel.e_ticaret.view.navigationview.ui.incomingorder.IncomingOrderFragment
import com.gaffaryucel.e_ticaret.view.navigationview.ui.incomingorder.IncomingOrderFragmentDirections
import com.gaffaryucel.e_ticaret.viewmodel.CustomerOrder

class IncomingOrdersAdapter : RecyclerView.Adapter<IncomingOrdersAdapter.OrderHolder>() {

    class OrderHolder(val binding: OrderRowBinding) : RecyclerView.ViewHolder(binding.root)

    var orderList = ArrayList<CustomerOrder>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        val binding = OrderRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        val myProduct = orderList[position]
        holder.binding.apply {
            order = myProduct
            executePendingBindings()
        }
        Glide.with(holder.itemView.context)
            .load(myProduct.imageUrl)
            .into(holder.binding.productImage)
        holder.itemView.setOnClickListener {
            var action = IncomingOrderFragmentDirections.actionIncomingOrderFragmentToOrderDetailsFragment(orderList.get(position).id.toString())
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}

