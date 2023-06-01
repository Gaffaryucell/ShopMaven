package com.gaffaryucel.e_ticaret.view.navigationview.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaffaryucel.e_ticaret.viewmodel.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class SearchViewModel : ViewModel() {
    var searchList = MutableLiveData<ArrayList<Product>>()
    var loading  = MutableLiveData<Boolean>()
    var error  = MutableLiveData<Boolean>()
    var errormessage  = MutableLiveData<String>()
    fun search(){
        loading.value = true
        val databaseUrl = "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance").child("allproducts")
        val searchQuery = "anahtarKelime" // Kullanıcının arama yaptığı anahtar kelime
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val searchResults = mutableListOf<Product>()
                dataSnapshot.children.forEach { productSnapshot ->
                    val product = productSnapshot.getValue(Product::class.java)
                    // Ürün adında veya diğer alanlarda anahtar kelimeye eşleşme kontrolü yapın
                    if (product != null && (product.name!!.contains(searchQuery, ignoreCase = true) || product.description!!.contains(searchQuery, ignoreCase = true))) {
                        searchResults.add(product)
                    }
                }
                searchList.value?.addAll(searchResults)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Hata durumunda gerekli işlemleri yapabilirsiniz
            }
        })

    }
}