package com.africanai.chat

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var chatHistory: TextView
    private lateinit var scrollView: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Create the main layout
        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(30, 30, 30, 30)
        }

        // 2. Chat history display (scrollable)
        scrollView = ScrollView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        }
        chatHistory = TextView(this).apply {
            textSize = 16f
            text = "AI: Hello! I am your offline AI assistant. Ask me anything.\n\n"
        }
        scrollView.addView(chatHistory)
        mainLayout.addView(scrollView)

        // 3. Input row (EditText + Button)
        val inputLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 20, 0, 0)
        }

        val inputField = EditText(this).apply {
            hint = "Type your message..."
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
        }

        val sendButton = Button(this).apply {
            text = "Send"
        }

        inputLayout.addView(inputField)
        inputLayout.addView(sendButton)
        mainLayout.addView(inputLayout)

        setContentView(mainLayout)

        // 4. Handle Send button click
        sendButton.setOnClickListener {
            val userMessage = inputField.text.toString().trim()
            if (userMessage.isNotEmpty()) {
                // Add user message to chat
                chatHistory.append("You: $userMessage\n")
                inputField.text.clear()

                // Simulate an AI response (we will replace this with the real model later)
                chatHistory.append("AI: [Thinking...]\n")
                
                // Scroll to bottom
                scrollView.post { scrollView.fullScroll(View.FOCUS_DOWN) }

                // Fake delayed response
                scrollView.postDelayed({
                    chatHistory.append("AI: This is a placeholder response. The real AI model will be integrated soon!\n\n")
                    scrollView.post { scrollView.fullScroll(View.FOCUS_DOWN) }
                }, 1000)
            }
        }
    }
}