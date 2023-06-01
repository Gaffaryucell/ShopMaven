package com.gaffaryucel.e_ticaret.service

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.widget.RemoteViews
import android.widget.RemoteViews.RemoteView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.gaffaryucel.e_ticaret.R
import com.gaffaryucel.e_ticaret.model.NotificationModel
import com.gaffaryucel.e_ticaret.view.navigationview.NavigationDrawerActivity
import com.gaffaryucel.e_ticaret.view.navigationview.ui.message.MessageFragment.Companion.isMessagesOpen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.net.HttpURLConnection
import java.net.URL

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private var productImage = ""
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMessageReceived(remotemessage: RemoteMessage) {
        if (remotemessage.notification != null){
            var title = remotemessage.notification!!.title
            var body = remotemessage.notification!!.body
            val where = body?.substringBefore(":")
            when(where){
                "message"->{

                    var name = title?.substringBefore(":")
                    var message = title?.substringAfter(":")
                    var first = body?.substringBefore("/")
                    var type = first?.substringBefore(":")
                    var from = first?.substringAfter(":")
                    var sockend = body?.substringAfter("/")
                    var customer = sockend?.substringBefore(",")
                    var supplier = sockend?.substringAfter(",")

                    var notModel = NotificationModel(from.toString(),customer.toString(),supplier.toString())

                    val description = "you have new message from $name"
                    val newModel = ProductModelForNotification()
                    newModel.senderName = title
                    newModel.message = message
                    newModel.description = description
                    sendNotification(newModel,type.toString(),"",notModel)
                }
                "order"->{
                    var sockend = body?.substringAfter(":")

                    var name = title?.substringBefore(":")
                    var image = title?.substringAfter(":")
                    var type = body?.substringBefore(":")
                    var status = sockend?.substringBefore("/")
                    var id = sockend?.substringAfter("/")

                    var newModel = ProductModelForNotification()
                    var description = "There has been a change about your order named $name"
                    newModel.productNmae  =name
                    newModel.productStatus =status
                    newModel.productImage  =image
                    newModel.description  =description
                    sendNotification(newModel,type.toString(),id.toString(), NotificationModel("","",""))
                }
            }
        }
    }
    // so here we need 3 thinks to do
    // generate the notification
    // attach the notification created with the custom layout
    // show the notification

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification (model : ProductModelForNotification ,where : String,id : String, notmodel : NotificationModel) {
        val bitmap = downloadImage(model.productImage ?: "")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "channel_id"
            val channelName = "channel_name"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, "channel_id")
        val pandingIntent = Intent(this,NavigationDrawerActivity::class.java)
        pandingIntent.putExtra("type","order")
        pandingIntent.putExtra("id",id)
            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                150,
                pandingIntent,
                PendingIntent.FLAG_MUTABLE)

            notificationBuilder.setSmallIcon(R.drawable.profile)
                .setLargeIcon(bitmap)
                .setColor(Color.WHITE)
                .setOnlyAlertOnce(true)
                .setContentTitle(model.productStatus)
                .setSubText(model.productNmae)
                .setContentText(model.description)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(getNotificationId(), notificationBuilder.build())
    }
    private fun getNotificationId(): Int {
        return ((System.currentTimeMillis() % 10000).toInt())
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onNewToken(token: String) {
        // Yeni FCM token'ını sunucunuza kaydedin
        val databaseUrl = "https://shopmaven-b5550-default-rtdb.europe-west1.firebasedatabase.app"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        // Satıcı tablosunda durum değişkenini güncelle
        val sellerOrderRef = database.getReference("referance")
            .child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)  // Burada satıcının UUID'si olmalı
            .child("user_token")
            .setValue(token)
    }
    fun downloadImage(url: String): Bitmap? {
        return try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    inner class ProductModelForNotification{
        var productNmae : String? = null
        var productStatus : String? = null
        var productImage : String? = null
        var senderName : String? = null
        var message : String? = null
        var description : String? = null
        constructor()
        constructor(
            productNmae : String?,
            productStatus : String?,
            productImage : String?,
            senderName : String?,
            message : String?,
            description : String?,
        ){
            this.productNmae = productNmae
            this.productStatus = productStatus
            this.productImage = productImage
            this.senderName = senderName
            this.message = message
            this.description = description
        }
    }
}










  /*  fun generateNotification(title : String, message : String){
        val intent = Intent(this,NavigationDrawerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)

        //chanel id , chanell name
        var builder :  NotificationCompat.Builder =
            NotificationCompat.Builder(
                applicationContext, chanelId
            ).setSmallIcon(R.drawable.home)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(1000,1000,1000,1000))//this make the phone vibrate 1sec V 1sec stop 1 sec V 1sec stop
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title,message))

        val notificationManager  = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChanel = NotificationChannel(chanelId, chanelname,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChanel)
        }
        notificationManager.notify(0,builder.build())
    }
      private fun getRemoteView(title : String, message: String) : RemoteViews{
      val remoteView = RemoteViews("com.gaffaryucel.e_ticaret.service",R.layout.z_notification_layout)
      remoteView.setTextViewText(R.id.titleText,title)
      remoteView.setTextViewText(R.id.description,message)
      remoteView.setImageViewResource(R.id.logoImageView,R.drawable.profile)
      return remoteView
  }*/
