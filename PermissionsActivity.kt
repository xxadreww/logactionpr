package com.example.logaction

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.ServerSocket
import java.net.Socket

class PermissionsActivity : AppCompatActivity() {

    private lateinit var statusTextView: TextView
    private lateinit var acceptButton: Button
    private lateinit var rejectButton: Button
    private var socket: Socket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissions)

        statusTextView = findViewById(R.id.statusTextView)
        acceptButton = findViewById(R.id.allowButton)
        rejectButton = findViewById(R.id.denyButton)

        // Recuperar IP y puerto del Intent
        val ipAddress = intent.getStringExtra("ipAddress")
        val port = intent.getIntExtra("port", -1)

        Log.d("PermissionsActivity", "Conexión desde IP: $ipAddress, Puerto: $port")

        // Aquí puedes usar esta información, por ejemplo, crear un socket con esos datos.

        Thread {
            try {
                Log.d("PermissionsActivity", "Iniciando servidor en el puerto 9000")
                val serverSocket = ServerSocket(9000)

                runOnUiThread {
                    statusTextView.text = "Esperando conexión..."
                }

                socket = serverSocket.accept()
                Log.d("PermissionsActivity", "Cliente conectado")

                val input = DataInputStream(socket!!.getInputStream())
                val output = DataOutputStream(socket!!.getOutputStream())

                val solicitud = input.readUTF().trim()
                Log.d("PermissionsActivity", "Solicitud recibida: $solicitud")

                if (solicitud == "SOLICITUD_DE_CONEXION") {
                    runOnUiThread {
                        statusTextView.text = "Solicitud de conexión recibida"
                        acceptButton.visibility = View.VISIBLE
                        rejectButton.visibility = View.VISIBLE
                    }

                    acceptButton.setOnClickListener {
                        Log.d("PermissionsActivity", "Conexión aceptada")
                        output.writeUTF("ACCEPTED")
                        startActivity(Intent(this, ConnectedActivity::class.java))
                    }

                    rejectButton.setOnClickListener {
                        Log.d("PermissionsActivity", "Conexión rechazada")
                        output.writeUTF("REJECTED")
                    }
                }

            } catch (e: Exception) {
                Log.e("PermissionsActivity", "Error en el servidor", e)
                runOnUiThread {
                    statusTextView.text = "Error en el servidor: ${e.message}"
                }
            }
        }.start()
    }


    override fun onDestroy() {
        super.onDestroy()
        // Asegurarse de cerrar el socket si la actividad se destruye
        socket?.close()
    }
}
