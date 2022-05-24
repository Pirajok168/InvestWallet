package com.example.investwallet.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investwallet.dto.QuoteDTO
import com.example.investwallet.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


sealed class StateSearch{
    object NullSearch: StateSearch()
    object LoadingSearch: StateSearch()
    object EndSearch: StateSearch()
}


data class SearchViewState(
    val listSearchingTicket: List<QuoteDTO> = List(20){
        QuoteDTO("","","","","","", emptyList(), "") },

    val isLoading: StateSearch = StateSearch.LoadingSearch
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ApiRepository
) : ViewModel() {


    val searchValue: MutableState<String> = mutableStateOf("")
    private val _searchViewState: MutableStateFlow<SearchViewState> = MutableStateFlow(SearchViewState())


    val searchViewState: StateFlow<SearchViewState>
        get() = _searchViewState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val list  = repository.getListTicket("")
            withContext(Dispatchers.Main){
                _searchViewState.value = SearchViewState(list, StateSearch.EndSearch)
            }
        }
    }

    fun onSearch(newValue: String){
        viewModelScope.launch (Dispatchers.IO){
            withContext(Dispatchers.Main){
                searchValue.value = newValue
            }

            val list  = repository.getListTicket(newValue)

            withContext(Dispatchers.Main){
                val state = if (list.isEmpty()){
                    StateSearch.NullSearch
                }else{
                    StateSearch.EndSearch
                }
                _searchViewState.value = SearchViewState(list, state)
            }
        }
    }

}