package br.com.dieyteixeira.projetoia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import br.com.dieyteixeira.projetoia.models.Chat
import br.com.dieyteixeira.projetoia.ui.theme.AzulC_Balao

@Composable
fun ChatComponent(message: Chat) {
    val corDoFundo = if (message.isSentByUser) AzulC_Balao else Color.LightGray
    val corDoTexto = if (message.isSentByUser) Color.White else Color.Black
    val alinhamento = if (message.isSentByUser) Arrangement.End else Arrangement.Start

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = alinhamento
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .background(
                    color = corDoFundo,
                    shape = messageBubbleShape(message.isSentByUser)
                )
                .widthIn(max = screenWidth * 0.8f)
                .padding(8.dp)
        ) {
            if (message.isSentByUser) {
                Text(
                    text = message.content,
                    color = corDoTexto,
                    modifier = Modifier.padding(5.dp)
                )
            } else {
                TypewriterText(
                    text = message.content,
                    textColor = corDoTexto,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }
}

@Composable
fun messageBubbleShape(isSentByUser: Boolean): GenericShape {
    val density = LocalDensity.current
    val triangleSize = with(density) { 16.dp.toPx() }

    return GenericShape { size, _ ->
        if (isSentByUser) {
            // Biquinho para a direita superior
            moveTo(size.width + triangleSize, 0f)
            lineTo(size.width, triangleSize)
            lineTo(size.width, 0f)
            close()
            addRoundRect(
                RoundRect(
                    left = 0f,
                    top = 0f,
                    right = size.width,
                    bottom = size.height,
                    radiusX = triangleSize,
                    radiusY = triangleSize
                )
            )
            addRect(
                Rect(
                    left = size.width - triangleSize,
                    top = 0f,
                    right = size.width,
                    bottom = triangleSize
                )
            )
        } else {
            // Biquinho para a esquerda superior
            moveTo(-triangleSize, 0f)
            lineTo(0f, triangleSize)
            lineTo(triangleSize, 0f)
            close()
            addRoundRect(
                RoundRect(
                    left = 0f,
                    top = 0f,
                    right = size.width,
                    bottom = size.height,
                    radiusX = triangleSize,
                    radiusY = triangleSize
                )
            )
        }
    }
}