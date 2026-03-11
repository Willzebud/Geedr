package com.app_will.geedrapplication.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app_will.geedrapplication.R
import com.app_will.geedrapplication.navigation.ScreenNavigation
import com.app_will.geedrapplication.network.dto.PlacesDto
import com.app_will.geedrapplication.repository.ApiRepository
import com.app_will.geedrapplication.utils.GeoLocalisation
import com.app_will.geedrapplication.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel() {

    private val _isProgressBarActiveStateFlow = MutableStateFlow(false)
    val isProgressBarActiveStateFlow = _isProgressBarActiveStateFlow.asStateFlow()

    private val _isRefreshingStateFlow = MutableStateFlow(false)
    val isRefreshingStateFlow = _isRefreshingStateFlow.asStateFlow()

    private val _placesListStateFlow = MutableStateFlow<List<PlacesDto>>(emptyList())
    val placesListStateFlow = _placesListStateFlow.asStateFlow()

    private val _responseUserStateFlow = MutableSharedFlow<UiEvent>()
    val responseUserStateFlow = _responseUserStateFlow.asSharedFlow()

    private val _navigateToCheckinProfileSharedFlow = MutableSharedFlow<String>()
    val navigateToCheckinProfileSharedFlow = _navigateToCheckinProfileSharedFlow.asSharedFlow()


    init {
        getPlaces()
    }

    private fun getPlaces() {

        viewModelScope.launch {
            _isProgressBarActiveStateFlow.value = true

            try {
                val res = withContext(Dispatchers.IO) {
                    apiRepository.getPlaces()
                }

                if (res.isSuccessful) {
                    _placesListStateFlow.value = res.body().orEmpty()
                }

            } catch (e: Exception) {
                _responseUserStateFlow.emit(
                    UiEvent.SnackBar(R.string.error_occurred)
                )
            }
            _isProgressBarActiveStateFlow.value = false
        }
    }

    fun swipeRefresh() {
        viewModelScope.launch {
            _isRefreshingStateFlow.value = true
            delay(2000)
            getPlaces()
            _isRefreshingStateFlow.value = false
            _isProgressBarActiveStateFlow.value = false
        }
    }

    fun navigateToUsersCheckinProfile(
        placeType: String,
        placeName: String,
        addressCity: String,
        userCheckin: Int
        ){
        viewModelScope.launch {
            _navigateToCheckinProfileSharedFlow
                .emit(
                    "${ScreenNavigation.CheckinProfile.route}/$placeType/$placeName/$addressCity/$userCheckin"
                )
        }
    }
}