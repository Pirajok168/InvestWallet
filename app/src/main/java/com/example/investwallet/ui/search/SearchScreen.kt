package com.example.investwallet.ui.search

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.investwallet.R
import com.example.investwallet.dto.QuoteDTO
import me.vponomarenko.compose.shimmer.shimmer



@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onOpen: (tag: String, category: String, country: String) -> Unit,
) {
    LaunchedEffect(key1 = 0, block = {
        searchViewModel.updateList(ToolsTicket.Stocks)
    })

    val searchViewState = searchViewModel.searchViewState.collectAsState()
    val selectedList = listOf(ToolsTicket.All, ToolsTicket.Stocks, ToolsTicket.Crypto, )
    val (selectedOption, onOptionSelected ) = remember { mutableStateOf(selectedList[1]) }

    Scaffold(
        topBar = { SearchTopBar(
            onBack =onBack,
            searchViewModel.searchValue.value,
            onEditText = { searchViewModel.onSearch(it,selectedOption) }
        ) }
    ) {
            contentPadding ->

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(20.dp),
            modifier = Modifier.padding(contentPadding)
        ){

            item {
                LineButton(
                    selectedList,
                    selectedOption = selectedOption,
                    onOptionSelected = onOptionSelected,
                    onClick = {
                        searchViewModel.updateList(it)
                    }
                )
            }
            when(searchViewState.value.isLoading){
                StateSearch.EndSearch -> {

                    items(searchViewState.value.listSearchingTicket){
                        SearchCardTicket(
                            it,
                            onOpen={
                                    symbol->
                                onOpen(symbol.getTag(),selectedOption.type, symbol.country?:"")
                            }
                        )
                    }
                }
                StateSearch.LoadingSearch -> {
                    items(20){
                        _PlaceholderSearchCardTicket()
                    }
                }
                StateSearch.NullSearch -> {

                }
            }


        }
    }
}




@Composable
fun LineButton(
    selectedList: List<ToolsTicket>,
    selectedOption: ToolsTicket,
    onOptionSelected: (ToolsTicket) -> Unit,
    onClick:(option: ToolsTicket) -> Unit
) {


    Row() {
        selectedList.forEach{
                option ->
            val backgroundColor = if (option == selectedOption)
                MaterialTheme.colors.primary else MaterialTheme.colors.surface
            Row(
                Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .selectable(
                        selected = (option == selectedOption),
                        onClick = {
                            onClick(option)
                            onOptionSelected(option)
                        },
                        role = Role.Button
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    backgroundColor = backgroundColor,
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Text(
                        text = option.text,
                        color = contentColorFor(backgroundColor = backgroundColor),
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.size(10.dp))
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchTopBar(
    onBack: () -> Unit,
    searchValue: String,
    onEditText: (newValue: String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val requester = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current




   var expandedKeyboard by remember {
       mutableStateOf(true)
   }

    val rotate by animateFloatAsState(
        targetValue = if (expandedKeyboard) 0f else 360f,
        animationSpec = tween(700   , easing = FastOutSlowInEasing),
    )


    LaunchedEffect(key1 = 0, block = {
        requester.requestFocus()
    })


    TopAppBar(
        title = {
            BasicTextField(
                value = searchValue,
                onValueChange = { onEditText(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(requester),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone={
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                ),
                decorationBox = {
                    innerTextField->
                    if (searchValue.isEmpty()) {
                        Row() {
                            Text(text = "Поиск", fontWeight = FontWeight.Light, fontSize = 18.sp)
                        }
                    }
                    innerTextField()
                },

            )
        },
        navigationIcon = { IconButton(onClick = { onBack() }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
        } },
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
        actions = {
            IconButton(onClick = {
                onEditText("")
                expandedKeyboard = !expandedKeyboard
                focusManager.clearFocus()
                keyboardController?.hide()
            }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "",
                    modifier = Modifier.rotate(rotate)
                )
            }
        },
        modifier = Modifier.statusBarsPadding()
    )
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchCardTicket(
    ticket: QuoteDTO,
    onOpen: (ticket: QuoteDTO) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(20.dp),
        onClick = {
            onOpen(ticket)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(modifier = Modifier.weight(6f)) {
                Log.e("logo", ticket.toString())
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
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(
                        text = ticket.getDescriptions(),
                        fontWeight = FontWeight.Bold,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Spacer(modifier = Modifier.size(5.dp))
            Surface(modifier = Modifier.weight(1f)) {
                Text(
                    text = ticket.exchange,
                    fontWeight = FontWeight.ExtraLight,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }

        }

    }
}

@Composable
fun _PlaceholderSearchCardTicket() {
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

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier
                            .size(50.dp)
                            .background(color = Color(0xFFF3F3F3), CircleShape)
                            .shimmer()
                    ){}

                    Spacer(modifier = Modifier.size(20.dp))

                    Surface(modifier = Modifier
                        .size(100.dp, 50.dp)
                        .background(color = Color(0xFFF3F3F3), RoundedCornerShape(4.dp))
                        .shimmer()) {}
                }
            }

            Spacer(modifier = Modifier.size(5.dp))

            Surface(modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .background(color = Color(0xFFF3F3F3), RoundedCornerShape(4.dp))
                .shimmer()) {}

        }

    }
}



