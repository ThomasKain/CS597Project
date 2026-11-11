package edu.stevens.cs597.seatfinder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Toast

class FindSeatBuildingSelection : AppCompatActivity() {

    companion object {
        const val EXTRA_BUILDING_IDS = "selected_building_ids"
        const val EXTRA_MIN_SEATS = "min_seats"
        const val EXTRA_MAX_NOISE = "max_noise"
    }

    private var selectedIds = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_find_seat_building_selection)

        val buildings = GlobalSeatMap.buildings
        val buildingNames = buildings.map { it.name }.toTypedArray()
        val selectButton = findViewById<Button>(R.id.button_select_buildings)
        val submitButton = findViewById<Button>(R.id.button_submit_buildings)

        selectedIds = savedInstanceState?.getStringArrayList(EXTRA_BUILDING_IDS)
            ?: arrayListOf()

        fun updateSelection() {
            val names = buildings
                .filter { it.id in selectedIds }
                .joinToString { it.name }

            selectButton.text = if (names.isEmpty()) "Select buildings ▾" else "$names ▾"
            submitButton.isEnabled = selectedIds.isNotEmpty()
        }

        updateSelection()

        selectButton.setOnClickListener {
            val checked = BooleanArray(buildings.size) { index ->
                buildings[index].id in selectedIds
            }

            AlertDialog.Builder(this)
                .setTitle("Select buildings")
                .setMultiChoiceItems(buildingNames, checked) { _, index, isChecked ->
                    checked[index] = isChecked
                }
                .setPositiveButton("Done") { _, _ ->
                    selectedIds.clear()
                    buildings.forEachIndexed { index, building ->
                        if (checked[index]) selectedIds.add(building.id)
                    }
                    updateSelection()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        submitButton.setOnClickListener {
            val seatsNeeded = intent.getIntExtra(EXTRA_MIN_SEATS, 0)
            val maxNoiseLevel = intent.getIntExtra(EXTRA_MAX_NOISE, 0)

            if (seatsNeeded !in 1..10 || maxNoiseLevel !in 1..5) {
                Toast.makeText(this, "Go back and enter your seat preferences.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val selectedBuildings = buildings.filter { it.id in selectedIds }

            val matches = findSeatGroups(
                preferredNoiseLevel = maxNoiseLevel,
                seatsNeeded = seatsNeeded,
                buildings = selectedBuildings
            )

            //show the number of matching groups for the prototype.
            val groupCount = matches.sumOf { it.size }
            // Toast.makeText(this, "$matches", Toast.LENGTH_LONG).show()
            Toast.makeText(this, "matching groups: $groupCount", Toast.LENGTH_SHORT).show()

            // Maya, the search result should be able to be read from any activity file through global object SeatSearchResults.matches
            SeatSearchResults.matches = matches
            startActivity(Intent(this, FindSeatBestMatches::class.java))
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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArrayList(EXTRA_BUILDING_IDS, selectedIds)
        super.onSaveInstanceState(outState)
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
                if (floor.noiseLevel > preferredNoiseLevel) continue

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
            Log.d("Submit Search", "$groups")
            groups
        }
    }
}