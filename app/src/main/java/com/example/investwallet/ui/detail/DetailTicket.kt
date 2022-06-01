package com.example.investwallet.ui.detail

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.investwallet.R
import com.example.investwallet.dto.converter.newsDtoItem
import com.example.investwallet.ui.detail.dynamicTheme.DynamicThemePrimaryColorsFromImage
import com.example.investwallet.ui.detail.dynamicTheme.rememberDominantColorState
import com.example.investwallet.ui.detail.placeholder.detailticket._PlaceHolderHeadlines
import com.example.investwallet.ui.detail.placeholder.detailticket._PlaceholderInfo
import com.example.investwallet.ui.theme.InvestWalletTheme
import com.example.investwallet.ui.theme.contrastAgainst
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel = hiltViewModel(),
    tagTicket: String,
    category: String,
    country: String,
    onBack: () -> Unit,
    onClick: (headline: newsDtoItem) -> Unit
) {
    LaunchedEffect(key1 = 0, block = {
        detailViewModel.loadListDetailNews(tagTicket, category, country)
    })


    val state = detailViewModel.stateDetail.collectAsState()

    val surfaceColor = MaterialTheme.colors.surface
    val dominantColorState = rememberDominantColorState(
        defaultColor = surfaceColor
    ) { color ->
        color.contrastAgainst(surfaceColor) >= 0.5f
    }

    val systemUiController = rememberSystemUiController()




    DynamicThemePrimaryColorsFromImage(dominantColorState){
        LaunchedEffect(key1 = state.value.stateLoad, block = {
            if (state.value.symbol?.getURLImg()?.isNotEmpty() == true){
                dominantColorState.updateColorsFromImageUrl(state.value.symbol?.getURLImg() ?: "")
            }else{
                dominantColorState.reset()
            }
        })

        val color = animateColorAsState(
            targetValue = MaterialTheme.colors.onError,
            spring(stiffness = Spring.StiffnessLow)
        )


        LaunchedEffect(key1 = color.value) {
            systemUiController.setStatusBarColor(
                color = color.value,
                darkIcons = color.value.luminance() > 0.3f
            )
        }


        Scaffold(
            topBar = {
                TopBarDetailScreen(
                    state.value.symbol?.getDescriptions() ?: "Загрузка",
                    onBack,
                    onFavoriteTicket = {
                        detailViewModel.onFavorite(it)
                    },
                    isFavorite = state.value.isFavorite
                )
            },
            modifier = Modifier,
        ) {
                paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .background(color = surfaceColor),
            ) {

                when(val _state = state.value.stateLoad){
                    is StateLoad.Error -> {

                    }
                    StateLoad.Loading -> {
                        _PlaceholderInfo()
                        _PlaceHolderHeadlines()
                    }
                    StateLoad.Success -> {
                        Info(
                            state.value.symbol?.getURLImg() ?: "",
                            state.value.price
                        )

                        Headlines(state.value.headlineList, onClick = {
                            detailViewModel.check(it)
                            onClick(it)
                        })
                    }
                }

            }
        }

    }

}

@Composable
fun TopBarDetailScreen(
    label: String,
    onBack: () -> Unit,
    onFavoriteTicket: (checked: Boolean) -> Unit,
    isFavorite: MutableState<Boolean>,
) {

    Log.e("_databaseFavoriteTicket", isFavorite.value.toString())
    TopAppBar(
        title = { Text(
            text = label,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        ) },
        navigationIcon = { IconButton(onClick = { onBack() }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
        } },
        backgroundColor = MaterialTheme.colors.primary,
        actions = {
            IconToggleButton(
                checked = isFavorite.value,
                onCheckedChange = {
                    isFavorite.value = it
                    onFavoriteTicket(isFavorite.value)
                }
            ) {
                val tint by animateColorAsState(if (isFavorite.value) MaterialTheme.colors.onPrimary else Color(0xFFB0BEC5))
                Icon(Icons.Filled.Favorite, contentDescription = "Localized description", tint = tint)
            }
        },
        modifier = Modifier.statusBarsPadding()
    )
}




@Composable
fun Info(
    urlImg: String,
    price: String
) {
    Log.e("it.getURLImg()", "Info -- $urlImg")
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
            fontSize = 60.sp,
        )
    }
}

@Composable
fun Headlines (
    listHeadline: List<newsDtoItem>,
    onClick: (headline: newsDtoItem) -> Unit
) {
    Text(
        text = "Главные новости",
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        fontFamily = FontFamily.Monospace,
        modifier = Modifier.padding(20.dp),
    )



    listHeadline.forEach {
        ItemNews(it, onClick)
        Spacer(modifier = Modifier.size(25.dp))
        Divider(modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.size(25.dp))
    }

}


/**Оптимизация нужна */
@Composable
fun ItemNews(
    headline: newsDtoItem,
    onClick: (headline: newsDtoItem) -> Unit
) {


    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .clickable {
                onClick(headline)
            }
    ) {

        if (!headline.relatedSymbols.isNullOrEmpty()){
            LittleIconPrevNews(Modifier){
                headline.relatedSymbols = headline.relatedSymbols.filter {
                    it.logoid != null
                }

                val sizeIcons = if (headline.relatedSymbols.size >= 13){
                    13
                }else{
                    headline.relatedSymbols.size
                }

                for (i in 0 until sizeIcons){
                    val symbol = headline.relatedSymbols[i]
                    Log.e("it.getURLImg()", "ItemNews -- ${symbol.getURLImg()}")
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