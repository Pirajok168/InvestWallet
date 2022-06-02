package com.example.investwallet.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investwallet.database.ActiveUser
import com.example.investwallet.database.FavoriteTicket
import com.example.investwallet.database.User
import com.example.investwallet.repository.ApiRepository
import com.example.investwallet.repository.DatabaseRepository
import com.example.investwallet.repository.StateCollectData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

enum class StateLoad{
    LOADING,
    SUCCESS
}

data class HomeState(
    val listFavoriteTicket: List<FavoriteTicket> = emptyList(),
    val stateLoad: StateLoad
)

//TODO(Переделать)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val repository: ApiRepository
): ViewModel() {
    private val _listFavoriteTicket = databaseRepository.listFavoriteTicket
    private val _stateHome: MutableStateFlow<HomeState> = MutableStateFlow(HomeState(stateLoad= StateLoad.LOADING))
    val stateHome: StateFlow<HomeState>
        get() = _stateHome




    val test: StateFlow<HomeState> = _listFavoriteTicket.transform {
            _istFavoriteTicket ->
        emit(HomeState(_istFavoriteTicket, StateLoad.LOADING))
        val _listData = mutableListOf<FavoriteTicket>()
        _istFavoriteTicket.forEach {
                _favorite ->
            _favorite.price = loadPrice(_favorite)
            _listData.add(_favorite)
        }
        emit(HomeState(_listData, StateLoad.SUCCESS))

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = HomeState(stateLoad = StateLoad.SUCCESS)
    )








    suspend fun loadPrice(_favoriteTicket: FavoriteTicket): String  {
        Log.e("_favoriteTicket", _favoriteTicket.toString())
        val favoriteTicket =
            when (_favoriteTicket.country){
                "RU"->{
                    repository.collectDataForShareRussia(_favoriteTicket.getTag())
                }
                "US" ->{
                    repository.collectDataForShareAmerica(_favoriteTicket.getTag())
                }
                "IN" ->{
                    repository.collectDataForShareIndia(_favoriteTicket.getTag())
                }
                else -> {
                    repository.collectDataForCrypto(_favoriteTicket.getTag())
                }
            }

        val formatPrice = when(val state = favoriteTicket){
            is StateCollectData.Error -> {
                " "
            }
            is StateCollectData.AmericaStock -> {
                "${state.symbol} ${state.answerDTO.data.first().d.first()}"
            }
            is StateCollectData.RussiaStock ->{
                "${state.answerDTO.data.first().d.first()} ${state.symbol}"
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
        return  formatPrice
    }


    fun create(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                databaseRepository.insertActiveUser(
                    ActiveUser(
                        User(1),
                        listFavoriteTicket = emptyList()
                    )
                )
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

}