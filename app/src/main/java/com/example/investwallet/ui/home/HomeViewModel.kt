package com.example.investwallet.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investwallet.database.ActiveUser
import com.example.investwallet.database.User
import com.example.investwallet.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ApiRepository
): ViewModel() {
     val listFavoriteTicket = repository.listFavoriteTicket

    fun create(){
        /*viewModelScope.launch(Dispatchers.IO) {
            repository.insertActiveUser(
                ActiveUser(
                    User(1),
                    listFavoriteTicket = emptyList()
                )
            )
        }*/
    }

}