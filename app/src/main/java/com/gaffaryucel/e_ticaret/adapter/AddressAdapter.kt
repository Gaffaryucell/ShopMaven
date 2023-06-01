package com.gaffaryucel.e_ticaret.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gaffaryucel.e_ticaret.R
import com.gaffaryucel.e_ticaret.databinding.RowAddressBinding
import com.gaffaryucel.e_ticaret.model.AddressModel
import com.gaffaryucel.e_ticaret.model.CardModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddressAdapter() : RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    var addressList =  ArrayList<AddressModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val address = addressList[position]
        holder.bind(address)
        try {
            if (address.main!!){
                holder.binding.imageView2.setImageResource(R.drawable.home2)
            }else{
                holder.binding.imageView2.setImageResource(R.drawable.home1)
            }
        }catch (e : Exception){

        }
        holder.binding.imageView2.setOnClickListener{
            makemain(address)
            holder.binding.imageView2.setImageResource(R.drawable.home2)
            for (i in addressList){
                if (i == address){
                    address.main = true
                }else{
                    i.main = false
                }
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    inner class ViewHolder(val binding: RowAddressBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(address: AddressModel) {
            binding.address = address
            binding.executePendingBindings()
        }
    }
    fun makemain(address : AddressModel){
        val databaseUrl = "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
            .child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("mainaddress")
            .setValue(address)
    }

}
