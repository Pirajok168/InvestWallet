package com.example.investwallet.ui.detail

import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.investwallet.dto.converter.Content
import com.example.investwallet.ui.theme.InvestWalletTheme
import com.google.accompanist.flowlayout.FlowRow

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailNews(
    detailNewsViewModel: DetailNewsViewModel = hiltViewModel(),
    isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
    onBack: () -> Unit
) {
    val detailNews = detailNewsViewModel.stateDetailNews.collectAsState()
    LaunchedEffect(key1 = 0, block = {
        detailNewsViewModel.getData(isSystemInDarkTheme)
    })


    Surface(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())) {

            Text(
                text = detailNews.value.title,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 30.sp,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp),
            )

            FlowRow(
                mainAxisSpacing = 10.dp,
                modifier = Modifier.padding(10.dp)
            ) {
                detailNews.value.listChip.forEach {
                    Log.e("chip", it.symbol)
                    Chip(
                        onClick = {  },
                        leadingIcon = {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(it.getURLImg())
                                    .decoderFactory(SvgDecoder.Factory())
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "",
                                modifier = Modifier
                                    .padding(5.dp)
                                    .size(30.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                                placeholder= painterResource(id = com.example.investwallet.R.drawable.ic_launcher_foreground)
                            )
                        },
                        content = {
                            Text(text = it.symbol)
                        },
                    )
                }
            }

            detailNews.value.annotationText.forEach {
                    annotated ->
                ClickableText(
                    text = annotated,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    onClick = {
                            offset->
                        Log.d("Clicked URL", "CLICK")
                        annotated.getStringAnnotations(tag = "URL", start = offset,
                            end = offset)
                            .firstOrNull()?.let { annotation ->
                                // If yes, we log its value
                                Log.d("Clicked URL", annotation.item)
                            }
                    }
                )
            }


            /*detailNews.value?.astDescription?.children?.forEach {
                if (it is Content.ChildrenContent){
                    val test = it
                    val list = test.content.children
                    val annotationText = buildAnnotatedString {
                        list.forEach {
                                con->
                            if (con is Content.StringContent){
                                append(con.content)
                            }else if (con is Content.ChildrenContent){
                                pushStringAnnotation(tag = "URL",
                                    annotation = "https://developer.android.com")

                                withStyle(style = SpanStyle(color = Color.Blue,
                                    fontWeight = FontWeight.Bold)
                                ) {
                                    append(con.content.params?.text?: "")
                                }
                                pop()
                            }
                        }

                    }

                    ClickableText(
                        text = annotationText,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        onClick = {
                                offset->
                            Log.d("Clicked URL", "CLICK")
                            annotationText.getStringAnnotations(tag = "URL", start = offset,
                                end = offset)
                                .firstOrNull()?.let { annotation ->
                                    // If yes, we log its value
                                    Log.d("Clicked URL", annotation.item)
                                }
                        }
                    )
                }else{

                }


            }*/
        }
    }


}

@Composable
fun TopBarDetailNews(
    label: String,
    onBack: () -> Unit
) {
    TopAppBar(
        title = { Text(text = label,  fontWeight = FontWeight.Bold) },
        navigationIcon = { IconButton(onClick = { onBack() }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
        } },
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
    )
}

@Preview
@Composable
fun _DetailNews() {
    InvestWalletTheme {
        DetailNews(){

        }
    }
}