package com.gaffaryucel.e_ticaret.model

class User {
    var userName : String? = null
    var eMail : String? = null
    var address : String? = null
    var phone : String? = null
    var type : String? = null
    var photo : String? = null
    var usertoken : Token? = null

    constructor()
    constructor(
        userName : String,eMail : String,address : String,phone : String,photo : String
    ){
        this.userName = userName
        this.eMail = eMail
        this.address = address
        this.phone = phone
        this.photo = photo
    }
}