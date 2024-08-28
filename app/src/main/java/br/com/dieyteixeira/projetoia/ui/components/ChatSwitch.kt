package br.com.dieyteixeira.projetoia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import br.com.dieyteixeira.projetoia.ui.theme.AzulCabecalho

@Composable
fun SwitchButton(
    switchState: Boolean,
    checkedThumbColor: Color,
    uncheckedThumbColor: Color,
    checkedTrackColor: Color,
    uncheckedTrackColor: Color,
    checkedBorderColor: Color,
    uncheckedBorderColor: Color,
    onSwitchChange: (Boolean) -> Unit
) {

    Spacer(modifier = Modifier.height(1.dp))
    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .width(80.dp)
                    .background(
                        color = if (switchState) Color.Gray else AzulCabecalho,
                        shape = RoundedCornerShape(
                            topStart = 10.dp,
                            bottomStart = 10.dp,
                            topEnd = 10.dp,
                            bottomEnd = 10.dp
                        )
                    ),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "Velocidade",
                    style = TextStyle.Default.copy(
                        color = Color.White,
                        fontSize = 14.sp
                    ),
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Box {
                Box (
                    modifier = Modifier
                        .height(4.dp)
                        .width(40.dp)
                        .background(
                            color = Color.Gray,
                            shape = RoundedCornerShape(100)
                        )
                        .align(Alignment.Center)
                ) {

                }
                Switch(
                    checked = switchState,
                    onCheckedChange = { state ->
                        onSwitchChange(state)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = checkedThumbColor,
                        uncheckedThumbColor = uncheckedThumbColor,
                        checkedTrackColor = checkedTrackColor,
                        uncheckedTrackColor = uncheckedTrackColor,
                        checkedBorderColor = checkedBorderColor,
                        uncheckedBorderColor = uncheckedBorderColor
                    ),
                    thumbContent = {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    color = checkedThumbColor
                                )
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .width(80.dp)
                    .background(
                        color = if (switchState) AzulCabecalho else Color.Gray,
                        shape = RoundedCornerShape(
                            topStart = 10.dp,
                            bottomStart = 10.dp,
                            topEnd = 10.dp,
                            bottomEnd = 10.dp
                        )
                    ),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "Precisão",
                    style = TextStyle.Default.copy(
                        color = Color.White,
                        fontSize = 14.sp
                    ),
                )
            }
        }
    }
}