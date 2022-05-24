package com.example.investwallet.home


import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.investwallet.ui.theme.InvestWalletTheme
import com.example.investwallet.R
import java.text.SimpleDateFormat
import java.util.*


data class Ticket(
    val labelTicket: String,
    val priceTicket: Int,
    val iconTicket: Int,
    val amountOnTheAccount: Int
){
    val priceTicketOnTheAccount = priceTicket * amountOnTheAccount
}

object SampleData{
    val list: List<Ticket> = listOf(
        Ticket(
            labelTicket = "APPLE",
            priceTicket = 134,
            iconTicket = R.drawable.ic_launcher_foreground,
            amountOnTheAccount = 3
        ),
        Ticket(
            labelTicket = "APPLE",
            priceTicket = 134,
            iconTicket = R.drawable.ic_launcher_foreground,
            amountOnTheAccount = 3
        ),
        Ticket(
            labelTicket = "APPLE",
            priceTicket = 134,
            iconTicket = R.drawable.ic_launcher_foreground,
            amountOnTheAccount = 3
        ),
        Ticket(
            labelTicket = "APPLE",
            priceTicket = 134,
            iconTicket = R.drawable.ic_launcher_foreground,
            amountOnTheAccount = 3
        ),
    )
}


@Composable
fun Home() {
    val format = SimpleDateFormat("H:MM, EEE, MMM d")
    Scaffold(
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
            HomeTopBar(
                "Мой кошелёк",
                onSearch = {
                    TODO()
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            AccountMany(
                "10000000000 ₽",
                format.format(Date())
            )
            Portfolio(
                SampleData.list,
                onSeeAll = { TODO() },
                onOpenTicket = { TODO() }
            )

            Information(SampleData.list,)
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
        }
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
fun Portfolio(
    listPortfolio: List<Ticket>,
    onSeeAll: () -> Unit,
    onOpenTicket: (ticket: Ticket) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Моё Портфолио",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        ClickableText(
            text = AnnotatedString(
                "Показать все",
                spanStyles = listOf(
                    AnnotatedString.Range(SpanStyle(color = Color.Blue),0,"Показать все".length)
                )
            ),
            onClick = { onSeeAll() }
        )
    }

    LazyRow(
        contentPadding = PaddingValues(20.dp),
        horizontalArrangement=Arrangement.spacedBy(20.dp)
    ){
        items(listPortfolio){
            CardTicket(
                it,
                onOpenTicket=onOpenTicket
            )
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardTicket(
    ticket: Ticket,
    onOpenTicket: (ticket: Ticket) -> Unit
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
                            .data("https://upload.wikimedia.org/wikipedia/commons/thumb/3/3a/Cat03.jpg/1199px-Cat03.jpg")
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
                    text = ticket.labelTicket,
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
                Text(
                    text = ticket.priceTicketOnTheAccount.toString(),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 25.sp
                )
                Spacer(modifier = Modifier.size(10.dp))
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = ticket.amountOnTheAccount.toString(),
                        fontSize = 18.sp,
                        color = Color.LightGray
                    )
                }
            }
        }

    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Information(
    list: List<Ticket>,
) {
    Text(
        text = "Изменения портфеля",
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        modifier = Modifier.padding(20.dp)
    )
    val selectedList= listOf("Все", "24 ч.", "Лучшая динамика", "Худшая динамика")
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
                MaterialTheme.colors.primary else Color.White
            Row(
                Modifier
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) },
                        role = Role.Button
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    backgroundColor = backgroundColor,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = text,
                        color = contentColorFor(backgroundColor = backgroundColor),
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Spacer(modifier = Modifier.size(10.dp))
            }
        }
    }

    list.forEach{
        LittleCardTicket(ticket = it)
        Spacer(modifier = Modifier.size(10.dp))
    }
}

@Composable
fun LittleCardTicket(
    ticket: Ticket
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
            Surface() {
                Row() {
                    Surface(
                        modifier = Modifier.border(1.dp, Color.Black, CircleShape)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("https://upload.wikimedia.org/wikipedia/commons/thumb/3/3a/Cat03.jpg/1199px-Cat03.jpg")
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
                        Text(text = ticket.labelTicket, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.size(3.dp))
                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                            Text(text = ticket.labelTicket, color = Color.LightGray)
                        }
                    }
                }
            }

            Surface() {
                Text(text = "+0.67%", color = Color.Green)
            }

        }
        

        
    }
}

@Preview
@Composable
fun PreviewHome() {
    InvestWalletTheme{
        Home()
    }
}

@Preview
@Composable
fun PreviewLittleCardTicket() {
    InvestWalletTheme{
        LittleCardTicket(SampleData.list.first())
    }
}