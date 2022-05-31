package com.example.investwallet.ui.detail

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.investwallet.database.FavoriteTicket
import com.example.investwallet.ui.theme.InvestWalletTheme
import com.google.accompanist.flowlayout.FlowRow

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailNews(
    detailNewsViewModel: DetailNewsViewModel = hiltViewModel(),
    isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
    onBack: () -> Unit,
    onClick:(tag: String) -> Unit
) {
    val detailNews = detailNewsViewModel.stateDetailNews.collectAsState()
    LaunchedEffect(key1 = 0, block = {
        detailNewsViewModel.getData(isSystemInDarkTheme)
    })


    Scaffold(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize(),
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
            TopBarDetailNews(
                detailNews.value.label,
                onBack = onBack
            )
        }
    ) {
        if (detailNews.value.title.isNotEmpty()) {

            val sizeList = if (detailNews.value.listChip.size > 2){
                2
            }else{
                detailNews.value.listChip.size
            }

            val sizePreview = remember {
                mutableStateOf(
                    sizeList
                )
            }
            val selectedList= listOf("Раскрыть список", "Скрыть список")
            val (selectedOption, onOptionSelected ) = remember { mutableStateOf(selectedList[0]) }

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {

                Text(
                    text = detailNews.value.title,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp),
                )

                FlowRow(
                    mainAxisSpacing = 10.dp,
                    modifier = Modifier
                        .padding(10.dp)
                        .animateContentSize()
                ) {
                    for (i in 0 until sizePreview.value) {
                        val it = detailNews.value.listChip[i]
                        Chip(
                            onClick = {
                                Log.e("it.getURLImg()", it.getTag())
                                onClick(it.getTag())
                            },
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
                                    placeholder = painterResource(id = com.example.investwallet.R.drawable.ic_launcher_foreground)
                                )
                            },
                            content = {
                                Text(text = it.symbol)
                            },
                        )
                    }

                }

                AnimatedVisibility(visible = detailNews.value.listChip.size > 2) {
                    ClickableText(
                        text = AnnotatedString(
                            text = selectedOption,
                            spanStyles = listOf(
                                AnnotatedString.Range(
                                    SpanStyle(
                                        color = MaterialTheme.colors.primary,
                                        fontSize = 18.sp,
                                        fontFamily = FontFamily.Monospace
                                    ),
                                    0,
                                    selectedOption.length
                                ),
                            )
                        ) ,
                        onClick = {
                            if (sizePreview.value == detailNews.value.listChip.size
                                && detailNews.value.listChip.size > 2
                            ){
                                sizePreview.value = 2
                                onOptionSelected(selectedList.first())
                            }else{
                                sizePreview.value = detailNews.value.listChip.size
                                onOptionSelected(selectedList.last())
                            }
                        },
                        modifier = Modifier.padding(10.dp)
                    )
                }




                detailNews.value.annotationText.forEach { annotated ->
                    ClickableText(
                        text = annotated,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        onClick = { offset ->
                            Log.d("Clicked URL", "CLICK")
                            annotated.getStringAnnotations(
                                tag = "URL", start = offset,
                                end = offset
                            )
                                .firstOrNull()?.let { annotation ->
                                    // If yes, we log its value
                                    Log.d("Clicked URL", annotation.item)
                                }
                        }
                    )
                }


            }
        }
    }


}

@Composable
fun TopBarDetailNews(
    label: String,
    onBack: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = label,  fontWeight = FontWeight.Bold) },
        navigationIcon = { IconButton(onClick = { onBack() }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
        } },
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
    )
}

@Preview
@Composable
fun _DetailNews() {
    InvestWalletTheme {
        DetailNews(onClick = {}, onBack = {})
    }
}