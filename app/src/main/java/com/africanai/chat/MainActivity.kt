package com.africanai.chat

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var chatHistory: TextView
    private lateinit var scrollView: ScrollView
    private lateinit var inputField: EditText
    private lateinit var sendButton: Button
    private lateinit var downloadButton: Button
    private lateinit var llm: LlmInference
    private lateinit var downloader: ModelDownloader

    private val modelFileName = "sunflower-model.gguf"

    private val downloadReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloader.getLastDownloadId()) {
                chatHistory.append("AI: Model downloaded! You can now chat.\n\n")
                downloadButton.visibility = View.GONE
                enableChat(true)
                scrollView.post { scrollView.fullScroll(View.FOCUS_DOWN) }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        llm = LlmInference(this)
        downloader = ModelDownloader(this)

        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        registerReceiver(downloadReceiver, filter)

        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(30, 30, 30, 30)
        }

        scrollView = ScrollView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
            )
        }
        chatHistory = TextView(this).apply {
            textSize = 16f
            text = "AI: Hello! I am your offline AI assistant. Ask me anything.\n\n"
        }
        scrollView.addView(chatHistory)
        mainLayout.addView(scrollView)

        val inputLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 20, 0, 0)
        }
        inputField = EditText(this).apply {
            hint = "Type your message..."
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }
        sendButton = Button(this).apply { text = "Send" }
        inputLayout.addView(inputField)
        inputLayout.addView(sendButton)
        mainLayout.addView(inputLayout)

        downloadButton = Button(this).apply {
            text = "Download AI Model (Wi-Fi only)"
        }
        mainLayout.addView(downloadButton)

        setContentView(mainLayout)

        val modelFile = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), modelFileName)
        if (modelFile.exists()) {
            chatHistory.append("AI: Model found. Ready to chat!\n\n")
            downloadButton.visibility = View.GONE
            enableChat(true)
        } else {
            enableChat(false)
        }

        sendButton.setOnClickListener {
            val userMessage = inputField.text.toString().trim()
            if (userMessage.isNotEmpty()) {
                chatHistory.append("You: $userMessage\n")
                inputField.text.clear()
                scrollView.post { scrollView.fullScroll(View.FOCUS_DOWN) }

                CoroutineScope(Dispatchers.IO).launch {
                    val aiResponse = llm.generateResponse(userMessage)
                    withContext(Dispatchers.Main) {
                        chatHistory.append("AI: $aiResponse\n\n")
                        scrollView.post { scrollView.fullScroll(View.FOCUS_DOWN) }
                    }
                }
            }
        }

        downloadButton.setOnClickListener {
            Toast.makeText(this, "Download starting on Wi-Fi...", Toast.LENGTH_LONG).show()
            chatHistory.append("AI: Starting model download... Please wait.\n\n")
            downloadButton.visibility = View.GONE
            downloader.downloadModel()
        }
    }

    private fun enableChat(enabled: Boolean) {
        inputField.isEnabled = enabled
        sendButton.isEnabled = enabled
        if (!enabled) {
            inputField.hint = "Download model first..."
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(downloadReceiver)
        } catch (e: Exception) {
            // Ignore
        }
    }
}