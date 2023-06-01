package com.gaffaryucel.e_ticaret.view.navigationview.ui.customermessages.messages

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaffaryucel.e_ticaret.view.navigationview.ui.customermessages.CustomerMessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageOfCustomerViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val users: MutableLiveData<List<CustomerMessageModel>> = MutableLiveData()
    var userlist = mutableListOf<CustomerMessageModel>()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<Boolean> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

    fun getAllSuppliers() {
        val databaseUrl = "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
            .child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("suppliermessages")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children){
                    getSupplierInfo(data.getValue().toString(),dataSnapshot.childrenCount.toInt())
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    fun getSupplierInfo(id : String,count : Int){
        val databaseUrl = "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
            .child("users")
            .child(id)
            .child("userName")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val username = dataSnapshot.getValue().toString()
                val model = CustomerMessageModel(id,username)
                userlist.add(model)
                if (userlist.size == count) {
                    users.value = userlist
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}