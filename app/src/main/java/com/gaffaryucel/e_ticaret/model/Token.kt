package com.gaffaryucel.e_ticaret.model

class Token {
    var canceled : Boolean? = null
    var complete: Boolean? = null
    var result: String? = null
    var successful: Boolean? = null
    constructor()
    constructor(canceled : Boolean?,complete: Boolean?,result : String?,successful : Boolean?){
        this.result = result
        this.canceled = canceled
        this.successful = successful
        this.complete = complete
    }
}