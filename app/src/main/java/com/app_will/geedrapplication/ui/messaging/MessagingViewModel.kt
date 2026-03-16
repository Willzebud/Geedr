package com.app_will.geedrapplication.ui.messaging

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app_will.geedrapplication.R
import com.app_will.geedrapplication.navigation.MainNavigation
import com.app_will.geedrapplication.network.dto.MessagesDto
import com.app_will.geedrapplication.network.dto.UpdateLikeStatus
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
class MessagingViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel() {

    private val _messagesListStateFlow = MutableStateFlow<List<MessagesDto>>(emptyList())
    val messageListStateFlow = _messagesListStateFlow.asStateFlow()

    private val _navigateToScreen = MutableSharedFlow<String>()
    val navigateToScreen = _navigateToScreen.asSharedFlow()

    private val _responseUserSharedFlow = MutableSharedFlow<UiEvent>()
    val responseUserStateFlow = _responseUserSharedFlow.asSharedFlow()

    init {
        getMessages()
    }

    private fun getMessages() {
        viewModelScope.launch {

            try {

                val res = withContext(Dispatchers.IO) {
                    apiRepository.getMessages()
                }

                res.body()?.let { message ->
                    _messagesListStateFlow.value = message
                }


            } catch (e: Exception) {
                _responseUserSharedFlow.emit(
                    UiEvent.ShowToast(R.string.error_occurred)
                )
            }
        }
    }

    fun navigateToScreen(
    ) {
        viewModelScope.launch {
            _navigateToScreen.emit("${MainNavigation.UserCheckinProfil.route}/6")
        }
    }


    fun likeVisibility(
        likeVisibility: Boolean
    ) {
        viewModelScope.launch {
            try {
                val res = withContext(Dispatchers.IO) {
                    apiRepository.updateLikeVisibility(
                        messageId = 1,
                        likeVisibility = UpdateLikeStatus(
                            isLikeVisible = likeVisibility
                        )
                    )
                }

                if(res.isSuccessful){
                    getMessages()
                }

            } catch (e: Exception) {
                _responseUserSharedFlow.emit(
                    UiEvent.ShowToast(R.string.error_occurred)
                )
            }
        }
    }
}