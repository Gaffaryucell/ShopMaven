package com.gaffaryucel.e_ticaret.view.navigationview.ui.buy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaffaryucel.e_ticaret.adapter.OrderAdapter
import com.gaffaryucel.e_ticaret.databinding.FragmentGalleryBinding
import com.gaffaryucel.e_ticaret.model.AddressModel
import com.gaffaryucel.e_ticaret.model.CardModel
import com.gaffaryucel.e_ticaret.view.navigationview.ui.gallery.GalleryViewModel
import com.gaffaryucel.e_ticaret.viewmodel.CustomerOrder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class BuyViewModel : ViewModel() {

    var orders = MutableLiveData<List<CustomerOrder>>()
    var address = MutableLiveData<String>()
    var cardname = MutableLiveData<String>()
    var loading  = MutableLiveData<Boolean>()
    var error  = MutableLiveData<Boolean>()
    var errormessage  = MutableLiveData<String>()
    var allproduct = MutableLiveData<List<CustomerOrder>>()

    fun getOrdered() {
        loading.value = true
        val databaseUrl =
            "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
            .child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("beforeOrder")
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

    fun getCardName() {
        loading.value = true
        val databaseUrl =
            "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
            .child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("maincard")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var card = snapshot.getValue(CardModel::class.java)
                cardname.value = card?.cardname
            }
            override fun onCancelled(myerror: DatabaseError) {
                errormessage.value = myerror.toException().localizedMessage?.toString()
                error.value = true
                loading.value = false
            }
        })
    }
    fun getAddressTitle(){
        loading.value = true
        val databaseUrl =
            "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
            .child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("mainaddress")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var useraddress = snapshot.getValue(AddressModel::class.java)
                address.value = useraddress?.city+"/"+useraddress?.district+"/"+useraddress?.neighborhood
            }
            override fun onCancelled(myerror: DatabaseError) {
                errormessage.value = myerror.toException().localizedMessage?.toString()
                error.value = true
                loading.value = false
            }
        })
    }
    fun finishOrder(orderList: List<CustomerOrder>) {
        val databaseUrl = "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid

        val userOrdersRef = database.getReference("referance")
            .child("users")
            .child(currentUserUid)
            .child("orders")


        for (i in orderList) {
            userOrdersRef.child(i.key.toString()).setValue(i)
            val supplierUid = i.supplier
            val supplierIncomingOrdersRef = database.getReference("referance")
                .child("users")
                .child(supplierUid.toString())
                .child("incomingorders")
                .child(i.key.toString())
                .setValue(i).addOnFailureListener {

                }
            val myRef = database.getReference("referance")
                .child("users")
                .child(currentUserUid)
                .child("basket")
                .child(i.id.toString())
                .removeValue()
        }
    }
}