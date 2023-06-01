package com.gaffaryucel.e_ticaret.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.UUID

class BasketViewModel : ViewModel() {

    var loading  = MutableLiveData<Boolean>()
    var error  = MutableLiveData<Boolean>()
    var errormessage  = MutableLiveData<String>()
    var basket = MutableLiveData<List<CustomerOrder>>()


    fun getBasket(){
        loading.value = true
        val databaseUrl = "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
            val myRef = database.getReference("referance")
                .child("users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("basket")

            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val productList = mutableListOf<CustomerOrder>()
                    snapshot.children.forEach { productSnapshot ->
                        val product = productSnapshot.getValue(Product::class.java)
                        if (product != null){
                            var newOrder = changeorderforsupplier(product,productSnapshot.key.toString())
                            productList.add(newOrder)
                        }
                    }
                    basket.value = productList
                    loading.value = false
                }
                override fun onCancelled(myerror: DatabaseError) {
                    errormessage.value = myerror.toException().localizedMessage?.toString()
                    error.value = true
                    loading.value = false
                }
            })
    }
    fun finishOrder(orderList: List<CustomerOrder>) {
        deleteFromBeforeOrder()
        val databaseUrl = "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid

        val userOrdersRef = database.getReference("referance")
            .child("users")
            .child(currentUserUid)
            .child("beforeOrder")

        for (i in orderList){
            userOrdersRef.child(i.key.toString()).setValue(i)
        }
    }
    fun deleteFromBeforeOrder() {
        val databaseUrl = "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid

        val userOrdersRef = database.getReference("referance")
            .child("users")
            .child(currentUserUid)
            .child("beforeOrder")
            .removeValue()
            .addOnCompleteListener {

            }.addOnFailureListener {
                println("fail")
            }
    }
    fun changeorderforsupplier(order : Product,key : String) : CustomerOrder{
        var newOrder = CustomerOrder()
        newOrder.id = order.id
        newOrder.name = order.name
        newOrder.description = order.description
        newOrder.category = order.category
        newOrder.price = order.price
        newOrder.stock = order.stock
        newOrder.imageUrl = order.imageUrl
        newOrder.supplier = order.supplier
        newOrder.customer = FirebaseAuth.getInstance().currentUser!!.uid
        newOrder.status = "ordered"
        newOrder.key = key
        return newOrder
    }
}
class CustomerOrder{
    var id: String? = null
    var name: String? = null
    var description: String? = null
    var category: String? = null
    var price: Double? = null
    var stock: Int? = null
    var imageUrl: String? = null
    var supplier : String? = null
    var customer : String? = null
    var status : String? = null
    var key : String? = null
    constructor()
    constructor(
        id: String? = null,
        name: String? = null,
        description: String? = null,
        category: String? = null,
        price: Double? = null,
        stock: Int? = null,
        imageUrl: String? = null,
        supplier : String? = null,
        customer : String? = null,
        status : String? = null,
        key : String? = null,
    ){
        this.id = id
        this.name = name
        this.description = description
        this.category = category
        this.price = price
        this.stock = stock
        this.imageUrl = imageUrl
        this.supplier = supplier
        this.customer = customer
        this.status = status
        this.key = key
    }
}