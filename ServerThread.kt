package com.example.logaction

import android.util.Log
import java.net.ServerSocket
import java.net.Socket
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

class ServerThread(private val socket: Socket) : Thread() {
    override fun run() {
        try {
            Log.d("ServerThread", "Cliente conectado desde ${socket.inetAddress.hostAddress}")

            val input = DataInputStream(socket.getInputStream())
            val output = DataOutputStream(socket.getOutputStream())

            val solicitud = input.readUTF()
            Log.d("ServerThread", "Solicitud recibida: $solicitud")

            if (solicitud == "SOLICITUD_DE_CONEXION") {
                output.writeUTF("ACCEPTED")
                Log.d("ServerThread", "Conexión aceptada")
            }

        } catch (e: IOException) {
            Log.e("ServerThread", "Error en la comunicación", e)
        } finally {
            try {
                socket.close()
                Log.d("ServerThread", "Conexión cerrada")
            } catch (e: IOException) {
                Log.e("ServerThread", "Error al cerrar la conexión", e)
            }
        }
    }
}
