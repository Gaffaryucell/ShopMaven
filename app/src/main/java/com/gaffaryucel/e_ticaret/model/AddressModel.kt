package com.gaffaryucel.e_ticaret.model

class AddressModel {
    var street: String = ""
    var neighborhood: String = ""
    var city: String = ""
    var district: String = ""
    var postalCode: String = ""
    var main: Boolean? = null

    constructor()

    constructor(street: String, neighborhood: String, city: String, district: String, postalCode: String,main : Boolean? = null) {
        this.street = street
        this.neighborhood = neighborhood
        this.city = city
        this.district = district
        this.postalCode = postalCode
        this.main = main
    }
}