package br.com.dieyteixeira.projetoia.ui.viewmodels

import android.util.Log
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel : ViewModel() {

    var selectedOption by mutableStateOf("Flash")
        private set

    private val _switchState = MutableStateFlow(false)
    val switchState: StateFlow<Boolean> = _switchState
    fun setSwitchState(state: Boolean) {
        _switchState.value = state
        updateSelectedOption(if (state) "Pro" else "Flash")
    }

    private val _messages = mutableStateListOf<Chat>()
    val messages: List<Chat> = _messages

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> get() = _uiState

    private var _geminiStile = mutableStateOf(getGeminiStyle(selectedOption))
    val geminiStile: String get() = _geminiStile.value

    private val generativeModel = GenerativeModel(
        modelName = geminiStile,
        apiKey = BuildConfig.apiKey
    )

    private fun getGeminiStyle(option: String): String {
        return if (option == "Flash") "gemini-1.5-flash" else "gemini-1.5-pro"
    }

    fun updateSelectedOption(option: String) {
        selectedOption = option
        _geminiStile.value = getGeminiStyle(option)
    }

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
        Log.e("ChatViewModel", "opções: $geminiStile $selectedOption")
    }
}