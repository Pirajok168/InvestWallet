package com.example.investwallet.ui.detail.placeholder.detailticket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.vponomarenko.compose.shimmer.shimmer

@Composable
fun _PlaceholderInfo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = CircleShape,
            modifier = Modifier
                .size(400.dp)
                .background(color = Color(0xFFF3F3F3), CircleShape)
                .shimmer()
        ) {}



        Spacer(modifier = Modifier.size(10.dp))
        Surface(
            modifier = Modifier
                .size(width = 60.dp, height = 30.dp)
                .background(color = Color(0xFFF3F3F3))
                .shimmer(),
            shape = RoundedCornerShape(20.dp)
        ){}
    }
}

@Composable
fun _PlaceHolderHeadlines() {
    Text(
        text = "Главные новости",
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        fontFamily = FontFamily.Monospace,
        modifier = Modifier.padding(20.dp)
    )



    (0..5).forEach {
        _PlaceholderItemNews()
        Spacer(modifier = Modifier.size(25.dp))
        Divider(modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.size(25.dp))
    }
}

@Composable
fun _PlaceholderItemNews(){
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)

    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(color = Color(0xFFF3F3F3))
                .shimmer(),
        ) {

        }
    }
}