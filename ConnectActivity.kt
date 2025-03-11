package com.example.logaction

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.logaction.databinding.ActivityConnectBinding
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

// ConnectActivity.kt
class ConnectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConnectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConnectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.connectButton.setOnClickListener {
            val serverIp = binding.connectCodeBox.text.toString().trim()
            if (serverIp.isEmpty()) {
                binding.statusTextView.text = "Ingresa una IP válida"
                return@setOnClickListener
            }

            // Ejecutamos la conexión en un hilo para no bloquear el hilo principal
            Thread {
                try {
                    Log.d("ConnectActivity", "Conectando a $serverIp en el puerto 9000")
                    val socket = Socket(serverIp, 9000)
                    val input = DataInputStream(socket.getInputStream())
                    val output = DataOutputStream(socket.getOutputStream())

                    // Enviar solicitud de conexión
                    output.write("SOLICITUD_DE_CONEXION".toByteArray(Charsets.UTF_8))
                    output.flush()
                    output.flush()  // Asegurarse de que los datos se envíen inmediatamente
                    Log.d("ConnectActivity", "Solicitud de conexión enviada")

                    // Esperar la respuesta del servidor
                    val reader = input.bufferedReader()
                    val response = reader.readLine()
                    Log.d("ConnectActivity", "Respuesta recibida: $response")

                    runOnUiThread {
                        if (response == "ACCEPTED") {
                            binding.statusTextView.text = "Conexión aceptada"
                            val intent = Intent(this, ConnectedActivity::class.java)
                            intent.putExtra("SERVER_IP", serverIp) // Pasa la IP al siguiente Activity
                            startActivity(intent)
                            finish()
                        } else {
                            binding.statusTextView.text = "Conexión rechazada"
                        }
                    }

                    socket.close() // Cerrar el socket
                    Log.d("ConnectActivity", "Socket cerrado")

                } catch (e: Exception) {
                    Log.e("ConnectActivity", "Error de conexión", e)
                    runOnUiThread {
                        binding.statusTextView.text = "Error de conexión: ${e.message}"
                    }
                }
            }.start() // Iniciar el hilo
        }
    }
}
