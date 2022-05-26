package com.example.investwallet.ui.detail

import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.investwallet.dto.converter.Content
import com.example.investwallet.ui.theme.InvestWalletTheme

@Composable
fun DetailNews(
    detailNewsViewModel: DetailNewsViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val detailNews = detailNewsViewModel.detailNews.collectAsState()



    Surface(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
    ) {
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())) {
            Text(
                text = detailNews.value?.title ?: " ",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 30.sp,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp)
            )
            detailNews.value?.astDescription?.children?.forEach {
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


            }
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