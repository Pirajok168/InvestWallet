package com.example.investwallet.ui.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
import okhttp3.internal.notify
import javax.inject.Inject


data class HomeState(
    val listFavoriteTicket: List<FavoriteTicket> = emptyList()
)

//TODO(Переделать)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val repository: ApiRepository
): ViewModel() {
    private val _listFavoriteTicket = databaseRepository.listFavoriteTicket
    private val _stateHome: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val stateHome: StateFlow<HomeState>
        get() = _stateHome


    init {

        viewModelScope.launch(Dispatchers.IO) {
            _listFavoriteTicket.transform{
                   _istFavoriteTicket ->
                val list: MutableList<FavoriteTicket> = mutableListOf()
                _istFavoriteTicket.forEach {
                        _favorite ->
                    if (_favorite.price.isEmpty()){
                        val price = loadPrice(_favorite)
                        _favorite.price = price
                        list.add(_favorite)
                    }else{
                        list.add(_favorite)
                    }
                }
                emit(list)
            }.collect {
                _stateHome.value = HomeState(it)
            }

        }
    }


    suspend fun loadPrice(_favoriteTicket: FavoriteTicket): String = runBlocking(Dispatchers.IO)  {
        Log.e("_favoriteTicket", _favoriteTicket.toString())
        val favoriteTicket = async {
            when (_favoriteTicket.country){
                "RU"->{
                    repository.collectDataForShareRussia(_favoriteTicket.getTag())
                }
                "US" ->{
                    repository.collectDataForShareAmerica(_favoriteTicket.getTag())
                }
                else -> {
                    repository.collectDataForCrypto(_favoriteTicket.getTag())
                }
            }
        }
        val formatPrice = when(val state = favoriteTicket.await()){
            is StateCollectData.Error -> {
                " "
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
        }
        withContext(Dispatchers.Main){
            formatPrice
        }
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