package br.com.dieyteixeira.projetoia.models

data class Chat(
    val content: String,
    val isSentByUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)