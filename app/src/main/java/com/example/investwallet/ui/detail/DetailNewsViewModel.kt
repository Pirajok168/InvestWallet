package com.example.investwallet.ui.detail

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investwallet.dto.converter.*
import com.example.investwallet.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject



data class StateDetailNews(
    val annotationText: List<AnnotatedString> = emptyList(),
    val title: String = "",
    val listChip: List<RelatedSymbol> = emptyList(),
)

@HiltViewModel
class DetailNewsViewModel @Inject constructor(
    private val repository: ApiRepository
) : ViewModel() {
    private val detailNews = repository.detailNews
    private val _stateDetailNews: MutableStateFlow<StateDetailNews> = MutableStateFlow(
        StateDetailNews()
    )


    val stateDetailNews: StateFlow<StateDetailNews>
        get() = _stateDetailNews



    fun getData(isSystemInDarkTheme: Boolean){
        viewModelScope.launch(Dispatchers.IO){
            val listAnnotatedString: MutableList<AnnotatedString> = mutableListOf()
            val listChip: MutableList<RelatedSymbol> = mutableListOf()
            detailNews.value?.astDescription?.children?.forEach {
                if (it is Content.ChildrenContent){
                    val test = it
                    val list = test.content.children

                    val annotationText: AnnotatedString = buildAnnotatedString {
                        list.forEach {
                                con->
                            if (con is Content.StringContent){
                                withStyle(
                                    style = SpanStyle(
                                        color = if (isSystemInDarkTheme) Color.White else Color.Black,
                                        fontSize = 16.sp,
                                    ),
                                ){
                                    append(con.content)
                                }

                            }else if (con is Content.ChildrenContent){
                                pushStringAnnotation(tag = "URL",
                                    annotation = "https://developer.android.com")

                                withStyle(style = SpanStyle(
                                    color = if (isSystemInDarkTheme) darkColors().primary
                                            else lightColors().primary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,

                                )) {
                                    append(con.content.params?.text?: "")
                                }
                                pop()
                            }
                        }
                    }
                    listAnnotatedString.add(annotationText)
                }else{

                }
            }
            detailNews.value?.relatedSymbols?.forEach {
                listChip.add(it)
            }

            withContext(Dispatchers.Main){
                _stateDetailNews.value = StateDetailNews(
                    listAnnotatedString,
                    detailNews.value?.title ?: " ",
                    listChip
                )
            }
        }
    }


}