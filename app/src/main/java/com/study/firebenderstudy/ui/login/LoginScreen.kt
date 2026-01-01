package com.study.firebenderstudy.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.study.firebenderstudy.ui.theme.FirebenderStudyTheme

@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: (emailAddress: String) -> Unit = {},
)
{
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val successState = uiState as? LoginUiState.Success
    if (successState != null)
    {
        LaunchedEffect(successState.signedInEmailAddress) {
            onLoginSuccess(successState.signedInEmailAddress)
        }
    }

    LoginScreen(
        uiState = uiState,
        modifier = modifier,
        onEmailAddressChanged = viewModel::onEmailAddressChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onPasswordVisibilityToggled = viewModel::onPasswordVisibilityToggled,
        onLoginClicked = viewModel::onLoginClicked,
        onErrorDismissed = viewModel::onErrorDismissed,
    )
}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    modifier: Modifier = Modifier,
    onEmailAddressChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onPasswordVisibilityToggled: () -> Unit,
    onLoginClicked: () -> Unit,
    onErrorDismissed: () -> Unit,
)
{
    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        LoginContent(
            uiState = uiState,
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp),
            onEmailAddressChanged = onEmailAddressChanged,
            onPasswordChanged = onPasswordChanged,
            onPasswordVisibilityToggled = onPasswordVisibilityToggled,
            onLoginClicked = onLoginClicked,
            onErrorDismissed = onErrorDismissed,
        )
    }
}

@Composable
private fun LoginContent(
    uiState: LoginUiState,
    modifier: Modifier = Modifier,
    onEmailAddressChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onPasswordVisibilityToggled: () -> Unit,
    onLoginClicked: () -> Unit,
    onErrorDismissed: () -> Unit,
)
{
    val focusManager = LocalFocusManager.current
    val passwordFocusRequester = remember { FocusRequester() }

    val isLoading = uiState is LoginUiState.Loading
    val errorMessage = (uiState as? LoginUiState.Error)?.message

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = uiState.emailAddress,
            onValueChange = onEmailAddressChanged,
            enabled = !isLoading,
            singleLine = true,
            label = { Text(text = "Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = { passwordFocusRequester.requestFocus() },
            ),
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Email input" },
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.password,
            onValueChange = onPasswordChanged,
            enabled = !isLoading,
            singleLine = true,
            label = { Text(text = "Password") },
            visualTransformation = if (uiState.isPasswordVisible)
            {
                VisualTransformation.None
            } else
            {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                val icon =
                    if (uiState.isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                val iconContentDescription = if (uiState.isPasswordVisible)
                {
                    "Hide password"
                } else
                {
                    "Show password"
                }

                IconButton(
                    onClick = onPasswordVisibilityToggled,
                    enabled = !isLoading,
                    modifier = Modifier.semantics { contentDescription = iconContentDescription },
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onLoginClicked()
                },
            ),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passwordFocusRequester)
                .semantics { contentDescription = "Password input" },
        )

        if (errorMessage != null)
        {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Login error message" },
            )

            Spacer(modifier = Modifier.height(8.dp))

            ClickableText(
                text = AnnotatedString("Dismiss"),
                onClick = { onErrorDismissed() },
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier
                    .align(Alignment.Start)
                    .semantics { contentDescription = "Dismiss error" },
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                focusManager.clearFocus()
                onLoginClicked()
            },
            enabled = uiState.isLoginEnabled && !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Login button" },
        ) {
            if (isLoading)
            {
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(18.dp)
                        .semantics { contentDescription = "Login loading indicator" },
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                Spacer(modifier = Modifier.height(0.dp))
            } else
            {
                Text(text = "Login")
            }
        }

        if (uiState is LoginUiState.Success)
        {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Signed in as ${uiState.signedInEmailAddress}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.semantics { contentDescription = "Login success message" },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview()
{
    FirebenderStudyTheme {
        LoginScreen(
            uiState = LoginUiState.Idle(
                emailAddress = "hello@example.com",
                password = "password",
                isPasswordVisible = false,
                isLoginEnabled = true,
            ),
            onEmailAddressChanged = {},
            onPasswordChanged = {},
            onPasswordVisibilityToggled = {},
            onLoginClicked = {},
            onErrorDismissed = {},
        )
    }
}
