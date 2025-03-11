package com.example.logaction

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.logaction.databinding.ActivityShareBinding
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.ServerSocket
import java.net.Socket

class ShareActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShareBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ip = getIpAddress()
        binding.ipTextView.setText(Editable.Factory.getInstance().newEditable(ip))
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.copyButton.setOnClickListener {
            copyToClipboard(ip)
        }

        // Llamar a startServerThread() para iniciar el servidor
        startServerThread()
    }

    private fun getIpAddress(): String {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo: WifiInfo = wifiManager.connectionInfo
        val ipAddress = wifiInfo.ipAddress
        return String.format(
            "%d.%d.%d.%d",
            (ipAddress and 0xFF),
            (ipAddress shr 8 and 0xFF),
            (ipAddress shr 16 and 0xFF),
            (ipAddress shr 24 and 0xFF)
        )
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("IP Address", text)
        clipboard.setPrimaryClip(clip)
    }

    private fun startServerThread() {
        Thread {
            try {
                val serverSocket = ServerSocket(9000)
                val socket = serverSocket.accept()
                serverSocket.close() // Cerrar después de aceptar la conexión

                runOnUiThread {
                    val ipAddress = socket.inetAddress.hostAddress
                    val port = socket.port

                    val intent = Intent(this, PermissionsActivity::class.java)
                    intent.putExtra("ipAddress", ipAddress)
                    intent.putExtra("port", port)
                    startActivity(intent)
                    finish() // Cerrar ShareActivity después de la conexión
                }
            } catch (e: Exception) {
                Log.e("ShareActivity", "Error al aceptar conexión", e)
            }
        }.start()
    }
}