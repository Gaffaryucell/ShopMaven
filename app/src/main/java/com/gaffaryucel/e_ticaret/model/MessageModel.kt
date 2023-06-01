package com.gaffaryucel.e_ticaret.model

import java.sql.Timestamp

class MessageModel {
    var messageId: String ? = null
    var sender: String ? = null
    var sendername : String ? = null
    var supplier : Boolean? = null
    var messageContent: String ? = null
    var timestamp: Long? = null
    constructor()
    constructor(messageId : String,sender : String,sendername : String,supplier :Boolean, messageContent : String,timestamp: Long){
        this.messageId = messageId
        this.sender = sender
        this.sendername = sendername
        this.supplier = supplier
        this.messageContent = messageContent
        this.timestamp = timestamp
    }
}