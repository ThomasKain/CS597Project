package edu.stevens.cs597.seatfinder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //button functionalities

        //Information button
        val popupText = HtmlCompat.fromHtml(
            "<b>Find a Seat:</b> lets you search for open study spots by group size and noise level.<br><br>"
                    + "<b>Browse:</b> lets you look at each building and its floors. You can also favorite seats to be notified about when they are available.",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        findViewById<ImageButton>(R.id.information_button).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("About Seat Finder")
                .setMessage(popupText)
                .setPositiveButton("Ok") {dialog, _ -> dialog.dismiss() }
                .show()
        }

        //Find a seat button
        findViewById<Button>(R.id.button_find_seat).setOnClickListener {
            startActivity(Intent(this, FindSeatEnterInformationActivity::class.java))
        }

        //Browse building and seat button
        findViewById<Button>(R.id.button_browse).setOnClickListener {
            startActivity(Intent(this, BuildingBusyness::class.java))
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}