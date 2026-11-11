package edu.stevens.cs597.seatfinder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.stevens.cs597.seatfinder.GlobalSeatMap.getBuilding
import edu.stevens.cs597.seatfinder.GlobalSeatMap.getFloor
import androidx.core.view.isEmpty

class FindSeatBestMatches : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_find_seat_best_matches)

        //buttons
        //dyanmic buttons for best seats that take you to correct directional map
        val container = findViewById<LinearLayout>(R.id.results_container)

        //going through matched seats to create correct displays and buttons for each
        SeatSearchResults.matches.forEach {buildingGroups ->
            val groups = buildingGroups.filter { it.isNotEmpty() }
            if (groups.isEmpty()) return@forEach

            val building = GlobalSeatMap.getFloor(groups.first().first().floorId)
                ?.let {GlobalSeatMap.getBuilding(it.buildingId)}

            val header = TextView(this)
            header.text = building?.name ?: "Unknown Building"
            header.textSize = 20f
            container.addView(header)

            groups.forEach { group->
                val floorId = group.first().floorId
                val floor = GlobalSeatMap.getFloor(floorId)

                val nums = group.map {it.id}
                val range = if (nums.size > 1) "${nums.first()}-${nums.last()}" else nums.first()

                val btn = Button(this)
                btn.text = "${floor?.label ?: floorId}\nSeats $range"
                btn.setOnClickListener {
                    val i = Intent(this, FindSeatFloorLayout::class.java)
                    i.putExtra("FLOOR_ID", floorId)
                    i.putStringArrayListExtra("SEATS_IDS", ArrayList(nums))
                    startActivity(i)
                }
                container.addView(btn)
            }
        }

        //if not matches were found
        if(container.isEmpty()) {
            container.addView(TextView(this).apply {text = "No matching seats found"})
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
    }
}