package com.gaffaryucel.e_ticaret.view.navigationview.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaffaryucel.e_ticaret.model.ProductModel
import com.gaffaryucel.e_ticaret.viewmodel.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeViewModel : ViewModel() {

    var products = MutableLiveData<List<Product>>()
    var orderEdProduct = MutableLiveData<List<Product>>()
    var loading  = MutableLiveData<Boolean>()
    var error  = MutableLiveData<Boolean>()
    var errormessage  = MutableLiveData<String>()
    var oneProduct = MutableLiveData<Product>()
    var name = MutableLiveData<String>()



    fun getProducts() {
        //userName
            loading.value = true
            val databaseUrl =
                "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
            val database = FirebaseDatabase.getInstance(databaseUrl)
            val myRef = database.getReference("referance")
                .child("allproducts")
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val productList = mutableListOf<Product>()
                    snapshot.children.forEach { productSnapshot ->
                        val product = productSnapshot.getValue(Product::class.java)
                        productList.add(product!!)
                    }
                    loading.value = false
                    products.value = productList
                }
                override fun onCancelled(myerror: DatabaseError) {
                    errormessage.value = myerror.toException().localizedMessage?.toString()
                    error.value = true
                    loading.value = false
                }
            })
    }
    fun getSupplireName(id : String){
        val databaseUrl =
            "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
            .child("users")
            .child(id)
            .child("userName")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                name.value  = snapshot.getValue().toString()
            }
            override fun onCancelled(myerror: DatabaseError) {

            }
        })
    }
    fun orderByCategory(category : String){
        loading.value = true
        val databaseUrl = "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
            .child("categories")
            .child(category)

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<Product>()
                snapshot.children.forEach { productSnapshot ->
                    val myproduct = productSnapshot.getValue(Product::class.java)
                    productList.add(myproduct!!)
                }
                loading.value = false
                orderEdProduct.value = productList
            }
            override fun onCancelled(myerror: DatabaseError) {
                errormessage.value = myerror.toException().localizedMessage?.toString()
                error.value = true
                loading.value = false
            }
        })
    }
    fun addInBasket(product : Product){
        val databaseUrl = "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
            .child("users")
        myRef.child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("basket")
            .child(product.id.toString())
            .setValue(product)
            .addOnSuccessListener {
                println("success for basket")
            }
            .addOnFailureListener { e ->
                println("fail for basket")
            }
    }
}