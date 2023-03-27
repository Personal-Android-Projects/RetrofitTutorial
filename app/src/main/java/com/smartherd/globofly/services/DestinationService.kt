package com.smartherd.globofly.services

import com.smartherd.globofly.models.Destination
import retrofit2.Call
import retrofit2.http.*

// TODO Web Service Interface and Service Builder Step 1 : Create an interface called DestinationService whose main work will be to define functions that will be mapped to our web server end point url functions eg app.get('/destination')
interface DestinationService {



    // The Call interface sends a request to the web server and returns a response defined within the angle brackets <>
    // In this case the response should return a List of Destinations
    //@GET("destination") // Declare to which URL this function will map to
    //fun getDestinationList(@QueryMap filter: HashMap<String, String>): Call<List<Destination>>

    // TODO Using Query Parameters Step 1 :  Use the @Query annotation to determine how the data will be filtered out
   /** @GET("destination") // Declare to which URL this function will map to
    fun getDestinationList(@Query ("country") country: String?): Call<List<Destination>> */

    // TODO Using QueryMap Step 1: Add the map variable that will be passed to the QueryMap
    // If no count value is given the default value set in the server will be used
    // TODO Send information to the server via Headers. These headers can be retrieved from the server
    @Headers("x-device-type: Android","x-foo: bar") // x is used as a prefix since we are not using any of the standard HTTP headers | (headerName: String : headerValue: String) As seen multiple headers can be sent at the same time
    @GET("destination")
    fun getDestinationList(@QueryMap filter : HashMap<String,String>,
                           @Header("Accept-Language") language: String): Call<List<Destination>> // Headers can also be passed as a parameter here
    // TODO Using path parameters to fetch data Step 1 : Define the parameter that will be used as a search key together with the method to fetch the data
    @GET("destination/{id}") // Using {id} means that the id is dynamically filled with the parameter value of the method
    fun getDestination(@Path("id") id: Int): Call<Destination> // TO indicate that the parameter value is to be used in the get URL use @Path("id{parameter variable name}") before the parameter value

    // TODO POST request using JSON Step 1 : Create a method with POST annotation and a URL endpoint
    // @Body annotation is used in order to inform GSON that the passed class needs to be converted to JSON
    @POST("destination")
    fun addDestination(@Body newDestination: Destination): Call<Destination> // Server will return an object whose type is Destination


    // TODO PUT request using FormUrlEncoded Step 1 : Create a method that uses the @FormUrlEncoded and define the different fields of the data you wish to send
    @FormUrlEncoded // Enable our HTTP response to use FormUrlEncoded Format
                    // When this format is used one has to list all the fields of the data you want to send
    @PUT("destination/{id}") // This is a combination of path and query parameters with path being(/destination) and query being({id})
    fun updateDestination(
        @Path("id") id: Int,
        @Field("city") city: String, // The name string values of the field has to match the name of corresponding the values in the server to enable proper mapping
        @Field("description") desc: String,
        @Field("country") country: String
    ): Call<Destination>

    // TODO DELETE request Step 1 : Define a method that will connect to the delete end point in the server
    @DELETE("destination/{id}")
    fun deleteDestination(@Path("id") id: Int): Call<Unit> // Returns unit since we don't return the deleted item
}