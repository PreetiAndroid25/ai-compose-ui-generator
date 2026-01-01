package com.study.firebenderstudy.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel()
{

    private val mutableUiState: MutableStateFlow<ProfileUiState> =
        MutableStateFlow(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = mutableUiState.asStateFlow()

    init
    {
        loadProfile()
    }

    fun onRetryClicked()
    {
        loadProfile()
    }

    private fun loadProfile()
    {
        viewModelScope.launch {
            mutableUiState.value = ProfileUiState.Loading
            delay(900)

            val mockFailure = false
            if (mockFailure)
            {
                mutableUiState.value = ProfileUiState.Error(
                    message = "Unable to load profile. Please try again.",
                )
                return@launch
            }

            mutableUiState.value = ProfileUiState.Content(
                userFullName = "Preeti Tundiwala",
                emailAddress = "preeti@example.com",
            )
        }
    }
}
