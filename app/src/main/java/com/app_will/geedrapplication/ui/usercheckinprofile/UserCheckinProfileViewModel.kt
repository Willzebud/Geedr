package com.app_will.geedrapplication.ui.usercheckinprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app_will.geedrapplication.R
import com.app_will.geedrapplication.network.dto.UpdateUserDto
import com.app_will.geedrapplication.repository.ApiRepository
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
class UserCheckinProfileViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel() {

    private val _userCheckinName = MutableStateFlow("")
    val userCheckinName = _userCheckinName.asStateFlow()
    private val _userCheckinAge = MutableStateFlow("")
    val userCheckinAge = _userCheckinAge.asStateFlow()
    private val _userCheckinGender = MutableStateFlow("")
    val userCheckinGender = _userCheckinGender.asStateFlow()
    private val _userCheckinSexualOrientation = MutableStateFlow("")
    val userCheckinSexualOrientation = _userCheckinSexualOrientation.asStateFlow()
    private val _userCheckinJob = MutableStateFlow("")
    val userCheckinJob = _userCheckinJob.asStateFlow()
    private val _userCheckinAboutMe = MutableStateFlow<List<String>>(emptyList())
    val userCheckinAboutMe = _userCheckinAboutMe.asStateFlow()
    private val _userCheckinImgProfile = MutableStateFlow("")
    val userCheckinImgProfile = _userCheckinImgProfile.asStateFlow()
    private val _userCheckinPassions = MutableStateFlow<List<String>>(emptyList())
    val userCheckinPassions = _userCheckinPassions.asStateFlow()

    private val _responseUserSharedFlow = MutableSharedFlow<UiEvent>()
    val responseUserStateFlow = _responseUserSharedFlow.asSharedFlow()

    fun getUserCheckinProfil(
        checkinId: Long
    ) {

        viewModelScope.launch {
            try {
                val res = withContext(Dispatchers.IO) {
                    apiRepository.getUser(checkinId)
                }

                if (res.isSuccessful) {
                    res.body()?.let { listUser ->
                        listUser.forEach { userDto ->
                            _userCheckinName.value = userDto.userName
                            _userCheckinAge.value = userDto.userAge.toString()
                            _userCheckinGender.value = userDto.userGender
                            _userCheckinSexualOrientation.value = userDto.userSexualOrientation
                            _userCheckinJob.value = userDto.userJob
                            _userCheckinAboutMe.value = userDto.userAboutMe
                            _userCheckinImgProfile.value = userDto.userPicture
                            _userCheckinPassions.value = userDto.userPassions
                        }
                    }
                }

            } catch (e: Exception) {
                _responseUserSharedFlow.emit(
                    UiEvent.ShowToast(R.string.error_occurred)
                )
            }
        }
    }

    fun userCheckinProfileVisibility(
        userCheckinId: Long,
        userCheckinVisibility: Boolean
    ) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    apiRepository.updateUserVisibility(
                        userCheckinId = userCheckinId,
                        userCheckinVisibility = UpdateUserDto(
                            isUserVisible = userCheckinVisibility
                        )
                    )
                }

            } catch (e: Exception) {
                _responseUserSharedFlow.emit(
                    UiEvent.ShowToast(R.string.error_occurred)
                )
            }
        }
    }
}

