package com.gaffaryucel.e_ticaret.view.navigationview.ui.orderdetails

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaffaryucel.e_ticaret.model.Notification
import com.gaffaryucel.e_ticaret.model.NotificationBody
import com.gaffaryucel.e_ticaret.retrofit.FcmInterface
import com.gaffaryucel.e_ticaret.viewmodel.CustomerOrder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import java.net.URL

class OrderDetailsViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    var incomingorder = MutableLiveData<CustomerOrder>()
    var userToken = MutableLiveData<String>()
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
            .child("incomingorders")
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

    fun updateStatusAndSyncWithCustomer(productidinsuplire: String, status: String,currentUserUid : String) {
        val databaseUrl = "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)

        // Satıcı tablosunda durum değişkenini güncelle
        val sellerOrderRef = database.getReference("referance")
            .child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)  // Burada satıcının UUID'si olmalı
            .child("incomingorders")
            .child(productidinsuplire)
            .child("status")

        sellerOrderRef.setValue(status).addOnSuccessListener {
            // Müşteri tablosunda durum değişkenini güncelle
            val customerOrdersRef = database.getReference("referance")
                .child("users")  // Burada müşterinin UUID'si olmalı
                .child(currentUserUid)
                .child("orders")
                .child(productidinsuplire)
                .child("status")
                .setValue(status)
        }
    }

    fun sendToUser(newStatus : String,productName : String,productImage : String,user_token : String,id : String){

        val BASE_URL = "https://fcm.googleapis.com/"

        val service = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(FcmInterface::class.java)

        val notification123 = Notification(
            to = user_token,
            notification = NotificationBody(
                title = "$productName:$productImage",
                body = "order:$newStatus/$id,",
            )
        )

        val call = service.sendNotification(notification123)
        call.enqueue(object : Callback<RequestBody> {
            override fun onResponse(call: Call<RequestBody>, response: Response<RequestBody>) {

            }

            override fun onFailure(call: Call<RequestBody>, t: Throwable) {

            }
        })
    }
    fun getToken(id : String) {
        val databaseUrl =
            "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
            .child("users")
            .child(id)
            .child("user_token")
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val s = snapshot.getValue()
                userToken.value = s.toString()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}
