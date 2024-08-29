package br.com.dieyteixeira.projetoia.models

import android.graphics.Bitmap

data class Chat(
    val content: String,
    val isSentByUser: Boolean,
    val bitmap: Bitmap? = null,
    val timestamp: Long = System.currentTimeMillis()
)