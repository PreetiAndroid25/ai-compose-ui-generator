package com.study.firebenderstudy.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel()
{

    private val mutableUiState: MutableStateFlow<LoginUiState> = MutableStateFlow(
        LoginUiState.Idle(
            emailAddress = "",
            password = "",
            isPasswordVisible = false,
            isLoginEnabled = false,
        ),
    )

    val uiState: StateFlow<LoginUiState> = mutableUiState.asStateFlow()

    fun onEmailAddressChanged(emailAddress: String)
    {
        mutableUiState.update { currentState ->
            val updatedEmailAddress = emailAddress
            currentState.copyFormValues(
                emailAddress = updatedEmailAddress,
                password = currentState.password,
                isPasswordVisible = currentState.isPasswordVisible,
            )
        }
    }

    fun onPasswordChanged(password: String)
    {
        mutableUiState.update { currentState ->
            val updatedPassword = password
            currentState.copyFormValues(
                emailAddress = currentState.emailAddress,
                password = updatedPassword,
                isPasswordVisible = currentState.isPasswordVisible,
            )
        }
    }

    fun onPasswordVisibilityToggled()
    {
        mutableUiState.update { currentState ->
            currentState.copyFormValues(
                emailAddress = currentState.emailAddress,
                password = currentState.password,
                isPasswordVisible = !currentState.isPasswordVisible,
            )
        }
    }

    fun onLoginClicked()
    {
        val currentState = mutableUiState.value
        if (currentState is LoginUiState.Loading)
        {
            return
        }

        val emailAddress = currentState.emailAddress.trim()
        val password = currentState.password

        val emailLooksValid = emailAddress.contains('@') && emailAddress.contains('.')
        val passwordLooksValid = password.length >= 6

        if (!emailLooksValid)
        {
            mutableUiState.update {
                it.asError(message = "Please enter a valid email address.")
            }
            return
        }

        if (!passwordLooksValid)
        {
            mutableUiState.update {
                it.asError(message = "Password must be at least 6 characters.")
            }
            return
        }

        mutableUiState.update {
            LoginUiState.Loading(
                emailAddress = it.emailAddress,
                password = it.password,
                isPasswordVisible = it.isPasswordVisible,
                isLoginEnabled = it.isLoginEnabled,
            )
        }

        viewModelScope.launch {
            val result = performLogin(
                emailAddress = emailAddress,
                password = password,
            )

            mutableUiState.update {
                when (result)
                {
                    LoginResult.Success -> LoginUiState.Success(signedInEmailAddress = emailAddress)
                    is LoginResult.Failure -> it.asError(message = result.message)
                }
            }
        }
    }

    fun onErrorDismissed()
    {
        mutableUiState.update { currentState ->
            LoginUiState.Idle(
                emailAddress = currentState.emailAddress,
                password = currentState.password,
                isPasswordVisible = currentState.isPasswordVisible,
                isLoginEnabled = currentState.isLoginEnabled,
            )
        }
    }

    private fun LoginUiState.copyFormValues(
        emailAddress: String,
        password: String,
        isPasswordVisible: Boolean,
    ): LoginUiState
    {
        val isLoginEnabled = emailAddress.trim().isNotEmpty() && password.isNotEmpty()

        return when (this)
        {
            is LoginUiState.Idle -> copy(
                emailAddress = emailAddress,
                password = password,
                isPasswordVisible = isPasswordVisible,
                isLoginEnabled = isLoginEnabled,
            )

            is LoginUiState.Loading -> copy(
                emailAddress = emailAddress,
                password = password,
                isPasswordVisible = isPasswordVisible,
                isLoginEnabled = isLoginEnabled,
            )

            is LoginUiState.Error -> copy(
                emailAddress = emailAddress,
                password = password,
                isPasswordVisible = isPasswordVisible,
                isLoginEnabled = isLoginEnabled,
                message = message,
            )

            is LoginUiState.Success -> LoginUiState.Idle(
                emailAddress = emailAddress,
                password = password,
                isPasswordVisible = isPasswordVisible,
                isLoginEnabled = isLoginEnabled,
            )
        }
    }

    private fun LoginUiState.asError(message: String): LoginUiState.Error
    {
        return LoginUiState.Error(
            emailAddress = emailAddress,
            password = password,
            isPasswordVisible = isPasswordVisible,
            isLoginEnabled = isLoginEnabled,
            message = message,
        )
    }

    private suspend fun performLogin(
        emailAddress: String,
        password: String,
    ): LoginResult
    {
        delay(1_000)

        val forceFailure = emailAddress.endsWith("@fail.com")
        return if (forceFailure)
        {
            LoginResult.Failure(message = "Login failed. Please check your credentials and try again.")
        } else
        {
            LoginResult.Success
        }
    }
}

private sealed interface LoginResult
{
    data object Success : LoginResult
    data class Failure(val message: String) : LoginResult
}
