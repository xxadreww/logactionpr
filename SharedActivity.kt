package com.example.logaction

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.logaction.databinding.ActivitySharedBinding

class SharedActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySharedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        // Mostrar un mensaje de que la conexión fue exitosa
        binding.statusTextView.text = "Conexión exitosa. Los datos se están compartiendo."

        // Botón para detener la conexión
        binding.stopButton.setOnClickListener {
            // Aquí puedes agregar la lógica para detener la conexión
            finish()
        }
    }
}