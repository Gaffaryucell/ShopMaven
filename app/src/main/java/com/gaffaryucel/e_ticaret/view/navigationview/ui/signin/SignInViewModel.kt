package com.gaffaryucel.e_ticaret.view.navigationview.ui.signin

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaffaryucel.e_ticaret.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging

class SignInViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private val auth = FirebaseAuth.getInstance()
    var signInSucces = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()
    var error = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()

    fun signIn(email : String,password : String,context : Context){
        loading.value = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveToken()
                    if (task.result.user!!.isEmailVerified){
                        Toast.makeText(context, "Giriş Yapılabilir", Toast.LENGTH_SHORT).show()
                        signInSucces.value = true
                        loading.value = false
                    }else{
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
                    }
                } else {
                    loading.value = false
                    error.value = true
                    errorMessage.value = task.exception?.localizedMessage ?: "error"
                }
            }
    }
    fun verify(context: Context){
        val current = FirebaseAuth.getInstance().currentUser
        if (current != null) {
            current.sendEmailVerification().addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Mail sent please confirm your e-mail address ",
                        Toast.LENGTH_SHORT
                    ).show()
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
                    // Token'i kullanma veya kaydetme işlemlerini burada yapabilirsiniz.
                } else {
                    // Token alınamadı, hata durumunu işleyin.
                }
            }
    }
}