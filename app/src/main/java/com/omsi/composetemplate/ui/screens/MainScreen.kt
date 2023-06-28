package com.omsi.composetemplate.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omsi.composetemplate.ui.theme.ComposeTemplateTheme
import com.omsi.composetemplate.ui.utils.GaugeMeter

@Composable
fun MainScreen(canEnabled:Boolean){

    Column(
        modifier = Modifier.fillMaxHeight()){
        Text(text = if(canEnabled)
            "Enabled"
        else
            "Disabled")
        var sliderVal by remember{ mutableStateOf(0) }
        GaugeMeter(
            sliderVal,
            maxValue = 200,
            smallStepValue = 5,
            bigStepValue = 20,
            modifier = Modifier.size(300.dp))

        Slider(
            value = sliderVal.toFloat(),
            onValueChange = {
            sliderVal = it.toInt()
        },
        valueRange = 0f..100f,
        steps = 101)
    }

}


//density: 1DIP = 1px in 160dpi screen
// ldpi = 120dpi, mdpi=160dpi, hdpi=240dpi, xhdpi=320dpi
//px = dp * (dpi / 160) --> dp = px*160/dpi(120?)
@Preview(name= "MainScreen", apiLevel = 23, widthDp = (1024)*160/160, heightDp = (600*160/160-72-48-24), showBackground = true)
@Composable
fun MainScreenPreview(){
    ComposeTemplateTheme() {
        MainScreen(false)
    }
}