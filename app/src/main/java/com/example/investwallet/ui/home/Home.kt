package com.example.investwallet.ui.home


import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.investwallet.ui.theme.InvestWalletTheme
import com.example.investwallet.R
import com.example.investwallet.database.FavoriteTicket
import me.vponomarenko.compose.shimmer.shimmer
import java.text.SimpleDateFormat
import java.util.*





@Composable
fun Home(
    homeViewModel: HomeViewModel = hiltViewModel(),
    onSearch: () -> Unit,
    onDetail: (ticket: FavoriteTicket) -> Unit,
    onSeeAll: () -> Unit
) {


    LaunchedEffect(key1 = 0, block = {
        homeViewModel.create()
    })



    val model: HomeViewModel = viewModel()


    val listFavoriteTicket = homeViewModel.test.collectAsState()




    Scaffold(
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
            HomeTopBar(
                "?????? ??????????????",
                onSearch = onSearch
            )
        },
        modifier = Modifier.systemBarsPadding()
    ) {
            contentPadding->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {


            FavoriteList(
                "?????????????????? ??????????",
                listFavoriteTicket.value.listFavoriteStock,
                listFavoriteTicket.value.stateLoad,
                onSeeAll = onSeeAll,
                onOpenTicket = onDetail
            )

            FavoriteList(
                "????????????????????????",
                listFavoriteTicket.value.listFavoriteCrypto,
                listFavoriteTicket.value.stateLoad,
                onSeeAll = onSeeAll,
                onOpenTicket = onDetail
            )

            FavoriteList(
                "Etf",
                listFavoriteTicket.value.listFavoriteEtf,
                listFavoriteTicket.value.stateLoad,
                onSeeAll = onSeeAll,
                onOpenTicket = onDetail
            )
        }
    }
}

@Composable
fun HomeTopBar(
    title: String,
    onSearch: () -> Unit
) {
    TopAppBar(
        title = { Text(text = title, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null
            )
        },
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
        actions = {
            IconButton(onClick = { onSearch() }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            }
        },
        modifier = Modifier
    )
}


@Composable
fun AccountMany(
    formatSum: String,
    formatDate: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(20.dp),
        backgroundColor = MaterialTheme.colors.primary,
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            contentAlignment = Alignment.Center){

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = formatSum,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSecondary,
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )

                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = formatDate,
                        color =  MaterialTheme.colors.onSecondary,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                    )
                }

            }
        }
    }
}



@Composable
fun FavoriteList(
    label: String,
    favoriteList: List<FavoriteTicket>,
    state: com.example.investwallet.ui.home.StateLoad,
    onSeeAll: () -> Unit,
    onOpenTicket: (ticket: FavoriteTicket) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        ClickableText(
            text = AnnotatedString(
                "???????????????? ??????",
                spanStyles = listOf(
                    AnnotatedString.Range(SpanStyle(color = Color.Blue),0,"???????????????? ??????".length)
                )
            ),
            onClick = { onSeeAll() }
        )
    }

    LazyRow(
        contentPadding = PaddingValues(20.dp),
        horizontalArrangement=Arrangement.spacedBy(20.dp)
    ){
        if(favoriteList.isEmpty()){
            items(5){
                Card(
                    modifier = Modifier
                        .width(300.dp)
                        .height(200.dp)
                        .shimmer(),
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = Color(0xFFF3F3F3)
                ) {}
            }
        }else{
            items(favoriteList){
                Log.e("price", it.price)
                CardTicket(
                    it,
                    state,
                    onOpenTicket=onOpenTicket
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardTicket(
    ticket: FavoriteTicket,
    state: com.example.investwallet.ui.home.StateLoad,
    onOpenTicket: (ticket: FavoriteTicket) -> Unit
) {
    Card(
        onClick = { onOpenTicket(ticket) },
        modifier = Modifier
            .width(300.dp)
            .height(200.dp),
        shape = RoundedCornerShape(20.dp)
    ) {

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(20.dp), contentAlignment = Alignment.TopStart){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.border(1.dp, Color.Black, CircleShape)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(ticket.getURLImg())
                            .decoderFactory(SvgDecoder.Factory())
                            .crossfade(true)
                            .build(),
                        contentDescription = "",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        placeholder= painterResource(id = R.drawable.ic_launcher_foreground)
                    )
                }

                Text(
                    text = ticket.getSymbols(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 20.dp)
                )

            }
        }



        Box(modifier = Modifier
            .fillMaxSize()
            .padding(20.dp), contentAlignment = Alignment.BottomStart){
            Column {
                when(state){
                    StateLoad.LOADING ->{
                        Surface(
                            color = Color(0xFFF3F3F3),
                            modifier = Modifier
                                .size(width = 90.dp, height = 30.dp)
                                .shimmer()
                        ){}
                    }
                    StateLoad.SUCCESS ->{
                        Text(
                            text = ticket.price,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 25.sp
                        )
                    }
                }
            }
        }

    }
}



@Composable
fun Information(
    list: List<FavoriteTicket>,
) {
    Text(
        text = "?????????????????? ????????????????",
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        modifier = Modifier.padding(20.dp)
    )
    val selectedList= listOf("??????", "24 ??.", "???????????? ????????????????", "???????????? ????????????????")
    val (selectedOption, onOptionSelected ) = remember { mutableStateOf(selectedList[0]) }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .selectableGroup()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        selectedList.forEach{
                text ->
            val backgroundColor = if (text == selectedOption)
                MaterialTheme.colors.primary else MaterialTheme.colors.surface
            Row(
                Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) },
                        role = Role.Button
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    backgroundColor = backgroundColor,
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Text(
                        text = text,
                        color = contentColorFor(backgroundColor = backgroundColor),
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.size(10.dp))
        }
    }

    list.forEach{
        LittleCardTicket(ticket = it)
        Spacer(modifier = Modifier.size(10.dp))
    }
}

@Composable
fun LittleCardTicket(
    ticket: FavoriteTicket
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                modifier = Modifier.weight(4f)
            ) {
                Row() {
                    Surface(
                        modifier = Modifier.border(1.dp, Color.Black, CircleShape)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(ticket.getURLImg())
                                .decoderFactory(SvgDecoder.Factory())
                                .crossfade(true)
                                .build(),
                            contentDescription = "",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            placeholder= painterResource(id = R.drawable.ic_launcher_foreground)
                        )
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Column() {
                        Text(
                            text = ticket.getDescriptions(),
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(end = 5.dp)
                        )
                        Spacer(modifier = Modifier.size(3.dp))
                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                            Text(
                                text = ticket.getSymbols(),
                                color = Color.LightGray,
                            )
                        }
                    }
                }
            }

            Surface(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "+0.67%", color = Color.Green)
            }

        }
        
    }
}


