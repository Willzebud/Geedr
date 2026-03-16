package com.app_will.geedrapplication.ui.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app_will.geedrapplication.R
import com.app_will.geedrapplication.navigation.MainNavigation
import com.app_will.geedrapplication.data.dto.PlacesDto
import com.app_will.geedrapplication.data.repository.ApiRepository
import com.app_will.geedrapplication.utils.API_RESPONSE_CODE_400
import com.app_will.geedrapplication.utils.API_RESPONSE_CODE_404
import com.app_will.geedrapplication.utils.API_RESPONSE_CODE_500
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
class PlacesViewModel @Inject constructor(
    private val apiRepository: ApiRepository
): ViewModel() {
    private val _isProgressBarActiveStateFlow = MutableStateFlow(false)
    val isProgressBarActiveStateFlow = _isProgressBarActiveStateFlow.asStateFlow()

    private val _isRefreshingStateFlow = MutableStateFlow(false)
    val isRefreshingStateFlow = _isRefreshingStateFlow.asStateFlow()

    private val _placesListStateFlow = MutableStateFlow<List<PlacesDto>>(emptyList())
    val placesListStateFlow = _placesListStateFlow.asStateFlow()

    private val _responseUserSharedFlow = MutableSharedFlow<UiEvent>()
    val responseUserStateFlow = _responseUserSharedFlow.asSharedFlow()

    private val _navigateToCheckInProfilesSharedFlow = MutableSharedFlow<String>()
    val navigateToCheckInProfilesSharedFlow = _navigateToCheckInProfilesSharedFlow.asSharedFlow()

    private val _userCheckInActive = MutableStateFlow(0)
    val userCheckInActive = _userCheckInActive.asStateFlow()


    init {
        getPlaces()
    }

    fun getPlaces() {
        viewModelScope.launch {
            _isProgressBarActiveStateFlow.value = true

            try {
                val placesResponse = withContext(Dispatchers.IO) {
                    apiRepository.getPlaces()
                }

                if (placesResponse.isSuccessful) {
                    _placesListStateFlow.value = placesResponse.body().orEmpty()
                } else {
                    when (placesResponse.code()) {
                        API_RESPONSE_CODE_400 -> _responseUserSharedFlow.emit(
                            UiEvent.ShowToast(R.string.error_occurred)
                        )

                        API_RESPONSE_CODE_404 -> _responseUserSharedFlow.emit(
                            UiEvent.ShowToast(R.string.api_response_404)
                        )

                        API_RESPONSE_CODE_500 -> _responseUserSharedFlow.emit(
                            UiEvent.ShowToast(R.string.api_response_500)
                        )
                    }
                }

                val usersResponse = withContext(Dispatchers.IO) {
                    apiRepository.getUsers()
                }

                if (usersResponse.isSuccessful) {
                    val users = usersResponse.body().orEmpty()

                    _userCheckInActive.value = 0

                    users.forEach { user ->
                        if (user.isUserVisible) {
                            _userCheckInActive.value += 1
                        }
                    }
                } else {
                    when (usersResponse.code()) {
                        API_RESPONSE_CODE_400 -> _responseUserSharedFlow.emit(
                            UiEvent.ShowToast(R.string.error_occurred)
                        )

                        API_RESPONSE_CODE_404 -> _responseUserSharedFlow.emit(
                            UiEvent.ShowToast(R.string.api_response_404)
                        )

                        API_RESPONSE_CODE_500 -> _responseUserSharedFlow.emit(
                            UiEvent.ShowToast(R.string.api_response_500)
                        )
                    }
                }
            } catch (e: Exception) {
                _responseUserSharedFlow.emit(
                    UiEvent.ShowToast(R.string.error_occurred)
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

    fun navigateToUsersCheckIn(
        placeType: String,
        placeName: String,
        addressCity: String,
    ){
        viewModelScope.launch {
            _navigateToCheckInProfilesSharedFlow
                .emit(
                    "${MainNavigation.CheckInUser.route}/$placeType/$placeName/$addressCity"
                )
        }
    }
}