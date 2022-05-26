package com.example.investwallet.ui.detail

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.investwallet.R
import com.example.investwallet.dto.converter.newsDtoItem
import com.example.investwallet.dto.headlines.Headline
import com.example.investwallet.dto.headlines.RelatedSymbol
import com.example.investwallet.ui.theme.InvestWalletTheme

@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel = hiltViewModel(),
    onBack: () -> Unit
) {


    val headlineList = detailViewModel.headlineList.collectAsState()
    val symbol = detailViewModel.symbol.collectAsState()

    Scaffold(
        topBar = { TopBarDetailScreen(symbol.value?.getDescription?:"", onBack) },
        modifier = Modifier.systemBarsPadding()
    ) {
            paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            Info(
                symbol.value?.getURLImg() ?: "",
                "140 $"
            )

            Headlines(headlineList.value, onClick = {})
        }
    }
}

@Composable
fun TopBarDetailScreen(
    label: String,
    onBack: () -> Unit
) {
    TopAppBar(
        title = { Text(text = label,  fontWeight = FontWeight.Bold) },
        navigationIcon = { IconButton(onClick = { onBack() }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
        } },
        backgroundColor = MaterialTheme.colors.background
    )
}

@Composable
fun Info(
    urlImg: String,
    price: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(urlImg)
                .decoderFactory(SvgDecoder.Factory())
                .crossfade(true)
                .build(),
            contentDescription = "",
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            placeholder= painterResource(id = R.drawable.ic_launcher_foreground)
        )
        
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = price,
            fontWeight = FontWeight.Bold,
            fontSize = 60.sp
        )
    }
}

@Composable
fun Headlines(
    listHeadline: List<newsDtoItem>,
    onClick:(headline: Headline)-> Unit
) {
    Text(
        text = "Главные новости",
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        fontFamily = FontFamily.Monospace,
        modifier = Modifier.padding(20.dp)
    )



    listHeadline.forEach {
        ItemNews(it)
        Spacer(modifier = Modifier.size(25.dp))
        Divider(modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.size(25.dp))
    }

}


/**Оптимизация нужна */
@Composable
fun ItemNews(
    headline: newsDtoItem
) {


    Column(modifier = Modifier.padding(horizontal = 20.dp)) {

        if (!headline.relatedSymbols.isNullOrEmpty()){
            LittleIconPrevNews(Modifier){
                /*headline.relatedSymbols = headline.relatedSymbols.filter {
                    it.currencyLogoid != null
                }*/

                val sizeIcons = if (headline.relatedSymbols.size >= 13){
                    13
                }else{
                    headline.relatedSymbols.size
                }

                for (i in 0 until sizeIcons){
                    val symbol = headline.relatedSymbols[i]

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(symbol.getURLImg())
                            .decoderFactory(SvgDecoder.Factory())
                            .crossfade(true)
                            .build(),
                        contentDescription = "",
                        modifier = Modifier
                            .size(25.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        placeholder= painterResource(id = R.drawable.ic_launcher_foreground),
                    )

                }

            }
        }
        
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = headline.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun LittleIconPrevNews(
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    Layout(modifier = modifier, content = content){
            measurables, constraints ->

        var height: Int = 0

        val placeables = measurables.map { measurable ->
            val placeable = measurable.measure(constraints)
            height= placeable.height

            placeable
        }
        layout(constraints.maxWidth, height) {
            var xPosition = 0


            placeables.forEach { placeable ->
                placeable.placeRelative(x = xPosition, y = 0)

                xPosition += placeable.width - 20
            }
        }
    }
}

@Preview
@Composable
fun DetailPreview() {
    InvestWalletTheme {

    }
}


@Preview
@Composable
fun ItemPreview() {
    val list = List(3){
        R.drawable.ic_launcher_foreground
    }
    InvestWalletTheme {
        LittleIconPrevNews(Modifier){
            list.forEach {

                Surface(
                    shape = CircleShape,
                    modifier = Modifier.size(60.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.imgimg),
                        contentDescription = ""
                    )
                }

                /*AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("")
                        .decoderFactory(SvgDecoder.Factory())
                        .crossfade(true)
                        .build(),
                    contentDescription = "",
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder= painterResource(id = R.drawable.ic_launcher_foreground)
                )*/
            }
        }
    }
}