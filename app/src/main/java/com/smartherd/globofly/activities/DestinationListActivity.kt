package com.smartherd.globofly.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smartherd.globofly.R
import com.smartherd.globofly.databinding.ActivityDestinyListBinding
import com.smartherd.globofly.helpers.DestinationAdapter
import com.smartherd.globofly.models.Destination
import com.smartherd.globofly.services.DestinationService
import com.smartherd.globofly.services.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DestinationListActivity : AppCompatActivity() {

    private var binding: ActivityDestinyListBinding? = null
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
        binding = ActivityDestinyListBinding.inflate(layoutInflater)
		setContentView(binding?.root)

		setSupportActionBar(binding?.toolbar)
        binding?.toolbar?.title = title

        binding?.fab?.setOnClickListener {
			val intent = Intent(this@DestinationListActivity, DestinationCreateActivity::class.java)
			startActivity(intent)
		}
	}

    // TODO POST request using JSON Step 3 : Refresh the recyclerView to enable up to date information to be displayed
	override fun onResume() {
		super.onResume()
		loadDestinations()
	}

	private fun loadDestinations() {

        // TODO Make GET request and receive response using retrofit Step 1: Create an anonymous inner class that implements the Destination Service
        // val destinationService is now a variable that has an instance of a class that implements DestinationService
		val destinationService = ServiceBuilder.buildService(DestinationService::class.java)

        // TODO Make GET request and receive response using retrofit Step 2: Use the destinationService to access the interface method that is meant to fetch the list. At this point you have not sent a request, you are just accessing the method.
        //val requestCall = destinationService.getDestinationList(filter)

        // TODO Using QueryMap Step 2: Define the hashmap that will be used
        val filter = HashMap<String, String>()
        // TODO Using QueryMap Step 3: Add the Key,Value paris of your choice
        /** filter["country"] = "India"
        filter["count"] = "1" */
        // TODO Using Query Parameters Step 2 : Alter the getDestinationsList method to match as it is described
        // If the query parameter passed at this point is null/empty retrofit will ignore the query parameter and return the entire list
        // Set string type as nullable in order to allow null values
        val requestCall = destinationService.getDestinationList(filter,"EN")
        // TODO Make GET request and receive response using retrofit Step 3: Send a request asynchronously,in the background, using the enqueue method.
        // The enqueue method takes as a parameter a CallBack object that will communicate responses from the server
        requestCall.enqueue(object: Callback<List<Destination>> {

            // If you receive a HTTP Response, then this method is executed
            // Your STATUS Code will decide if your Http Response is a Success or Error
            override fun onResponse(call: Call<List<Destination>>, response: Response<List<Destination>>) {
                if (response.isSuccessful) {
                    // Your status code is in the range of 200's
                    val destinationList = response.body()!! // .body() returns the list of destinations
                    binding?.destinyRecyclerView?.adapter = DestinationAdapter(destinationList)
                } else if(response.code() == 401) {
                    Toast.makeText(this@DestinationListActivity,
                        "Your session has expired. Please Login again.", Toast.LENGTH_LONG).show()
                } else { // Application-level failure
                    // Your status code is in the range of 300's, 400's and 500's
                    Toast.makeText(this@DestinationListActivity, "Failed to retrieve items", Toast.LENGTH_LONG).show()
                }
            }

            // Invoked in case of Network Error or Establishing connection with Server
            // or Error Creating Http Request or Error Processing Http Response
            override fun onFailure(call: Call<List<Destination>>, t: Throwable) {
                Toast.makeText(this@DestinationListActivity, "Error Occurred" + t.toString(), Toast.LENGTH_LONG).show()
            }
        })
    }
}









