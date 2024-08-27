package br.com.dieyteixeira.projetoia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.dieyteixeira.projetoia.ui.components.ChatComponent
import br.com.dieyteixeira.projetoia.ui.theme.AzulCabecalho
import br.com.dieyteixeira.projetoia.ui.viewmodels.ChatViewModel

@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel = viewModel()
) {
    val messages by remember { mutableStateOf(chatViewModel.messages) }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        listState.scrollToItem(messages.size - 1)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(
                    color = AzulCabecalho,
                    shape = RoundedCornerShape(
                        topStart = 30.dp,
                        bottomStart = 5.dp,
                        topEnd = 5.dp,
                        bottomEnd = 30.dp
                    )
                )
        ){
            Text(
                text = "Chat IA",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(16.dp),
                color = Color.White
            )
            Text(
                text = "Codek - Soluções em Tecnologia",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(16.dp),
                color = Color.White
            )
        }
        // Lista de mensagens
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(messages) { message ->
                ChatComponent(message)
            }
        }

        var messageText by rememberSaveable { mutableStateOf("") }

        // Campo de texto e botão de envio
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .imePadding()
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier
                    .weight(1f),
                shape = RoundedCornerShape(40),
                placeholder = {
                    Text(
                        text = "Digite sua mensagem",
                        color = Color.LightGray
                    )
                }
            )
            Button(
                onClick = {
                    if (messageText.isNotBlank()) {
                        chatViewModel.sendMessage(messageText)
                        messageText = ""
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(text = "Enviar")
            }
        }
    }
}