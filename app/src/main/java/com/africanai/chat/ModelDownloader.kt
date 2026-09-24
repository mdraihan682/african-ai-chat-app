package com.africanai.chat

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment

class ModelDownloader(private val context: Context) {

    private var lastDownloadId: Long = -1

    fun getLastDownloadId(): Long = lastDownloadId

    fun downloadModel() {
        val modelUrl = "https://huggingface.co/Sunbird/Sunflower-14B-GGUF/resolve/main/sunflower-14B-q4_k_m.gguf"
        val fileName = "sunflower-model.gguf"

        val request = DownloadManager.Request(Uri.parse(modelUrl))
            .setTitle("African AI Model Download")
            .setDescription("Downloading model to run offline...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        lastDownloadId = downloadManager.enqueue(request)
    }
}