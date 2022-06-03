package com.example.investwallet.ui.all

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.investwallet.R
import com.example.investwallet.database.FavoriteTicket

@Composable
fun SeeAll(
    seeAllViewModel: SeeAllViewModel = hiltViewModel(),
    onBack:() -> Unit,
    onDetail: (ticket: FavoriteTicket) -> Unit,
) {
    val listFavoriteTicket = seeAllViewModel.state.collectAsState()
    Scaffold(
        topBar = {
            TopBarSeeAll(onBack=onBack)
        },
        modifier = Modifier.systemBarsPadding()
    ) {
            paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            items(listFavoriteTicket.value.listFavoriteTicket){
                FullCard(it, onDetail)
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FullCard(
    favoriteTicket: FavoriteTicket,
    onOpen: (favoriteTicket: FavoriteTicket) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(20.dp),
        onClick = {
            onOpen(favoriteTicket)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(modifier = Modifier.weight(6f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.border(1.dp, Color.Black, CircleShape)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(favoriteTicket.getURLImg())
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
                    Text(
                        text = favoriteTicket.getDescriptions(),
                        fontWeight = FontWeight.Bold,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Spacer(modifier = Modifier.size(5.dp))
            Surface(modifier = Modifier.weight(1f)) {
                Text(
                    text = favoriteTicket.exchange,
                    fontWeight = FontWeight.ExtraLight,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }

        }

    }
}


@Composable
fun TopBarSeeAll(
    onBack:() -> Unit
) {
    TopAppBar(
        title = { Text(text = "Изрбранное", fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
            }
        },
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
    )
}