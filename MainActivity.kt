package com.example.logaction

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.logaction.databinding.ActivityMainBinding // Importa el binding generado

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa el binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura los listeners de los botones
        binding.shareButton.setOnClickListener {
            startActivity(Intent(this, ShareActivity::class.java))
        }

        binding.connectButton.setOnClickListener {
            startActivity(Intent(this, ConnectActivity::class.java))
        }

        binding.locateButton.setOnClickListener {
            startActivity(Intent(this, LocateActivity::class.java))
        }
    }
}
