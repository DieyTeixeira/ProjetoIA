package br.com.dieyteixeira.projetoia.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.dieyteixeira.projetoia.models.Chat
import br.com.dieyteixeira.projetoia.ui.theme.AzulC_Balao
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatComponent(message: Chat) {
    val corDoFundo = if (message.isSentByUser) AzulC_Balao else Color.LightGray
    val corDoTexto = if (message.isSentByUser) Color.White else Color.Black
    val alinhamento = if (message.isSentByUser) Arrangement.End else Arrangement.Start

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Formatando a data/hora
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val formattedTime = dateFormat.format(Date(message.timestamp)) // Supondo que `timestamp` seja um long

    // Obtendo a imagem do usuário (se disponível)
    val userProfileImage: Bitmap? = message.bitmap

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        horizontalArrangement = alinhamento
    ) {
        if (!message.isSentByUser && userProfileImage != null) {
            Image(
                bitmap = userProfileImage.asImageBitmap(),
                contentDescription = "User Profile Image",
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 8.dp),
                contentScale = ContentScale.Crop
            )
        }
        Column {
            Text(
                text = formattedTime,
                style = TextStyle(
                    color = Color.LightGray,
                    fontSize = 15.sp
                ),
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp, bottom = 2.dp, top = 2.dp)
                    .align(alignment = if (message.isSentByUser) Alignment.End else Alignment.Start)
            )
            Box(
                modifier = Modifier
                    .background(
                        color = corDoFundo,
                        shape = messageBubbleShape(message.isSentByUser)
                    )
                    .widthIn(max = screenWidth * 0.8f)
                    .padding(start = 8.dp, end = 8.dp, top = 5.dp, bottom = 5.dp)
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