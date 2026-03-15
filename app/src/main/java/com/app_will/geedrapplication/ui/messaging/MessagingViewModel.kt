package com.app_will.geedrapplication.ui.messaging

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app_will.geedrapplication.network.dto.MessagesDto
import com.app_will.geedrapplication.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val _messageUrlImgStateFlow = MutableStateFlow("")
    val messageUrlImgStateFlow = _messageUrlImgStateFlow.asStateFlow()
    private val _messageUserNameStateFlow = MutableStateFlow("")
    val messageUserNameStateFlow = _messageUserNameStateFlow.asStateFlow()
    private val _messageTextStateFlow = MutableStateFlow("")
    val messageTextStateFlow = _messageTextStateFlow.asStateFlow()

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

                _messagesListStateFlow.value.forEach { message ->
                    _messageUrlImgStateFlow.value = message.messagePicture
                    _messageUserNameStateFlow.value = message.messageName
                    _messageTextStateFlow.value = message.messageText

                }

            } catch (e: Exception) {

            }
        }
    }
}