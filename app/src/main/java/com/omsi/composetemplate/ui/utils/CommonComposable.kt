package com.omsi.marmix.ui.remote.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@Composable
fun ActivityEventListener(OnEvent : (event: Lifecycle.Event)->Unit){
    val eventHandler = rememberUpdatedState(newValue = OnEvent)
    val lifecycleOwner = rememberUpdatedState(newValue = LocalLifecycleOwner.current)

    DisposableEffect(key1 = lifecycleOwner.value){
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver{source, event ->
            eventHandler.value(event)
        }
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun HeaderBox(
    modifier: Modifier=Modifier,
    color:Color = Color.LightGray,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    composable: @Composable() ColumnScope.()->Unit){

    Column(modifier = modifier
        .clip(RoundedCornerShape(10.dp))
        .background(color)
        .width(120.dp),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = composable
    )
}
private const val TEXT_SCALE_REDUCTION_INTERVAL = 0.9f

@Composable
fun HeaderDataIndicator(text:String="", prefTextSize : TextUnit = 15.sp, color: Color =Color.LightGray){

    HeaderBox(
        modifier = Modifier
            .width(70.dp)
            .padding(horizontal = 2.dp, vertical = 2.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceAround,
        color = color
    ){

        var textSize by remember { mutableStateOf(prefTextSize) }

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontSize = textSize,
            fontFamily = FontFamily.Default,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Normal,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                val maxCurrentLineIndex: Int = textLayoutResult.lineCount - 1

                if (textLayoutResult.isLineEllipsized(maxCurrentLineIndex)) {
                    textSize = textSize.times(TEXT_SCALE_REDUCTION_INTERVAL)
                }
            }
        )
    }
}
