package com.app_will.geedrapplication.ui.usersprofiles.userprofil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app_will.geedrapplication.R
import com.app_will.geedrapplication.data.repository.ApiRepository
import com.app_will.geedrapplication.data.sharedpreferences.SharedPreferencesManager
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
class UserProfileViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    private val myPref: SharedPreferencesManager
) : ViewModel() {
    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()
    private val _userAge = MutableStateFlow("")
    val userAge = _userAge.asStateFlow()
    private val _userGender = MutableStateFlow("")
    val userGender = _userGender.asStateFlow()
    private val _userSexualOrientation = MutableStateFlow("")
    val userSexualOrientation = _userSexualOrientation.asStateFlow()
    private val _userJob = MutableStateFlow("")
    val userJob = _userJob.asStateFlow()
    private val _userAboutMe = MutableStateFlow<List<String>>(emptyList())
    val userAboutMe = _userAboutMe.asStateFlow()
    private val _userImgProfile = MutableStateFlow("")
    val userImgProfile = _userImgProfile.asStateFlow()
    private val _userPassions = MutableStateFlow<List<String>>(emptyList())
    val userPassions = _userPassions.asStateFlow()

    private val _responseUserSharedFlow = MutableSharedFlow<UiEvent>()
    val responseUserSharedFlow = _responseUserSharedFlow.asSharedFlow()


    fun getUserProfile(
    ) {

        viewModelScope.launch {
            try {
                val res = withContext(Dispatchers.IO) {
                    apiRepository.getUser(myPref.getUser()!!.id.toLong())
                }

                if (res.isSuccessful) {
                    res.body()?.let { listUser ->
                        listUser.forEach { userDto ->
                            _userName.value = userDto.userName
                            _userAge.value = userDto.userAge.toString()
                            _userGender.value = userDto.userGender
                            _userSexualOrientation.value = userDto.userSexualOrientation
                            _userJob.value = userDto.userJob
                            _userAboutMe.value = userDto.userAboutMe
                            _userImgProfile.value = userDto.userPicture
                            _userPassions.value = userDto.userPassions
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
}