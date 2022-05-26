package com.example.investwallet.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investwallet.dto.QuoteDTO
import com.example.investwallet.dto.converter.newsDtoItem
import com.example.investwallet.dto.headlines.Headline
import com.example.investwallet.repository.ApiRepository
import com.example.investwallet.ui.search.SearchViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: ApiRepository
) : ViewModel() {
    val headlineList: MutableStateFlow<List<newsDtoItem>> = MutableStateFlow(emptyList())
    private val _symbol = repository.symbol

    val symbol: StateFlow<QuoteDTO?>
        get() = _symbol

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val list = repository.getHeadlines()


            withContext(Dispatchers.IO){
                headlineList.value = list
            }
        }
    }
}