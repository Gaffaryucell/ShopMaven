package com.gaffaryucel.e_ticaret.view.navigationview.ui.address.newaddress

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaffaryucel.e_ticaret.model.AddressModel
import com.gaffaryucel.e_ticaret.model.CardModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class AddNewAddressViewModel : ViewModel() {
    var loading  = MutableLiveData<Boolean>()
    var error  = MutableLiveData<Boolean>()
    var errormessage  = MutableLiveData<String>()
    var success = MutableLiveData<Boolean>()

    fun addNewAddress(address : AddressModel){
        loading.value = true
        val databaseUrl = "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
            .child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("address")
            .child(UUID.randomUUID().toString())
            .setValue(address).addOnSuccessListener {
                success.value = true
            }
    }
}