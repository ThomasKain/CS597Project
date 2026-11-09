package edu.stevens.cs597.seatfinder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
    }
    fun findSeatGroups(
        preferredNoiseLevel: Int,
        seatsNeeded: Int,
        buildings: List<Building>
    ): List<List<List<Seat>>> {
        require(preferredNoiseLevel in 1..5)
        require(seatsNeeded > 0)

        return buildings.map { building ->
            val groups = mutableListOf<List<Seat>>()

            for (floor in building.floors) {
                if (floor.noiseLevel != preferredNoiseLevel) continue

                val availableSeats = floor.seats
                    .filter { it.isAvailable && it.floorId == floor.id }
                    .map { seat -> seat.id.toInt() to seat }
                    .sortedBy { (id, _) -> id }

                var group = mutableListOf<Seat>()
                var previousId: Int? = null

                for ((id, seat) in availableSeats) {
                    val previous = previousId
                    if (previous != null && id.toLong() != previous.toLong() + 1) {
                        if (group.size >= seatsNeeded) groups.add(group)
                        group = mutableListOf()
                    }

                    group.add(seat)
                    previousId = id
                }

                if (group.size >= seatsNeeded) groups.add(group)
            }

            groups
        }
    }
}