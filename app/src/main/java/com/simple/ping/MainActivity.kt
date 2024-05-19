package com.simple.ping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.simple.ping.viewmodel.PingViewModel

class MainActivity : ComponentActivity() {

    private val model: PingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreateButton(model)
        }
    }
}

@Preview
@Composable
fun CreateButton(pingViewModel: PingViewModel? = null) {
    val mosteratFontFamily = FontFamily(Font(R.font.mosterat))
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF072939),
                        Color(0xFFaeaeae)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
        ) {
            Image(
                painter = painterResource(R.drawable.fdr),
                contentDescription = null,
                modifier = Modifier
                    .height(200.dp)
                    .width(200.dp)
                    .padding(32.dp)
                    .align(CenterHorizontally)
            )
            var text by remember { mutableStateOf(pingViewModel?.interval?.value) }
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    unfocusedLeadingIconColor = Color.White,
                    focusedBorderColor = Color.White,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White
                ),
                value = text.toString(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = true),
                onValueChange = {
                    if (it.length <= 6) {
                        if (it.isDigitsOnly()) text = it
                        pingViewModel?.interval?.value = it
                    }
                },
                maxLines = 1,
                singleLine = true,
                label = {
                    Text(
                        "Interval in ms default = 500 (max 6 symbols)",
                        style = TextStyle(color = Color.White)
                    )
                }
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(20.dp)
                .align(Center),
            text = pingViewModel?.text?.value.toString(),
            color = Color.White,
            fontFamily = mosteratFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.Bottom
        )
        {
            createCardStartPing("Start Ping", pingViewModel)
            createCardStopPing("Stop Ping", pingViewModel)
        }
    }
}

@Composable
private fun createCardStartPing(text: String, pingViewModel: PingViewModel? = null) {
    val mosteratFontFamily = FontFamily(Font(R.font.mosterat))
    val gradient = Brush.horizontalGradient(
        listOf(
            Color(0xFF072939),
            Color(0xFF0f303f)
        )
    )
    ElevatedCard(elevation = CardDefaults.cardElevation(
        defaultElevation = 10.dp
    ), modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .height(50.dp)
        .clip(RoundedCornerShape(30.dp))
        .background(gradient)
        .clickable(enabled = pingViewModel?.enabledStart!!.value) {
            pingViewModel.startPing()
            pingViewModel.text.value = "Start ping - 8.8.8.8"
            pingViewModel.enabledStart.value = false
            pingViewModel.enabledStop.value = true
        }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(gradient)
                .clip(RoundedCornerShape(30.dp))
        ) {
            Text(
                color = Color.White,
                text = text,
                fontFamily = mosteratFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                modifier = Modifier.align(Center)
            )
        }
    }
}

@Composable
private fun createCardStopPing(text: String, pingViewModel: PingViewModel? = null) {
    val mosteratFontFamily = FontFamily(Font(R.font.mosterat))
    val gradient = Brush.horizontalGradient(
        listOf(
            Color(0xFF072939),
            Color(0xFF0f303f)
        )
    )
    ElevatedCard(elevation = CardDefaults.cardElevation(
        defaultElevation = 10.dp
    ), modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .height(50.dp)
        .clip(RoundedCornerShape(30.dp))
        .background(gradient)
        .clickable(enabled = pingViewModel?.enabledStop!!.value) {
            pingViewModel.job?.cancel()
            pingViewModel.text.value = "Please press button start ping - 8.8.8.8"
            pingViewModel.enabledStop.value = false
            pingViewModel.enabledStart.value = true
        }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(gradient)
                .clip(RoundedCornerShape(30.dp))
        ) {
            Text(
                color = Color.White,
                text = text,
                fontFamily = mosteratFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                modifier = Modifier.align(Center)
            )
        }
    }
}


