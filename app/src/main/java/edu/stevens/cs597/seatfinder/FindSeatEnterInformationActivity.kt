package edu.stevens.cs597.seatfinder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Spinner

class FindSeatEnterInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_find_seat_enter_information)

        //button functionality
        //submit seat information (# and noise level) button
        findViewById<Button>(R.id.button_submit_seat_reqs).setOnClickListener {
            startActivity(Intent(this, FindSeatBuildingSelection::class.java))
        }

        //back button
        findViewById<Button>(R.id.button_back).setOnClickListener {
            finish()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViewById<Button>(R.id.button_submit_seat_reqs).setOnClickListener {
            val seatsNeeded = findViewById<Spinner>(R.id.spinner_seat_count)
                .selectedItem.toString().toInt()

            val maxNoiseLevel = findViewById<Spinner>(R.id.spinner_noise_level)
                .selectedItem.toString().toInt()

            val selectionIntent = Intent(this, FindSeatBuildingSelection::class.java)
                .putExtra(FindSeatBuildingSelection.EXTRA_MIN_SEATS, seatsNeeded)
                .putExtra(FindSeatBuildingSelection.EXTRA_MAX_NOISE, maxNoiseLevel)

            startActivity(selectionIntent)
        }
    }

}