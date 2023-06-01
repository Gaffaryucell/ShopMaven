package com.gaffaryucel.e_ticaret.view.navigationview.ui.incomingorder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaffaryucel.e_ticaret.viewmodel.CustomerOrder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class IncomingOrderViewModel : ViewModel() {

    var orders = MutableLiveData<List<CustomerOrder>>()
    var loading  = MutableLiveData<Boolean>()
    var error  = MutableLiveData<Boolean>()
    var errormessage  = MutableLiveData<String>()

    fun showIncomingOrder(){
        loading.value = true
        val databaseUrl =
            "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
            .child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("incomingorders")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<CustomerOrder>()
                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(CustomerOrder::class.java)
                    productList.add(product!!)
                }
                loading.value = false
                orders.value = productList
            }
            override fun onCancelled(myerror: DatabaseError) {
                errormessage.value = myerror.toException().localizedMessage?.toString()
                error.value = true
                loading.value = false
            }
        })
    }
}