package com.example.proyectorinconsur.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.delay

@Composable
fun NumeroAnimado(
    valor: Int,
    modifier: Modifier = Modifier,
    estilo: TextStyle = MaterialTheme.typography.headlineMedium,
    color: Color = Color.Unspecified
) {
    AnimatedContent(
        targetState = valor,
        transitionSpec = {
            if (targetState > initialState) {
                (slideInVertically { it } + fadeIn()) togetherWith (slideOutVertically { -it } + fadeOut())
            } else {
                (slideInVertically { -it } + fadeIn()) togetherWith (slideOutVertically { it } + fadeOut())
            }
        },
        label = "contador",
        modifier = modifier
    ) { numero ->
        Text(text = numero.toString(), style = estilo, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun rememberAhora(): State<Long> = produceState(initialValue = System.currentTimeMillis()) {
    while (true) {
        delay(1000)
        value = System.currentTimeMillis()
    }
}
