package com.example.investwallet.search

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.investwallet.R
import com.example.investwallet.dto.QuoteDTO
import com.example.investwallet.ui.theme.InvestWalletTheme

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel()
) {

    val listSearchingTicket = searchViewModel.listSearchingTicket
    Scaffold(
        topBar = { SearchTopBar(
            onBack ={ TODO() },
            searchViewModel.searchValue.value,
            onEditText = { searchViewModel.onSearch(it) }
        ) }
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(20.dp)
        ){
            items(listSearchingTicket.value){
                SearchCardTicket(it)
            }
        }
    }
}

@Composable
fun SearchTopBar(
    onBack: () -> Unit,
    searchValue: String,
    onEditText: (newValue: String) -> Unit
) {
    TopAppBar(
        title = { BasicTextField(value = searchValue, onValueChange = { onEditText(it) }) },
        navigationIcon = { IconButton(onClick = { onBack() }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
        } },
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
    )
}

@Composable
fun SearchCardTicket(
    ticket: QuoteDTO
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(modifier = Modifier.weight(6f)) {
                Log.e("logo", "https://s3-symbol-logo.tradingview.com/${ticket.logoid}--big.svg")
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.border(1.dp, Color.Black, CircleShape)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("https://s3-symbol-logo.tradingview.com/${ticket.logoid}--big.svg")
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
                        text = ticket.getDescription,
                        fontWeight = FontWeight.Bold,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.size(5.dp))
            Surface(modifier = Modifier.weight(1f)) {
                Text(
                    text = ticket.exchange, 
                    fontWeight = FontWeight.ExtraLight,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }

    }
}
@Preview
@Composable
fun PreviewSearchScreen() {
    InvestWalletTheme {
        SearchScreen()
    }
}