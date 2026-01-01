package com.study.firebenderstudy.ui.login

sealed interface LoginUiState
{
    val emailAddress: String
    val password: String
    val isPasswordVisible: Boolean
    val isLoginEnabled: Boolean

    data class Idle(
        override val emailAddress: String,
        override val password: String,
        override val isPasswordVisible: Boolean,
        override val isLoginEnabled: Boolean,
    ) : LoginUiState

    data class Loading(
        override val emailAddress: String,
        override val password: String,
        override val isPasswordVisible: Boolean,
        override val isLoginEnabled: Boolean,
    ) : LoginUiState

    data class Error(
        override val emailAddress: String,
        override val password: String,
        override val isPasswordVisible: Boolean,
        override val isLoginEnabled: Boolean,
        val message: String,
    ) : LoginUiState

    data class Success(
        val signedInEmailAddress: String,
    ) : LoginUiState
    {
        override val emailAddress: String = signedInEmailAddress
        override val password: String = ""
        override val isPasswordVisible: Boolean = false
        override val isLoginEnabled: Boolean = false
    }
}
