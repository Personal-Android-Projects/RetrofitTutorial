package com.smartherd.globofly.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smartherd.globofly.R
import com.smartherd.globofly.databinding.ActivityDestinyDetailBinding
import com.smartherd.globofly.models.Destination
import com.smartherd.globofly.services.DestinationService
import com.smartherd.globofly.services.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DestinationDetailActivity : AppCompatActivity() {

    private var binding: ActivityDestinyDetailBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDestinyDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.detailToolbar)
        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Fetch the ID that has been passed when an item has been clicked
        val bundle: Bundle? = intent.extras
        // Remember the string defined in this class was used as a key in the adapter class in order to pass data via the intent
        // If that key is present it means that the intent from the adapter class has data,the id of the item pressed, otherwise its safe to assume no item has been pressed
        if (bundle?.containsKey(ARG_ITEM_ID)!!) {
            val id = intent.getIntExtra(ARG_ITEM_ID, 0)
            loadDetails(id)
            initUpdateButton(id)
            initDeleteButton(id)
        }
    }

    private fun loadDetails(id: Int) {
        // TODO Using path parameters to fetch data Step 2 : Fetch the data
        // The important thing to note is that the type described in the angle brackets <> for the Call type and the Callback type should be the same
        val destinationService = ServiceBuilder.buildService(DestinationService::class.java)
        val requestCall = destinationService.getDestination(id)
        requestCall.enqueue(object : retrofit2.Callback<Destination> {
            override fun onResponse(call: Call<Destination>, response: Response<Destination>) {
                if (response.isSuccessful) {
                    val destination = response.body()
                    destination?.let {
                        binding?.etCity?.setText(destination.city)
                        binding?.etDescription?.setText(destination.description)
                        binding?.etCountry?.setText(destination.country)
                        binding?.collapsingToolbar?.title = destination.city
                    }
                } else {
                    Toast.makeText(this@DestinationDetailActivity, "Failed to retrieve details", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            override fun onFailure(call: Call<Destination>, t: Throwable) {
                Toast.makeText(
                    this@DestinationDetailActivity,
                    "Failed to retrieve details " + t.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun initUpdateButton(id: Int) {
        binding?.btnUpdate?.setOnClickListener {
            val city = binding?.etCity?.text.toString()
            val description = binding?.etDescription?.text.toString()
            val country = binding?.etCountry?.text.toString()

            val destinationService = ServiceBuilder.buildService(DestinationService::class.java)
            // TODO PUT request using FormUrlEncoded Step 2 : Pass the different items that are meant to be sent in order as defined in the DestinationService
            val requestCall = destinationService.updateDestination(id, city, description, country)
            requestCall.enqueue(object: Callback<Destination> {
                override fun onResponse(call: Call<Destination>, response: Response<Destination>) {
                    if (response.isSuccessful) {
                        finish() // Move back to DestinationListActivity
                        var updatedDestination = response.body() // Use it or ignore It
                        Toast.makeText(this@DestinationDetailActivity,
                            "Item Updated Successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@DestinationDetailActivity,
                            "Failed to update item", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Destination>, t: Throwable) {
                    Toast.makeText(this@DestinationDetailActivity,
                        "Failed to update item", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun initDeleteButton(id: Int) {
        binding?.btnDelete?.setOnClickListener {

            val destinationService = ServiceBuilder.buildService(DestinationService::class.java)
            // TODO DELETE request  Step 2: Use the method from the invoking class
            val requestCall = destinationService.deleteDestination(id)
            requestCall.enqueue(object: Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        finish() // Move back to DestinationListActivity
                        Toast.makeText(this@DestinationDetailActivity, "Successfully Deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@DestinationDetailActivity, "Failed to Delete", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Toast.makeText(this@DestinationDetailActivity, "Failed to Delete", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            navigateUpTo(Intent(this, DestinationListActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val ARG_ITEM_ID = "item_id"
    }
}
