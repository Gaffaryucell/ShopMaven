package com.gaffaryucel.e_ticaret.view.navigationview.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.gaffaryucel.e_ticaret.model.User
import com.gaffaryucel.e_ticaret.viewmodel.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileViewModel : ViewModel() {

    val user  = MutableLiveData<User>()
    var loading  = MutableLiveData<Boolean>()
    var error  = MutableLiveData<Boolean>()
    var errormessage  = MutableLiveData<String>()

    fun getProfilInfo(id : String){
        val databaseUrl =
            "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
            .child("users")
            .child(id)
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var userInfos  = snapshot.getValue(User::class.java) ?: User()
                if (userInfos != null){
                    user.value = userInfos
                }
                loading.value = false
            }
            override fun onCancelled(myerror: DatabaseError) {

            }
        })
    }
}