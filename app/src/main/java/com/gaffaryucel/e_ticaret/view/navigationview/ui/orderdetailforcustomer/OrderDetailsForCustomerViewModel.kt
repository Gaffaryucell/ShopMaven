package com.gaffaryucel.e_ticaret.view.navigationview.ui.orderdetailforcustomer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaffaryucel.e_ticaret.viewmodel.CustomerOrder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrderDetailsForCustomerViewModel : ViewModel() {
    var incomingorder = MutableLiveData<CustomerOrder>()
    var loading  = MutableLiveData<Boolean>()
    var error  = MutableLiveData<Boolean>()
    var errormessage  = MutableLiveData<String>()
    fun getOrder(key : String){
        loading.value = true
        val databaseUrl =
            "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
            .child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("orders")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<CustomerOrder>()
                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(CustomerOrder::class.java)
                    if (product!=null){
                        productList.add(product)
                    }
                }
                for (i in productList){
                    if (i.key == key){
                        incomingorder.value = i
                        break
                    }
                }
                loading.value = false
            }
            override fun onCancelled(myerror: DatabaseError) {
                errormessage.value = myerror.toException().localizedMessage?.toString()
                error.value = true
                loading.value = false
            }
        })
    }

}