package com.example.investwallet.ui.detail

import androidx.lifecycle.ViewModel
import com.example.investwallet.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DetailNewsViewModel @Inject constructor(
    private val repository: ApiRepository
) : ViewModel() {
    val detailNews = repository.detailNews

}