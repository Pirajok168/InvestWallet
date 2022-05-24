package com.example.investwallet.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investwallet.dto.QuoteDTO
import com.example.investwallet.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ApiRepository
) : ViewModel() {
    val listSearchingTicket: MutableState<List<QuoteDTO>> = mutableStateOf(emptyList())
    val searchValue: MutableState<String> = mutableStateOf("")

    fun onSearch(newValue: String){
        viewModelScope.launch (Dispatchers.IO){
            withContext(Dispatchers.Main){
                searchValue.value = newValue
            }

            val list  = repository.getListTicket(newValue)

            withContext(Dispatchers.Main){
                listSearchingTicket.value = list
            }
        }
    }

}