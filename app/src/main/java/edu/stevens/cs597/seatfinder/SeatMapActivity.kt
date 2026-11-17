package edu.stevens.cs597.seatfinder

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SeatMapActivity : AppCompatActivity() {
    companion object {
        const val BUILDING = "building"
    }

    private var floor1SeatMap: SeatMapView? = null
    private var floor2SeatMap: SeatMapView? = null
    private var floor1Button: Button? = null
    private var floor2Button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seat_map)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        floor1SeatMap = findViewById(R.id.floor_1_seat_map)
        floor2SeatMap = findViewById(R.id.floor_2_seat_map)
        floor1Button = findViewById(R.id.floor1_button)
        floor2Button = findViewById(R.id.floor2_button)

        val buildingId = intent.getStringExtra(BUILDING)
        val building = GlobalSeatMap.buildings.find { building -> building.id == buildingId }
        val floor1 = building?.floors?.find { floor -> floor.level == 1 }
        val floor2 = building?.floors?.find { floor -> floor.level == 2 }

        floor1SeatMap?.floor = floor1
        floor2SeatMap?.floor = floor2

        floor1Button?.setOnClickListener {
            selectFloor1()
        }
        floor2Button?.setOnClickListener {
            selectFloor2()
        }

        selectFloor1()

        findViewById<Button>(R.id.button_back).setOnClickListener {
            finish()
        }
    }

    private fun selectFloor1() {
        floor1SeatMap?.visibility = View.VISIBLE
        floor2SeatMap?.visibility = View.GONE
        floor1Button?.isEnabled = false
        floor2Button?.isEnabled = true
    }

    private fun selectFloor2() {
        floor1SeatMap?.visibility = View.GONE
        floor2SeatMap?.visibility = View.VISIBLE
        floor1Button?.isEnabled = true
        floor2Button?.isEnabled = false
    }
}