package com.gaffaryucel.e_ticaret.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.gaffaryucel.e_ticaret.databinding.RowCustomerMessagesBinding
import com.gaffaryucel.e_ticaret.databinding.RowMessageBinding
import com.gaffaryucel.e_ticaret.model.MessageModel
import com.gaffaryucel.e_ticaret.model.User
import com.gaffaryucel.e_ticaret.view.navigationview.ui.customermessages.CustomerMessageModel
import com.gaffaryucel.e_ticaret.view.navigationview.ui.customermessages.CustomerMessagesFragmentDirections
import com.google.firebase.auth.FirebaseAuth

class CustomerMessageAdapter : RecyclerView.Adapter<CustomerMessageAdapter.MessageHolder>() {

    var customerlist = mutableListOf<CustomerMessageModel>()

    class MessageHolder(val binding: RowCustomerMessagesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerMessageAdapter.MessageHolder {
        val binding = RowCustomerMessagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val customer = customerlist[position]
        holder.binding.customername.text = customer.name
        var supplier = FirebaseAuth.getInstance().currentUser!!.uid
        holder.itemView.setOnClickListener {
            val action = CustomerMessagesFragmentDirections.actionCustomerMessagesFragmentToMessageFragment(customer.id,supplier,"supplier")
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return customerlist.size
    }
}