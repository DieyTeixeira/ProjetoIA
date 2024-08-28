package br.com.dieyteixeira.projetoia.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import br.com.dieyteixeira.projetoia.R
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.dieyteixeira.projetoia.ui.components.ChatComponent
import br.com.dieyteixeira.projetoia.ui.theme.Azul
import br.com.dieyteixeira.projetoia.ui.theme.AzulCabecalho
import br.com.dieyteixeira.projetoia.ui.viewmodels.ChatViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel = viewModel()
) {
    val messages by remember { mutableStateOf(chatViewModel.messages) }
    val uiState by chatViewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        listState.animateScrollToItem(messages.size - 1)
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
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 8.dp)
            ){
                Text(
                    text = "Chat IA",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    color = Color.White
                )
                Image(
                    painter = painterResource(id = R.drawable.logocodek_allwhite),
                    contentDescription = "Logo da IA",
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.CenterVertically),
                )
            }
        }
        // Lista de mensagens
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 5.dp)
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
                .padding(start = 15.dp, end = 10.dp, bottom = 10.dp)
                .imePadding()
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 30.dp),
                shape = RoundedCornerShape(30.dp),
                placeholder = {
                    Text(
                        text = "Digite sua mensagem",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Azul,  // Cor da borda ao focar
                    unfocusedBorderColor = Color.Gray   // Cor da borda quando não está focado
                ),
            )
            Box(
                modifier = Modifier
                    .height(55.dp)
                    .width(50.dp)
                    .align(Alignment.Bottom)
            ){
                Image(
                    painter = painterResource(id = R.drawable.send_message),
                    contentDescription = "Enviar",
                    modifier = Modifier
                        .size(35.dp)
                        .align(Alignment.Center)
                        .clickable {
                            if (messageText.isNotBlank()) {
                                keyboardController?.hide()
                                chatViewModel.sendMessage(messageText)
                                messageText = ""
                            }
                        }
                )
            }
        }
    }
}