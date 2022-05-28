package com.example.investwallet.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investwallet.dto.QuoteDTO
import com.example.investwallet.dto.converter.IUTag
import com.example.investwallet.dto.converter.newsDtoItem

import com.example.investwallet.repository.ApiRepository
import com.example.investwallet.repository.StateCollectData
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
    data class Error(val message: String): StateLoad()
}

data class StateDetail(
    val headlineList:List<newsDtoItem> = emptyList(),
    val symbol: IUTag? = null,
    val price: String = "140 $",
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

    fun loadListDetailNews(
        _tag: String,
        _category: String,
        _country: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            val list = async { repository.getHeadlines(_tag, category = _category) }
            val text = _tag.split(":")
            val _symbol = async { repository.getListTicket(text = text.last(), exchange = text.first(), type= _category) }
            val country = if (_country.isEmpty()){
                "${_symbol.await().first().country}"
            }else{
                _country
            }
            Log.e("_symbol","_symbol - $_tag")
            Log.e("_symbol","country - $country")

            val quoteDTO = async {
                when (country){
                    "RU"->{
                        repository.collectDataForShareRussia(_tag)
                    }
                    "US" ->{
                        repository.collectDataForShareAmerica(_tag)
                    }
                    else -> {

                        repository.collectDataForShareAmerica(_tag)
                    }
                }

            }


            //Log.e("_symbol",  "$_tag --- ${quoteDTO.await()?.data?.first()?.d?.first()}")

            val formatPrice = when(val state = quoteDTO.await()){
                is StateCollectData.Error -> {
                    " "
                }
                is StateCollectData.AmericaStock -> {
                    "${state.symbol} ${state.answerDTO.data.first().d.first()}"
                }
                is StateCollectData.RussiaStock ->{
                    "${state.answerDTO.data?.first()?.d.first()} ${state.symbol}"
                }
            }

            withContext(Dispatchers.IO){
                _stateDetail.value = StateDetail(
                    list.await(),
                    _symbol.await().first(),
                    formatPrice,
                    StateLoad.Success
                )

            }
        }
    }

}