package edu.stevens.cs597.seatfinder

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Shows one seat and the "Get Notified When This Spot Opens Up" button.
 *
 * To open this page from another page:
 *     startActivity(SeatDetailActivity.newIntent(this, floor.id, seat.id))
 */
class SeatDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_FLOOR_ID = "floorId"
        const val EXTRA_SEAT_ID = "seatId"

        fun newIntent(context: Context, floorId: String, seatId: String): Intent =
            Intent(context, SeatDetailActivity::class.java)
                .putExtra(EXTRA_FLOOR_ID, floorId)
                .putExtra(EXTRA_SEAT_ID, seatId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seat_detail)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //back button
        findViewById<Button>(R.id.button_back).setOnClickListener {
            finish()
        }

        // 1. Read which seat we were asked to show
        val floorId = intent.getStringExtra(EXTRA_FLOOR_ID)
        val seatId = intent.getStringExtra(EXTRA_SEAT_ID)

        // 2. Look the seat up in GlobalSeatMap
        val floor = GlobalSeatMap.buildings.flatMap { it.floors }.find { it.id == floorId }
        val seat = floor?.seats?.find { it.id == seatId }

        if (floor == null || seat == null) {
            Toast.makeText(this, "Seat not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // 3. Fill in the text on the page
        findViewById<TextView>(R.id.text_seat_title).text = "Seat ${seat.id}"
        findViewById<TextView>(R.id.text_seat_location).text = floor.label
        findViewById<TextView>(R.id.text_noise_level).text =
            "Noise level: ${floor.noiseLevel} / 5"

        val statusText = findViewById<TextView>(R.id.text_seat_status)
        if (seat.isAvailable) {
            statusText.text = "Available now"
            statusText.setTextColor(Color.parseColor("#2E9E5B"))
        } else {
            statusText.text = "Taken"
            statusText.setTextColor(Color.parseColor("#757575"))
        }

        // 4. The notify (favorite) button
        val notifyButton = findViewById<Button>(R.id.button_notify)

        fun updateNotifyButton() {
            notifyButton.text = if (FavoriteSeats.isFavorite(floor.id, seat.id)) {
                "✓ You'll Be Notified (Tap to Cancel)"
            } else {
                "Get Notified When This Spot Opens Up"
            }
        }

        updateNotifyButton()

        notifyButton.setOnClickListener {
            val nowSaved = FavoriteSeats.toggle(floor.id, seat.id)
            val message = if (nowSaved) {
                "We'll notify you when Seat ${seat.id} opens up"
            } else {
                "Notification cancelled"
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            updateNotifyButton()
        }

        // 5. Info button: explains what the notify button does
        findViewById<ImageButton>(R.id.information_button).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Seat Notifications")
                .setMessage(
                    "Tap \"Get Notified When This Spot Opens Up\" to save this seat. " +
                        "When it becomes free, we'll send you a notification.\n\n" +
                        "Tap the button again to stop notifications for this seat."
                )
                .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }
}
