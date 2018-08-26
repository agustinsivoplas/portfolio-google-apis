package com.uy.atomico.googleapisportfolio.data.api

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.uy.atomico.googleapisportfolio.BuildConfig
import com.uy.atomico.googleapisportfolio.extensions.formatToDateTime
import com.uy.atomico.googleapisportfolio.services.PortfolioFirebaseMessageService
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.util.*

/**
 * Created by agustin.sivoplas@gmail.com on 8/15/18.
 * Atomico Labs
 */

object PushAPI {

    fun sendPushNotification() {
        val data = HashMap<String, String>()
        data[PortfolioFirebaseMessageService.KEY_TITLE] = "Push from Atomico Labs"
        data[PortfolioFirebaseMessageService.KEY_MESSAGE] = "Works like a charm!!! ${Date().formatToDateTime()}"

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            val contentType = MediaType.parse("application/json; charset=utf-8")
            val serverKey = "key=" + BuildConfig.FIREBASE_CLOUD_MESSAGE_KEY

            val params = HashMap<String, Any>()

            params["to"] = it.token
            params["data"] = data

            val json = JSONObject(params).toString()

            val client = OkHttpClient()

            val body = RequestBody.create(contentType, json)
            val request = Request.Builder()
                    .url("https://fcm.googleapis.com/fcm/send")
                    .header("Authorization", serverKey)
                    .post(body).build()


            Completable.fromCallable {
                val response = client.newCall(request).execute()
                Log.i("PushAPI", response.toString())
            }.subscribeOn(Schedulers.io()).subscribe()
        }
    }
}