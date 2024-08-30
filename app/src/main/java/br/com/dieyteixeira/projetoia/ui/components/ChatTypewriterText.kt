package br.com.dieyteixeira.projetoia.ui.components

import android.util.Log
import br.com.dieyteixeira.projetoia.ui.viewmodels.ChatViewModel
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun TypewriterText(
    text: String,
    viewModel: ChatViewModel,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Black,
    textSize: TextUnit = 16.sp
) {
    var displayedText by remember { mutableStateOf("") }
    val textToDisplay = text
    val textLength = textToDisplay.length

    LaunchedEffect(text) {
        viewModel.updateTextLength(displayedText.length) // Atualize o comprimento do texto no ViewModel
        displayedText = ""
        for (i in 0 until textLength) {
            delay(30)
            displayedText += textToDisplay[i]
            Log.e("TypewriterText", "displayedText: $displayedText")
        }
        viewModel.setTextComplete(true) // Notifique que o texto está completo
    }

    Text(
        text = displayedText,
        modifier = modifier,
        color = textColor,
        style = TextStyle(fontSize = textSize)
    )
}