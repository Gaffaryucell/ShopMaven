package com.gaffaryucel.e_ticaret.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gaffaryucel.e_ticaret.R
import com.gaffaryucel.e_ticaret.databinding.ProductRowBinding
import com.gaffaryucel.e_ticaret.databinding.RowBasketBinding
import com.gaffaryucel.e_ticaret.view.BasketFragmentDirections
import com.gaffaryucel.e_ticaret.view.navigationview.ui.home.HomeFragmentDirections
import com.gaffaryucel.e_ticaret.viewmodel.CustomerOrder
import com.gaffaryucel.e_ticaret.viewmodel.Product


class BasketAdapter(private val checkboxListener: CheckBoxListener) : RecyclerView.Adapter<BasketAdapter.ProductHolder>() {
    private val checkedItems = mutableListOf<CustomerOrder>()

    inner class ProductHolder(val binding : RowBasketBinding): RecyclerView.ViewHolder(binding.root){
    }
    var productList = ArrayList<CustomerOrder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val binding = RowBasketBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        println(productList.get(0).imageUrl)
        return ProductHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        val myProduct = productList[position]
        holder.binding.apply {
            product = myProduct
            executePendingBindings()
        }
        Glide.with(holder.itemView.context)
            .load(myProduct.imageUrl)
            .into(holder.binding.productImage)
        holder.itemView.setOnClickListener{
            val action = BasketFragmentDirections.actionNavBasketToProductDetailsFragment(myProduct.key!!,"basket")
            Navigation.findNavController(it).navigate(action)
        }
        holder.binding.basketcheckbox.setOnCheckedChangeListener(null)
        holder.binding.basketcheckbox.isChecked = checkedItems.contains(myProduct)

        holder.binding.basketcheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (!checkedItems.contains(myProduct)) {
                    checkedItems.add(myProduct)
                    checkboxListener.onItemChecked(myProduct)
                }
            } else {
                if (checkedItems.contains(myProduct)) {
                    checkedItems.remove(myProduct)
                    checkboxListener.onItemUnchecked(myProduct)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}