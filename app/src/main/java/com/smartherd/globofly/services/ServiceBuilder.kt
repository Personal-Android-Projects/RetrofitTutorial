package com.smartherd.globofly.services

import android.os.Build
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

// TODO Web Service Interface and Service Builder Step 2 : Create an object that will instantiate the DestinationService interface. This object will be used to access any method we define in the DestinationService interface. The connection to the webserver is also defined here
object ServiceBuilder {
    // https://stackoverflow.com/questions/42505735/avc-denied-read-for-name-dev-rootfs-ino-1-scontext-uruntrusted-app
    // Before release, change this URL to your live server URL such as "https://smartherd.com/"
    // http://10.0.2.2:9000/ localhost used for android virtual devices
    // http://10.0.3.2:9000/ localhost used for genymotion devices
    // 192.168.56.1
    private const val URL = "http://10.0.3.2:9000/" // This ip together with the port number is defined in the server.js file

    // TODO Log HTTP Request and Response Step 2: Create Logger
    // Level key word determines how much you want to monitor your HTTP connection
    // The two main ones are Level.BODY and Level.BASIC
    // Level.BASIC monitors 
    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    // TODO Web Service Interface and Service Builder Step 3: Create OkHttp Client
    // This client will be used to create the Retrofit builder
    private val okHttp = OkHttpClient.Builder()
            // END step 3
                                        .callTimeout(5, TimeUnit.SECONDS)
                                        .addInterceptor(logger)

    // TODO Web Service Interface and Service Builder Step 4 Create Retrofit Builder
    private val builder = Retrofit.Builder().baseUrl(URL)
                                        .addConverterFactory(GsonConverterFactory.create()) // Integrate GSON
                                        .client(okHttp.build()) // Attach the client as okHTTP
    // End of step 4

    // TODO Web Service Interface and Service Builder Step 5 Create Retrofit Instance
    private val retrofit = builder.build()

    // TODO Web Service Interface and Service Builder Step 6 Create a function that will create an implementation of the API endpoints as defined by the DestinationService interface. The serviceType variable is any class that implements DestinationService
    // Can be interpreted as because of this function the methods in the DestinationService become live
    // It is used to create an anonymous inner class object to implement the Destination service
    fun <T> buildService(serviceType: Class<T>): T {
        return retrofit.create(serviceType)
    }
}