package com.example.investwallet.ui.detail

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investwallet.database.FavoriteTicket
import com.example.investwallet.dto.QuoteDTO
import com.example.investwallet.dto.converter.IUTag
import com.example.investwallet.dto.converter.newsDtoItem

import com.example.investwallet.repository.ApiRepository
import com.example.investwallet.repository.DatabaseRepository
import com.example.investwallet.repository.StateCollectData
import com.example.investwallet.ui.search.SearchViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.random.Random


sealed class StateLoad{
    object Loading: StateLoad()
    object Success: StateLoad()
    data class Error(val message: String): StateLoad()
}

data class StateDetail(
    val headlineList:List<newsDtoItem> = emptyList(),
    val symbol: IUTag? = null,
    val price: String = "140 $",
    val isFavorite: MutableState<Boolean> = mutableStateOf(false),
    val stateLoad: StateLoad = StateLoad.Loading
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: ApiRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {




    private val detailNews = repository.detailNews

    private val _stateDetail: MutableStateFlow<StateDetail> = MutableStateFlow(StateDetail())
    val stateDetail: StateFlow<StateDetail>
        get() = _stateDetail

    fun check(_detailNews: newsDtoItem){
        detailNews.value = _detailNews
    }

    private lateinit var _symbol: QuoteDTO
    private var _databaseFavoriteTicket: FavoriteTicket? = null
    fun onFavorite(isFavoriteTicket: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            with(databaseRepository) {
                if(isFavoriteTicket){
                    insertFavoriteTicket(
                        FavoriteTicket(
                            id = _symbol.hashCode(),
                            userOwnerId = 1,
                            symbol = _symbol.symbol,
                            logoid = _symbol.logoid,
                            base_currency_logoid = _symbol.`base-currency-logoid`,
                            country = _symbol.country ?: "",
                            exchange = _symbol.exchange,
                            type = _symbol.type,
                            typespecs = _symbol.typespecs?.firstOrNull(),
                            prefix = _symbol.prefix
                        )
                    )
                }else{
                    deleteFavoriteTicket(
                        _symbol.hashCode()
                    )
                }
            }
        }
    }

    fun loadListDetailNews(
        _tag: String,
        _category: String,
        _country: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            val list = async { repository.getHeadlines(_tag, category = _category) }
            val text = _tag.split(":")
            val _symbolSearch = async { repository.getListTicket(text = text.last(), exchange = text.first(), type= _category) }

            val country = if (_country.isEmpty()){
                "${_symbolSearch.await().first().country}"
            }else{
                _country
            }
            Log.e("_symbol","_symbol - ${list.await().toString()}")
            Log.e("_symbol","country - $country")

            val quoteDTO = async {
                when (country){
                    "RU"->{
                        repository.collectDataForShareRussia(_tag)
                    }
                    "US" ->{
                        repository.collectDataForShareAmerica(_tag)
                    }
                    "IN" ->{
                        repository.collectDataForShareIndia(_tag)
                    }
                    "GB" ->{
                        repository.collectDataForShareUk(_tag)
                    }
                    else -> {
                        repository.collectDataForCrypto(_tag)
                    }
                }

            }
            _symbol = _symbolSearch.await().first()

            val _databaseFavoriteTicketJob = async {
                databaseRepository.getFavorite(_symbol.symbol)
            }


            val formatPrice = when(val state = quoteDTO.await()){
                is StateCollectData.Error -> {
                    state.message
                }
                is StateCollectData.AmericaStock -> {
                    "${state.symbol} ${state.answerDTO.data.first().d.first()}"
                }
                is StateCollectData.RussiaStock ->{
                    "${state.answerDTO.data?.first()?.d.first()} ${state.symbol}"
                }
                is StateCollectData.CryptoStock -> {
                    "${state.symbol} ${state.answerDTO.data.first().d.first()}"
                }
                is StateCollectData.IndiaStock -> {
                    "${state.symbol} ${state.answerDTO.data.first().d.first()}"
                }
                is StateCollectData.UkStock -> {
                    "${state.symbol} ${state.answerDTO.data.first().d.first()}"
                }
            }
            _databaseFavoriteTicket = _databaseFavoriteTicketJob.await()
            Log.e("_databaseFavoriteTicket", _databaseFavoriteTicket.toString())
            withContext(Dispatchers.IO){
                _stateDetail.value = StateDetail(
                    list.await(),
                    _symbolSearch.await().first(),
                    formatPrice,
                    mutableStateOf(_databaseFavoriteTicket?.symbol == _symbol.symbol),
                    StateLoad.Success
                )

            }
        }
    }

}