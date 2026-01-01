package com.study.firebenderstudy.ui.profile

sealed interface ProfileUiState
{
    data object Loading : ProfileUiState

    data class Content(
        val userFullName: String,
        val emailAddress: String,
    ) : ProfileUiState

    data class Error(
        val message: String,
    ) : ProfileUiState
}
