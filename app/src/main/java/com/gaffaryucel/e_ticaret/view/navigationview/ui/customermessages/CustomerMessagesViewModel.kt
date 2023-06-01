package com.gaffaryucel.e_ticaret.view.navigationview.ui.customermessages

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaffaryucel.e_ticaret.model.MessageModel
import com.gaffaryucel.e_ticaret.model.User
import com.gaffaryucel.e_ticaret.viewmodel.CustomerOrder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CustomerMessagesViewModel : ViewModel() {

    val users: MutableLiveData<List<CustomerMessageModel>> = MutableLiveData()
    var userlist = mutableListOf<CustomerMessageModel>()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<Boolean> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

    fun getAllMessages(supplier: String) {
        val databaseUrl = "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
            .child("users")
            .child(supplier)
            .child("message")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userlist = mutableListOf<CustomerMessageModel>()
                dataSnapshot.children.forEach {
                    getUserInfo(it.key.toString(),dataSnapshot.childrenCount.toInt())
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    fun getUserInfo(id : String,count : Int){
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
                    println("equls")
                    // Tüm veriler eklendikten sonra users.value'yu güncelleyin
                    users.value = userlist
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
