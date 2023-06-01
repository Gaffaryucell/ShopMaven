package com.gaffaryucel.e_ticaret.model

import android.net.Uri

class ProductModel {
    var productName: String? = null
    var productDescription: String? = null
    var category: String? = null
    var price: Double? = null
    var stock: Int? = null
    var imageUri: Uri? = null
    constructor()
    constructor(
        productName: String,
        productDescription: String,
        category: String,
        price: Double,
        stock: Int,
        imageUri: Uri,
    ){
        this.productName = productName
        this.productDescription = productDescription
        this.category = category
        this.price = price
        this.stock = stock
        this.imageUri = imageUri
    }
}