package com.example.logaction

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.logaction.databinding.ActivityLocateBinding
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

class LocateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.locateButton.setOnClickListener {
            val serverIp = binding.locateCodeBox.text.toString().trim()
            if (serverIp.isEmpty()) {
                binding.statusTextView.text = "Ingresa una IP válida"
                return@setOnClickListener
            }

            // Ejecutamos la conexión en un hilo para no bloquear el hilo principal
            Thread {
                try {
                    Log.d("LocateActivity", "Conectando a $serverIp en el puerto 9000")

                    // Conectamos al servidor
                    val socket = Socket(serverIp, 9000)
                    val output = DataOutputStream(socket.getOutputStream())
                    val input = DataInputStream(socket.getInputStream())

                    // Enviar solicitud de ubicación al servidor usando writeUTF
                    val solicitud = "SOLICITUD_DE_UBICACION"
                    Log.d("LocateActivity", "Solicitud enviada: $solicitud")
                    output.writeUTF(solicitud)  // Usar writeUTF para enviar la cadena
                    output.flush()
                    Log.d("LocateActivity", "Solicitud de ubicación enviada al servidor")

                    // Esperar la respuesta del servidor
                    val response = input.readUTF()  // Leer la respuesta completa del servidor
                    Log.d("LocateActivity", "Respuesta recibida: $response")

                    // Cerrar la conexión
                    socket.close()

                    // Verificar si la respuesta es de tipo LOCATED
                    if (response.startsWith("LOCATED")) {
                        // Pasar los datos de ubicación a LocatedActivity y mostrar la información
                        val intent = Intent(this, LocatedActivity::class.java)
                        intent.putExtra("LOCATION_INFO", response)  // Pasa la información al siguiente Activity
                        startActivity(intent)
                        finish()
                    } else {
                        // Si no es LOCATED, mostrar un mensaje de error
                        runOnUiThread {
                            Toast.makeText(this, "Error: No se pudo obtener la ubicación.", Toast.LENGTH_LONG).show()
                        }
                    }

                } catch (e: Exception) {
                    Log.e("LocateActivity", "Error de conexión", e)
                    runOnUiThread {
                        Toast.makeText(this, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }.start() // Iniciar el hilo
        }
    }
}
