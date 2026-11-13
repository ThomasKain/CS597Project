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
            startActivity(Intent(this, SeatMapActivity::class.java))
        }
        findViewById<Button>(R.id.button_browse_gateway_south).setOnClickListener {
            startActivity(Intent(this, SeatMapActivity::class.java))
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