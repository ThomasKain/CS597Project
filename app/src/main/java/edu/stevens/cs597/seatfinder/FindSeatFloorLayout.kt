package edu.stevens.cs597.seatfinder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.stevens.cs597.seatfinder.GlobalSeatMap.getFloor

class FindSeatFloorLayout : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_find_seat_floor_layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //values passed from button on best seat matches to display correct floor layout picture
        val floorId = intent.getStringExtra("FLOOR_ID")
        val seatIds = intent.getStringArrayListExtra("SEAT_IDS") ?: arrayListOf()
        val floor = floorId?.let { GlobalSeatMap.getFloor(it) }

        val title = findViewById<TextView>(R.id.map_title)
        val seats = findViewById<TextView>(R.id.map_seats)
        val image = findViewById<ImageView>(R.id.map_image)

        if (floor == null) {
            title.text = "Floor not found"
        } else {
            title.text = floor.label

            if (seatIds.isNotEmpty()) {
                seats.text = if (seatIds.size > 1)
                    "Seats ${seatIds.first()}-${seatIds.last()}"
                else
                    "Seat ${seatIds.first()}"
            }

            image.setImageResource(floor.mapImage)
        }

        //send same information to directional layout page to make sure the right map shows up
        findViewById<Button>(R.id.button_take_me_there).setOnClickListener {
            startActivity(Intent(this, FindSeatDirectionalMap::class.java).putExtras(intent))
        }

        //back button
        findViewById<Button>(R.id.button_back).setOnClickListener {
            finish()
        }
    }
}