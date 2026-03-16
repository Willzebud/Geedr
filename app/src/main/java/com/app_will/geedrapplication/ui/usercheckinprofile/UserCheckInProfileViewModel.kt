package com.app_will.geedrapplication.ui.usercheckinprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app_will.geedrapplication.R
import com.app_will.geedrapplication.navigation.MainNavigation
import com.app_will.geedrapplication.data.dto.UpdateUserDto
import com.app_will.geedrapplication.data.repository.ApiRepository
import com.app_will.geedrapplication.utils.API_RESPONSE_CODE_400
import com.app_will.geedrapplication.utils.API_RESPONSE_CODE_404
import com.app_will.geedrapplication.utils.API_RESPONSE_CODE_500
import com.app_will.geedrapplication.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserCheckInProfileViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel() {

    private val _userCheckInName = MutableStateFlow("")
    val userCheckInName = _userCheckInName.asStateFlow()
    private val _userCheckInAge = MutableStateFlow("")
    val userCheckInAge = _userCheckInAge.asStateFlow()
    private val _userCheckInGender = MutableStateFlow("")
    val userCheckInGender = _userCheckInGender.asStateFlow()
    private val _userCheckInSexualOrientation = MutableStateFlow("")
    val userCheckInSexualOrientation = _userCheckInSexualOrientation.asStateFlow()
    private val _userCheckInJob = MutableStateFlow("")
    val userCheckInJob = _userCheckInJob.asStateFlow()
    private val _userCheckInAboutMe = MutableStateFlow<List<String>>(emptyList())
    val userCheckInAboutMe = _userCheckInAboutMe.asStateFlow()
    private val _userCheckInImgProfile = MutableStateFlow("")
    val userCheckInImgProfile = _userCheckInImgProfile.asStateFlow()
    private val _userCheckInPassions = MutableStateFlow<List<String>>(emptyList())
    val userCheckInPassions = _userCheckInPassions.asStateFlow()

    private val _responseUserSharedFlow = MutableSharedFlow<UiEvent>()
    val responseUserStateFlow = _responseUserSharedFlow.asSharedFlow()

    private val _navigateToScreenSharedFlow = MutableSharedFlow<MainNavigation>()
    val navigateToScreenSharedFlow = _navigateToScreenSharedFlow.asSharedFlow()

    fun getUserCheckInProfile(
        checkInId: Long
    ) {

        viewModelScope.launch {
            try {
                val res = withContext(Dispatchers.IO) {
                    apiRepository.getUser(checkInId)
                }

                if (res.isSuccessful) {
                    res.body()?.let { listUser ->
                        listUser.forEach { userDto ->
                            _userCheckInName.value = userDto.userName
                            _userCheckInAge.value = userDto.userAge.toString()
                            _userCheckInGender.value = userDto.userGender
                            _userCheckInSexualOrientation.value = userDto.userSexualOrientation
                            _userCheckInJob.value = userDto.userJob
                            _userCheckInAboutMe.value = userDto.userAboutMe
                            _userCheckInImgProfile.value = userDto.userPicture
                            _userCheckInPassions.value = userDto.userPassions
                        }
                    }
                } else {
                    when (res.code()) {
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
        }
    }

    fun userCheckInProfileVisibility(
        userCheckInId: Long,
        userCheckInVisibility: Boolean
    ) {
        viewModelScope.launch {
            try {
                val res = withContext(Dispatchers.IO) {
                    apiRepository.updateUserVisibility(
                        userCheckInId = userCheckInId,
                        userCheckInVisibility = UpdateUserDto(
                            isUserVisible = userCheckInVisibility
                        )
                    )
                }

                if(!res.isSuccessful){
                    when (res.code()) {
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
        }
    }

    fun navigateToScreen(){
        viewModelScope.launch {
            _navigateToScreenSharedFlow.emit(MainNavigation.Messaging)
        }
    }
}

