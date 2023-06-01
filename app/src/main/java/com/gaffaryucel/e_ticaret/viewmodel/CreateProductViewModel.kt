package com.gaffaryucel.e_ticaret.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.gaffaryucel.e_ticaret.model.ProductModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class CreateProductViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance().reference
    private val storageRef = FirebaseStorage.getInstance().reference
    private val userId = "FirebaseAuth.getInstance().currentUser!!.uid"

    fun addProduct(product: ProductModel) {
        val imageRef = storageRef.child("$userId/product_images/${UUID.randomUUID()}")
        val uploadTask = imageRef.putFile(product.imageUri!!)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception!!
                println("storage succes")
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                val productId = db.child("products").push().key

                val myproduct = Product(
                    id = productId,
                    name = product.productName,
                    description = product.productDescription,
                    category = product.category,
                    price = product.price,
                    stock = product.stock,
                    imageUrl = downloadUri.toString(),
                    supplier = FirebaseAuth.getInstance().currentUser!!.uid,
                )
                val databaseUrl = "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
                val database = FirebaseDatabase.getInstance(databaseUrl)
                val myRef = database.getReference("referance")
                addInCategory(myproduct)
                myRef.child("allproducts")
                    .child(productId!!)
                    .setValue(myproduct)
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
    fun addInCategory(product: Product) {
        val databaseUrl = "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val myRef = database.getReference("referance")
        myRef.child("categories")
            .child(product.category.toString())
            .child(product.id.toString())
            .setValue(product)
            .addOnSuccessListener {
                println("success for db")
            }
            .addOnFailureListener { e ->
                println("fail for db")
            }
        }
    }
data class Product(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val category: String? = null,
    val price: Double? = null,
    val stock: Int? = null,
    val imageUrl: String? = null,
    val supplier : String? = null,
)