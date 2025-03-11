package com.example.logaction

import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.TextView
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

class ClientThread(
    private val serverIp: String,
    private val serverPort: Int,
    private val handler: Handler,
    private val textView: TextView // Aquí está el TextView como parámetro
) : Thread() {

    override fun run() {
        try {
            Log.d("ClientThread", "Intentando conectar a $serverIp en el puerto $serverPort")
            val socket = Socket(serverIp, serverPort)
            Log.d("ClientThread", "Conexión establecida con el servidor")

            // Configura los flujos de entrada y salida
            val input = DataInputStream(socket.getInputStream())
            val output = DataOutputStream(socket.getOutputStream())

            // Enviar solicitud de conexión al servidor
            output.write("SOLICITUD_DE_CONEXION".toByteArray(Charsets.UTF_8))
            output.flush()

            // Continuar leyendo mensajes del servidor de manera indefinida
            while (true) {
                // Verifica si hay datos disponibles para leer
                if (input.available() > 0) {
                    // Leer la respuesta del servidor
                    val response = input.readUTF()
                    Log.d("ClientThread", "Respuesta del servidor: $response")

                    // Enviar mensaje de vuelta a la UI usando el handler
                    val msg = Message.obtain()
                    msg.obj = response // Asumimos que la respuesta es un texto
                    handler.sendMessage(msg)
                } else {
                    // Puedes hacer alguna otra cosa mientras no hay datos
                    Thread.sleep(100) // Esto previene que el hilo consuma demasiados recursos
                }
            }
        } catch (e: Exception) {
            Log.e("ClientThread", "Error en la conexión", e)
        }
    }
}

