package br.com.dieyteixeira.projetoia.ui.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.dieyteixeira.projetoia.BuildConfig
import br.com.dieyteixeira.projetoia.UiState
import br.com.dieyteixeira.projetoia.models.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel : ViewModel() {

    private val _messages = mutableStateListOf<Chat>()
    val messages: List<Chat> = _messages

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey
    )

    fun sendMessage(content: String) {
        _messages.add(Chat(content = content, isSentByUser = true))
        _uiState.value = UiState.Loading

        val chatHistory = _messages.joinToString(separator = "\n") { message ->
            if (message.isSentByUser) {
                "Usuário: ${message.content}"
            } else {
                "IA: ${message.content}"
            }
        }

        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    generativeModel.generateContent(
                        content {
                            text("Por favor, responda em português, considerando o histórico do chat: \n$chatHistory\nUsuário: $content")
                        }
                    )
                }
                response.text?.let { outputContent ->
                    _messages.add(Chat(content = outputContent, isSentByUser = false))
                    _uiState.value = UiState.Success(outputContent)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "Erro desconhecido")
            }
        }
    }
}