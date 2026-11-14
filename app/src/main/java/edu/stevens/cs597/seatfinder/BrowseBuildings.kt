package edu.stevens.cs597.seatfinder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BrowseBuildings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_browse_buildings)

        //buttons
        findViewById<Button>(R.id.button_browse_gateway_north).setOnClickListener {
            val intent = Intent(this, SeatMapActivity::class.java)
            intent.putExtra(SeatMapActivity.BUILDING, "GN")
            startActivity(intent)
        }
        findViewById<Button>(R.id.button_browse_gateway_south).setOnClickListener {
            val intent = Intent(this, SeatMapActivity::class.java)
            intent.putExtra(SeatMapActivity.BUILDING, "GS")
            startActivity(intent)
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