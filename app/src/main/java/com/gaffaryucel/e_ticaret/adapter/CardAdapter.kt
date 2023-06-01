package com.gaffaryucel.e_ticaret.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gaffaryucel.e_ticaret.R
import com.gaffaryucel.e_ticaret.databinding.OrderRowBinding
import com.gaffaryucel.e_ticaret.databinding.RowCardBinding
import com.gaffaryucel.e_ticaret.model.CardModel
import com.gaffaryucel.e_ticaret.viewmodel.CustomerOrder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.ArrayList


class CardAdapter :  RecyclerView.Adapter<CardAdapter.OrderHolder>() {

        class OrderHolder(val binding : RowCardBinding) : RecyclerView.ViewHolder(binding.root)

        var cardlist = ArrayList<CardModel>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
            val binding = RowCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return OrderHolder(binding)
        }

        override fun onBindViewHolder(holder: OrderHolder, position: Int) {
            var isfrontvisible = false
            val mycard = cardlist.get(position)
            holder.binding.cardrowback.setBackgroundResource(R.drawable.card1)
            holder.binding.cardnumtext.visibility = View.VISIBLE
            holder.binding.nameoncardtext.visibility = View.VISIBLE

            holder.binding.mounthoncardtext.visibility = View.INVISIBLE
            holder.binding.yearoncardtext.visibility = View.INVISIBLE
            holder.binding.cvv.visibility = View.INVISIBLE
            holder.binding.slashtext.visibility = View.INVISIBLE
            if (mycard.main!!){
                holder.binding.makemainimage.setBackgroundResource(R.drawable.home2)
            }else{
                holder.binding.makemainimage.setBackgroundResource(R.drawable.home1)
            }
            val mycardInfo = cardlist[position]
            holder.binding.apply {
                cardmodel = mycardInfo
            }
            holder.itemView.setOnClickListener{
                if (isfrontvisible) {
                    holder.binding.cardrowback.setBackgroundResource(R.drawable.card1)
                    holder.binding.cardnumtext.visibility = View.VISIBLE
                    holder.binding.nameoncardtext.visibility = View.VISIBLE

                    holder.binding.mounthoncardtext.visibility = View.INVISIBLE
                    holder.binding.yearoncardtext.visibility = View.INVISIBLE
                    holder.binding.cvv.visibility = View.INVISIBLE
                    holder.binding.slashtext.visibility = View.INVISIBLE
                    isfrontvisible = false
                }else{
                    holder.binding.cardrowback.setBackgroundResource(R.drawable.car2)
                    holder.binding.cardnumtext.visibility = View.INVISIBLE
                    holder.binding.nameoncardtext.visibility = View.INVISIBLE

                    holder.binding.mounthoncardtext.visibility = View.VISIBLE
                    holder.binding.yearoncardtext.visibility = View.VISIBLE
                    holder.binding.cvv.visibility = View.VISIBLE
                    holder.binding.slashtext.visibility = View.VISIBLE
                    isfrontvisible = true
                }
            }
            holder.binding.makemainimage.setOnClickListener{
                makemain(mycard)
                holder.binding.makemainimage.setBackgroundResource(R.drawable.home2)
                for (i in cardlist){
                    if (i == mycard){
                        mycard.main = true
                    }else{
                        i.main = false
                    }
                }
                notifyDataSetChanged()
            }
        }
    override fun getItemCount(): Int {
        return cardlist.size
    }
    fun makemain(card : CardModel){
        val databaseUrl = "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
            .child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("maincard")
            .setValue(card)
    }
}