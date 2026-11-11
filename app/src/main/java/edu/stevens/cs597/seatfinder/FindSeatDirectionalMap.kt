package edu.stevens.cs597.seatfinder

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.stevens.cs597.seatfinder.GlobalSeatMap.getBuilding
import edu.stevens.cs597.seatfinder.GlobalSeatMap.getFloor

class FindSeatDirectionalMap : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_find_seat_directional_map)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.button_back).setOnClickListener { finish() }

        val titleView = findViewById<TextView>(R.id.map_title)
        val seatsView = findViewById<TextView>(R.id.map_seats)
        val imageView = findViewById<ImageView>(R.id.map_image)

        val floorId = intent.getStringExtra("FLOOR_ID")
        val seatIds = intent.getStringArrayListExtra("SEAT_IDS") ?: arrayListOf()
        val floor = floorId?.let { GlobalSeatMap.getFloor(it) }
        val building = floor?.let { GlobalSeatMap.getBuilding(it.buildingId) }

        if (floor == null || building == null) {
            titleView.text = "Building not found"
            return
        }

        titleView.text = building.name

        if (seatIds.isNotEmpty()) {
            seatsView.text = if (seatIds.size > 1)
                "${floor.label}: Seats ${seatIds.first()}-${seatIds.last()}"
            else
                "${floor.label}: Seat ${seatIds.first()}"
        }

        val imageName = "directions_" + building.id.lowercase()
        val resId = resources.getIdentifier(imageName, "drawable", packageName)
        if (resId != 0) {
            imageView.setImageResource(resId)
        } else {
            seatsView.text = "No directions image: $imageName"
        }
    }
}