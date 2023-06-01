package com.gaffaryucel.e_ticaret.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.gaffaryucel.e_ticaret.databinding.RowCustomerMessagesBinding
import com.gaffaryucel.e_ticaret.view.navigationview.ui.customermessages.CustomerMessageModel
import com.gaffaryucel.e_ticaret.view.navigationview.ui.customermessages.CustomerMessagesFragmentDirections
import com.gaffaryucel.e_ticaret.view.navigationview.ui.customermessages.messages.MessageOfCustomerFragmentDirections
import com.google.firebase.auth.FirebaseAuth

class SuppliersMessagesAdapter : RecyclerView.Adapter<SuppliersMessagesAdapter.MessageHolder>() {

    var suppliers = mutableListOf<CustomerMessageModel>()

    class MessageHolder(val binding: RowCustomerMessagesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuppliersMessagesAdapter.MessageHolder {
        val binding = RowCustomerMessagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val supplier = suppliers[position]
        holder.binding.customername.text = supplier.name
        var customer = FirebaseAuth.getInstance().currentUser!!.uid
        holder.itemView.setOnClickListener {
            val action = MessageOfCustomerFragmentDirections.actionMessageOfCustomerFragmentToMessageFragment(customer,supplier.id,"ask")
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return suppliers.size
    }
}