package com.example.investwallet.ui.all

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investwallet.database.FavoriteTicket
import com.example.investwallet.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject




data class StateSeeAll(
    val listFavoriteTicket: List<FavoriteTicket> = emptyList()
)

@HiltViewModel
class SeeAllViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
) : ViewModel() {
    private val _listFavoriteTicket = databaseRepository.listFavoriteTicket
    private val _stateSeeAll: MutableStateFlow<StateSeeAll> = MutableStateFlow(StateSeeAll())
    val state: StateFlow<StateSeeAll>
        get() = _stateSeeAll


    init {
        viewModelScope.launch {
            _listFavoriteTicket.collect{
                _stateSeeAll.value = StateSeeAll(it)
            }
        }
    }
}