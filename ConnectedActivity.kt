package com.example.logaction

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.logaction.databinding.ActivityConnectedBinding

class ConnectedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConnectedBinding // Suponiendo que usas ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConnectedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener la IP del servidor desde el Intent
        val serverIp = intent.getStringExtra("SERVER_IP")

        if (serverIp.isNullOrEmpty()) {
            binding.connectedTextView.text = "IP no válida"
            return
        }

        // Crear un Handler para actualizar la UI
        val handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                // Actualiza el TextView con la respuesta del servidor
                val response = msg.obj as String
                binding.connectedTextView.text = response
            }
        }

        // Inicializar el ClientThread y pasar el handler, IP y el TextView
        val clientThread = ClientThread(serverIp, 9000, handler, binding.connectedTextView)
        clientThread.start()
    }
}
