package com.example.investwallet.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investwallet.dto.QuoteDTO
import com.example.investwallet.dto.converter.IUTag
import com.example.investwallet.dto.converter.newsDtoItem
import com.example.investwallet.dto.headlines.Headline
import com.example.investwallet.repository.ApiRepository
import com.example.investwallet.ui.search.SearchViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


sealed class StateLoad{
    object Loading: StateLoad()
    object Success: StateLoad()
    data class Error(val message: Error): StateLoad()
}

data class StateDetail(
    val headlineList:List<newsDtoItem> = emptyList(),
    val symbol: IUTag? = null,
    val stateLoad: StateLoad = StateLoad.Loading
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: ApiRepository
) : ViewModel() {




    private val detailNews = repository.detailNews

    private val _stateDetail: MutableStateFlow<StateDetail> = MutableStateFlow(StateDetail())
    val stateDetail: StateFlow<StateDetail>
        get() = _stateDetail

    fun check(_detailNews: newsDtoItem){
        detailNews.value = _detailNews
    }

    fun loadListDetailNews(_tag: String){
        viewModelScope.launch(Dispatchers.IO) {
            val list = async { repository.getHeadlines(_tag) }
            val text = _tag.split(":")
            val _symbol = async { repository.getListTicket(text = text.last(), exchange = text.first()) }


            withContext(Dispatchers.IO){
                _stateDetail.value = StateDetail(
                    list.await(),
                    _symbol.await().first(),
                    StateLoad.Success
                )

            }
        }
    }

}