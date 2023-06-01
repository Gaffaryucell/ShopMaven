package com.gaffaryucel.e_ticaret.view.navigationview.ui.message

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaffaryucel.e_ticaret.model.MessageModel
import com.gaffaryucel.e_ticaret.model.Notification
import com.gaffaryucel.e_ticaret.model.NotificationBody
import com.gaffaryucel.e_ticaret.model.NotificationModel
import com.gaffaryucel.e_ticaret.retrofit.FcmInterface
import com.gaffaryucel.e_ticaret.viewmodel.CustomerOrder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.UUID

class MessageViewModel : ViewModel() {
    var messages = MutableLiveData<List<MessageModel>>()
    var senderName = MutableLiveData<String>()
    var loading  = MutableLiveData<Boolean>()
    var error  = MutableLiveData<Boolean>()
    var errormessage  = MutableLiveData<String>()
    var saved  = MutableLiveData<Boolean>()
    var token  = MutableLiveData<String>()

    fun sendmessage(message: MessageModel,supplier : String,customer :String) {
   // Yeni bir benzersiz mesaj kimliği oluşturulur
  // Mesaj nesnesinin kimliği güncellenir
        val databaseUrl = "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
            .child("users")
            .child(supplier)
            .child("message")
            .child(customer)
            .child(message.messageId.toString())
            .setValue(message)

        if (saved.value != true){
            var ref2 = database.getReference("referance")
                .child("users")
                .child(customer)
                .child("suppliermessages")
                .child(supplier)
                .setValue(supplier)
            saved.value = true
        }
    }
    fun getAllMessages(supplier : String,customer : String){
        val databaseUrl =
            "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
            .child("users")
            .child(supplier)
            .child("message")
            .child(customer)
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messageList = mutableListOf<MessageModel>()
                for (productSnapshot in snapshot.children) {
                    val messagevalue = productSnapshot.getValue(MessageModel::class.java)
                    if (messagevalue != null){
                        messageList.add(messagevalue)
                    }
                }
                messageList.sortBy { it.timestamp } // Artan sıralama (eski tarih önce)
                messages.value = messageList
                loading.value = false
            }
            override fun onCancelled(myerror: DatabaseError) {
                errormessage.value = myerror.toException().localizedMessage?.toString()
                error.value = true
                loading.value = false
            }
        })
    }
    fun getSenderName(id : String){
        val databaseUrl =
            "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
            .child("users")
            .child(id)
            .child("userName")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                senderName.value = snapshot.getValue().toString()
                loading.value = false
            }
            override fun onCancelled(myerror: DatabaseError) {
                errormessage.value = myerror.toException().localizedMessage?.toString()
                error.value = true
                loading.value = false
            }
        })
    }
    fun isSupplierSaved(supplier : String,customer : String){
        val databaseUrl =
            "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
            .child("users")
               .child(customer)
            .child("suppliermessages")
            .child(supplier)
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                saved.value = snapshot.getValue() != null
            }
            override fun onCancelled(myerror: DatabaseError) {
                errormessage.value = myerror.toException().localizedMessage?.toString()
                error.value = true
                loading.value = false
            }
        })
    }
    fun sendToUser(sendername: String, message : String,user_token: String,model : NotificationModel) {
        val BASE_URL = "https://fcm.googleapis.com/"

        val service = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(FcmInterface::class.java)

        val notification123 = Notification(
            to = user_token,
            notification = NotificationBody(
                title = "$sendername:$message",
                body = "message:${model.from}/${model.customer},${model.supplier}"
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
            .orderByValue()
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                token.value = snapshot.getValue().toString()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}