package com.gaffaryucel.e_ticaret.retrofit

import com.gaffaryucel.e_ticaret.model.Notification
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FcmInterface {
    @Headers(
        "Content-Type:application/json",
        "Authorization:key=AAAA86tgXpg:APA91bHmEUtvQYr4Ut_K2JwoybTUrUMlNGXVCbH2YO9y6geyp7Pb9baap1MrZVtUe5O6TtwUzzrD_G-jzSq2pysZtAnbiuu2FIRkEzBQjoATO4TKqSBudBJLWUmR94LOGDzj6EwJclau"
    )
    @POST("fcm/send")
    fun sendNotification(@Body body: Notification): Call<RequestBody>
}