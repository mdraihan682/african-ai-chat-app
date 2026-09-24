package com.africanai.chat

import android.content.Context
import android.os.Environment
import java.io.File

class LlmInference(context: Context) {

    private val appContext = context.applicationContext
    private val modelFileName = "sunflower-model.gguf"

    fun generateResponse(userPrompt: String): String {
        val modelFile = File(
            appContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            modelFileName
        )

        if (!modelFile.exists()) {
            return "Model not downloaded yet. Please tap 'Download AI Model'."
        }

        // The model file exists. Real inference will be added in the next phase.
        // For now, we confirm the model is loaded and reply with a friendly message.
        return "Model loaded (${modelFile.length() / 1024 / 1024} MB). " +
                "You said: \"$userPrompt\". " +
                "Native AI inference will be enabled in the next build."
    }
}