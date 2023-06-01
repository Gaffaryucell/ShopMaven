package com.gaffaryucel.e_ticaret.view.navigationview.ui.signup

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaffaryucel.e_ticaret.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging


class SignUpViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private val auth = FirebaseAuth.getInstance()
    var signUpSucces = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()
    var error = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()

    fun signUp(email: String, password: String,password2 : String,context:Context,type : String,activity : Activity) {
        signUpSucces.value = false
        loading.value = true
        error.value = false
        if(password == password2 && password.isNotEmpty()&&email.isNotEmpty()){
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val myUser = User()
                        myUser.userName = "fatih"
                        myUser.eMail = email
                        myUser.address = "toakt"
                        myUser.phone = "5428313930"
                        myUser.type = type
                        createUser(myUser,activity)
                        verify(context)
                    } else {
                        Toast.makeText(context, "Error try again", Toast.LENGTH_SHORT).show()
                        error.value = true
                        errorMessage.value = task.exception?.localizedMessage ?: "error"
                    }
                }
        }else{
            Toast.makeText(context, "You Have to fill All the blanks", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createUser(myuser : User,activity : Activity) {
        val databaseUrl = "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
        myRef.child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .setValue(myuser)
            .addOnSuccessListener {
                println("success for User")
                val update = UserProfileChangeRequest.Builder()
                    .setDisplayName(myuser.type)
                    .build()
                auth.currentUser!!.updateProfile(update)
                    .addOnSuccessListener {
                        signUpSucces.value = true
                        saveToken()
                    }
            }
            .addOnFailureListener { e ->
                println("fail for User")
                error.value = true
                errorMessage.value = e.localizedMessage
            }
    }
    fun verify(context: Context){
        val current = FirebaseAuth.getInstance().currentUser
        if (current != null) {
            current.sendEmailVerification().addOnCompleteListener {
                println("mail send complate")
                if (it.isSuccessful) {
                    println("mail send succes")
                    val alert = AlertDialog.Builder(context)
                        .setTitle("Verify Alert")
                        .setMessage("Your e-mail address has not been verified please check your mailbox")
                        .setPositiveButton("resend mail", object : DialogInterface.OnClickListener{
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                verify(context)
                            }
                        })
                        .create()
                    alert.show()
                } else {
                    Toast.makeText(context, "Error1", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{
                Toast.makeText(context, "ERROR2", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun saveToken(){
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String?> ->
                if (task.isSuccessful) {
                    val token = task.result
                    val databaseUrl = "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
                    val database = FirebaseDatabase.getInstance(databaseUrl)
                    // Satıcı tablosunda durum değişkenini güncelle
                    val sellerOrderRef = database.getReference("referance")
                        .child("users")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)  // Burada satıcının UUID'si olmalı
                        .child("user_token")
                        .setValue(token)
                } else {
                    // Token alınamadı, hata durumunu işleyin.
                }
            }

    }
}