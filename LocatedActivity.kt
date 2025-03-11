package com.example.logaction

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.logaction.databinding.ActivityLocatedBinding

class LocatedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocatedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocatedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        // Obtener la información de localización pasada desde LocateActivity
        val locationInfo = intent.getStringExtra("LOCATION_INFO") ?: "No se pudo obtener la información"

        // Mostrar la información en el TextView
        val locationTextView: TextView = findViewById(R.id.locationInfo)
        locationTextView.text = locationInfo
    }
}
