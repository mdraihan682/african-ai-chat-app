package com.africanai.chat

import android.content.Context
import android.os.Environment
import com.ljcamargo.kotlinllamacpp.LlamaHelper
import java.io.File

class LlmInference(context: Context) {
    private val llamaHelper = LlamaHelper(context)
    private val modelFileName = "sunflower-model.gguf"

    fun generateResponse(userPrompt: String): String {
        val modelFile = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), modelFileName)

        if (!modelFile.exists()) {
            return "Model not downloaded. Please download it first."
        }

        val formattedPrompt = "<|im_start|>user\n$userPrompt<|im_end|>\n<|im_start|>assistant\n"

        return try {
            if (!llamaHelper.isModelLoaded) {
                llamaHelper.loadModel(modelFile.absolutePath)
            }
            llamaHelper.generate(formattedPrompt)
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}