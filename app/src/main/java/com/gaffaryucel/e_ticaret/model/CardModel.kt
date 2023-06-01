package com.gaffaryucel.e_ticaret.model

class CardModel {
    var cardname : String? = null
    var name : String? = null
    var num : String? = null
    var mounth : String? = null
    var year : String? = null
    var cvv : String? = null
    var main : Boolean? = null
    constructor()
    constructor(
        cardName : String,
        name : String,
        num : String,
        mounth : String,
        year : String,
        cvv : String,
        main : Boolean
    ){
        this.cardname = cardName
        this.name = name
        this.num = num
        this.mounth = mounth
        this.year = year
        this.cvv = cvv
        this.main = main
    }
}