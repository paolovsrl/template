package com.omsi.composetemplate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omsi.composetemplate.ui.theme.ComposeTemplateTheme
import com.omsi.composetemplate.ui.theme.Typography

@Composable
fun WaitingPage(){
    Column(Modifier.fillMaxSize(), Arrangement.SpaceEvenly, Alignment.CenterHorizontally) {

        Text(text = "Waiting...", style= Typography.headlineMedium)
        CircularProgressIndicator(modifier= Modifier.size(100.dp))

    }
}

@Preview(showBackground = true, device = Devices.AUTOMOTIVE_1024p)
@Composable
fun WaitingScreenPreview(){
    ComposeTemplateTheme() {
        WaitingPage()
    }
}