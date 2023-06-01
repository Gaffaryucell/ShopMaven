package com.gaffaryucel.e_ticaret.adapter

import com.gaffaryucel.e_ticaret.viewmodel.CustomerOrder

interface CheckBoxListener {
    fun onItemChecked(item: CustomerOrder)
    fun onItemUnchecked(item: CustomerOrder)
}
