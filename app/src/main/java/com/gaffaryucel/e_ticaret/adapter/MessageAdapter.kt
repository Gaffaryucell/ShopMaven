package com.gaffaryucel.e_ticaret.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gaffaryucel.e_ticaret.R
import com.gaffaryucel.e_ticaret.databinding.RowAddressBinding
import com.gaffaryucel.e_ticaret.databinding.RowMessageBinding
import com.gaffaryucel.e_ticaret.model.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MessageAdapter(var type : String) : RecyclerView.Adapter<MessageAdapter.MessageHolder>() {
    var messagelist = mutableListOf<MessageModel>()
    class MessageHolder(val binding : RowMessageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapter.MessageHolder {
        val binding = RowMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val message = messagelist[position]
        if (type.equals("customer")){
            if (message.supplier == false) {
                holder.binding.message1.text = message.messageContent
                holder.binding.message1.visibility = View.VISIBLE
                holder.binding.message2.visibility = View.INVISIBLE
            } else {
                holder.binding.message2.text = message.messageContent
                holder.binding.message1.visibility = View.INVISIBLE
                holder.binding.message2.visibility = View.VISIBLE
            }
        }else{
            if (message.supplier == true) {
                holder.binding.message1.text = message.messageContent
                holder.binding.message1.visibility = View.VISIBLE
                holder.binding.message2.visibility = View.INVISIBLE
            } else {
                holder.binding.message2.text = message.messageContent
                holder.binding.message1.visibility = View.INVISIBLE
                holder.binding.message2.visibility = View.VISIBLE
            }
        }
    }
    override fun getItemCount(): Int {
        return messagelist.size
    }

}