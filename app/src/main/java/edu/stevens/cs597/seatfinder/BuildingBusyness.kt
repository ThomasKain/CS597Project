package edu.stevens.cs597.seatfinder

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Shows how busy each building is (the % of seats that are taken).
 * Tapping a building with seat data opens its seat map (SeatMapActivity).
 */
class BuildingBusyness : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_building_busyness)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //back button
        findViewById<Button>(R.id.button_back_busyness).setOnClickListener {
            finish()
        }

        // Buildings that have seat data in GlobalSeatMap
        setUpBuilding("GN", R.id.button_gnorth_busyness, R.id.bar_fill_gnorth,
            R.id.bar_empty_gnorth, R.id.text_gnorth_busyness)
        setUpBuilding("GS", R.id.button_gsouth_busyness, R.id.bar_fill_gsouth,
            R.id.bar_empty_gsouth, R.id.text_gsouth_busyness)

    }

    private fun setUpBuilding(buildingId: String, buttonId: Int, fillId: Int, emptyId: Int, textId: Int) {
        val building = GlobalSeatMap.buildings.find { it.id == buildingId } ?: return

        // Count seats across all floors of this building
        val seats = building.floors.flatMap { it.seats }
        val taken = seats.count { !it.isAvailable }
        val open = seats.size - taken
        val percentTaken = if (seats.isEmpty()) 0 else taken * 100 / seats.size

        // Make the colored part of the bar as wide as the % taken
        val fill = findViewById<View>(fillId)
        val empty = findViewById<View>(emptyId)
        fill.layoutParams = (fill.layoutParams as LinearLayout.LayoutParams).apply {
            weight = percentTaken.toFloat()
        }
        empty.layoutParams = (empty.layoutParams as LinearLayout.LayoutParams).apply {
            weight = (100 - percentTaken).toFloat()
        }

        // Green = quiet, orange = getting busy, red = very busy
        val color = when {
            percentTaken < 40 -> "#2E9E5B"
            percentTaken < 75 -> "#E67E22"
            else -> "#C0392B"
        }
        fill.setBackgroundColor(Color.parseColor(color))

        findViewById<TextView>(textId).text = "$percentTaken% full · $open open"

        // Open this building's seat map
        findViewById<Button>(buttonId).setOnClickListener {
            val intent = Intent(this, SeatMapActivity::class.java)
            intent.putExtra(SeatMapActivity.BUILDING, buildingId)
            startActivity(intent)
        }
    }
}