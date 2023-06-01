package com.gaffaryucel.e_ticaret.view.navigationview.ui.profile.profiledetails

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaffaryucel.e_ticaret.model.ProductModel
import com.gaffaryucel.e_ticaret.model.User
import com.gaffaryucel.e_ticaret.viewmodel.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.HashMap

class ProfileDetailsViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val user  = MutableLiveData<User>()
    var loading  = MutableLiveData<Boolean>()
    var error  = MutableLiveData<Boolean>()
    var errormessage  = MutableLiveData<String>()
    private val db = FirebaseDatabase.getInstance().reference
    private val storageRef = FirebaseStorage.getInstance().reference
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid

    fun getProfileDetails(id : String){
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
    fun changeUserName(name : String){
        val databaseUrl = "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
        myRef.child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("userName")
            .setValue(name)
            .addOnSuccessListener {
                println("success for name")
            }
            .addOnFailureListener { e ->
                println("fail for User")
            }
    }
    fun changeEmail(email : String){
        val databaseUrl = "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
        myRef.child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("email")
            .setValue(email)
            .addOnSuccessListener {
                println("success for name")
            }
            .addOnFailureListener { e ->
                println("fail for User")
            }
    }
    fun changeProfilPhoto(imageUri: Uri) {
        val imageRef = storageRef.child("$userId/profilphoto/profilphoto.jpg")
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception!!
                println("storage succes")
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result

                val databaseUrl = "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
                val database = FirebaseDatabase.getInstance(databaseUrl)
                val myRef = database.getReference("referance")
                myRef.child("users")
                    .child(userId)
                    .child("photo")
                    .setValue(downloadUri.toString())
                    .addOnSuccessListener {
                        println("success for db")
                    }
                    .addOnFailureListener { e ->
                        println("fail for db")
                    }
            } else {
                println(task.exception?.localizedMessage ?: "error")
            }
        }
    }
}