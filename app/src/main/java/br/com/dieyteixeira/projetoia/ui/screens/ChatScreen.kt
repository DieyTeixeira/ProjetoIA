package br.com.dieyteixeira.projetoia.ui.screens

import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import br.com.dieyteixeira.projetoia.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.dieyteixeira.projetoia.ui.components.ChatComponent
import br.com.dieyteixeira.projetoia.ui.theme.AzulCabecalho

import br.com.dieyteixeira.projetoia.ui.components.SwitchButton
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.zIndex
import br.com.dieyteixeira.projetoia.ui.viewmodels.ChatViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel = viewModel(),
    onCaptureImage: () -> Unit,
    onFindImage: () -> Unit
) {
    val messages by remember { mutableStateOf(chatViewModel.messages) }
    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val switchState by chatViewModel.switchState.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    val selectedOption by remember { derivedStateOf { chatViewModel.selectedOption } }
    val imageBitmap by chatViewModel.imageBitmap.collectAsState() // Estado para a imagem capturada

    var showOptions by remember { mutableStateOf(false) }

    val isTextComplete by chatViewModel.isTextComplete.collectAsState(true)

    LaunchedEffect(isTextComplete) {
        if (messages.isNotEmpty()) {
            if (!isTextComplete) {
                while (!isTextComplete) {
                    val maxIndex = messages.size + 2
                    listState.animateScrollToItem(maxIndex)
                    delay(200) // Rolagem a cada segundo
                }
            } else {
                val maxIndex = messages.size
                listState.animateScrollToItem(maxIndex)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 8.dp)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                .background(
                    color = AzulCabecalho,
                    shape = RoundedCornerShape(
                        topStart = 30.dp,
                        bottomStart = 5.dp,
                        topEnd = 5.dp,
                        bottomEnd = 30.dp
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, top = 8.dp)
            ) {
                Text(
                    text = "Chat IA",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    color = Color.White
                )
                Column (
                    modifier = Modifier
                        .weight(2f)
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = "Tipo de IA",
                        style = TextStyle.Default.copy(
                            fontSize = 14.sp,
                            color = Color.White
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable(
                                onClick = { expanded = !expanded }
                            ),
                        color = Color.White
                    )
                    Text(
                        text = selectedOption,
                        style = TextStyle.Default.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable(
                                onClick = { expanded = !expanded }
                            ),
                        color = Color.White
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.logocodek_allwhite),
                    contentDescription = "Logo da IA",
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.CenterVertically),
                )
            }
        }
        if (expanded) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .align(Alignment.CenterHorizontally)
                    .height(40.dp)
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            bottomStart = 20.dp,
                            topEnd = 0.dp,
                            bottomEnd = 15.dp
                        )
                    )
                    .padding(start = 15.dp, end = 15.dp)
            ) {
                SwitchButton(
                    checkedThumbColor = AzulCabecalho, // Cor do thumb quando ativado
                    uncheckedThumbColor = AzulCabecalho, // Cor do thumb quando desativado
                    checkedTrackColor = Color.Transparent, // Cor do fundo quando ativado
                    uncheckedTrackColor = Color.Transparent, // Cor do fundo quando desativado
                    checkedBorderColor = Color.Transparent, // Remover contorno quando ativado
                    uncheckedBorderColor = Color.Transparent, // Remover contorno quando desativado
                    switchState = switchState,
                    onSwitchChange = { state ->
                        chatViewModel.setSwitchState(state)
                        val option = if (state) "Pro" else "Flash"
                        chatViewModel.updateSelectedOption(option)
                    }
                )
            }
        }

        // Lista de mensagens
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 5.dp),
            verticalArrangement = Arrangement.Bottom, // Alinha os itens no fundo da tela
        ) {
            items(messages) { message ->
                val isLastMessage = message == messages.lastOrNull()
                ChatComponent(message = message, isLastMessage = isLastMessage, viewModel = chatViewModel)
            }
        }

        var messageText by rememberSaveable { mutableStateOf("") }

        Spacer(modifier = Modifier.height(8.dp))

        // Exibir imagem se estiver presente
        imageBitmap?.let { bitmap ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 5.dp)
                    .background(Color.LightGray, RoundedCornerShape(15.dp))
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Imagem Capturada",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp) // Ajuste conforme necessário
                        .widthIn(max = 300.dp)
                        .padding(8.dp)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 5.dp, end = 5.dp)
                        .size(20.dp)
                        .background(color = Color.Gray, shape = RoundedCornerShape(100))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "Remover Imagem",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(12.dp)
                            .clickable {
                                chatViewModel.setImageBitmap(null)
                            },
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            }
        }
        if (showOptions) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(start = 8.dp, end = 8.dp, bottom = 5.dp)
                    .background(Color.LightGray, RoundedCornerShape(15.dp)),
                contentAlignment = Alignment.Center
            ) {
                FloatingActionButtons(
                    onCaptureImage = onCaptureImage,
                    onFindImage = onFindImage,
                    onFechar = { showOptions = false }
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 5.dp, end = 5.dp)
                        .size(20.dp)
                        .background(color = Color.Gray, shape = RoundedCornerShape(100))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "Remover Imagem",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(12.dp)
                            .clickable {
                                showOptions = false
                            },
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            }
        }

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
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 30.dp)
                    .onFocusChanged {
                        showOptions = false
                        expanded = false
                    },
                shape = RoundedCornerShape(30.dp),
                placeholder = {
                    Text(
                        text = "Digite sua mensagem",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = AzulCabecalho,  // Cor da borda ao focar
                    unfocusedBorderColor = Color.Gray   // Cor da borda quando não está focado
                ),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Row(
                modifier = Modifier
                    .height(55.dp)
                    .align(Alignment.Bottom)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_anexo),
                    contentDescription = "Anexar Imagem",
                    modifier = Modifier
                        .size(35.dp)
                        .align(Alignment.CenterVertically)
                        .clickable {
                            showOptions = !showOptions
                        },
                    colorFilter = ColorFilter.tint(AzulCabecalho)
                )

                Spacer(modifier = Modifier.width(12.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_send_message),
                    contentDescription = "Enviar",
                    modifier = Modifier
                        .size(35.dp)
                        .align(Alignment.CenterVertically)
                        .clickable {
                            if (messageText.isNotBlank()) {
                                chatViewModel.sendMessage(
                                    bitmap = imageBitmap,
                                    content = messageText
                                )
                                messageText = ""
                                chatViewModel.setImageBitmap(null)
                                keyboardController?.hide()
                                chatViewModel.setTextComplete(false)
                                showOptions = false
                                expanded = false
                            }
                        },
                    colorFilter = ColorFilter.tint(AzulCabecalho)
                )
            }
        }
    }
}

@Composable
fun FloatingActionButtons(
    onCaptureImage: () -> Unit,
    onFindImage: () -> Unit,
    onFechar: () -> Unit
) {
    Row (
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.7f)
            .padding(5.dp)
            .zIndex(1f), // Coloca os botões sobre outros elementos
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_folder),
                contentDescription = "Anexar do Arquivo",
                modifier = Modifier
                    .size(35.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        onFindImage()
                        onFechar()
                    },
                colorFilter = ColorFilter.tint(Color.DarkGray)
            )
            Text(
                text = "ARQUIVO",
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_camera),
                contentDescription = "Abrir Camera",
                modifier = Modifier
                    .size(34.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        onCaptureImage()
                        onFechar()
                    },
                colorFilter = ColorFilter.tint(Color.DarkGray)
            )
            Text(
                text = "CÂMERA",
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}