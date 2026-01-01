package com.study.firebenderstudy.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.study.firebenderstudy.ui.theme.FirebenderStudyTheme

@Composable
fun ProfileRoute(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(),
    onNavigateToPayment: () -> Unit = {},
    onLogout: () -> Unit = {},
)
{
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileScreen(
        uiState = uiState,
        modifier = modifier,
        onRetryClicked = viewModel::onRetryClicked,
        onNavigateToPayment = onNavigateToPayment,
        onLogout = onLogout,
    )
}

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    modifier: Modifier = Modifier,
    onRetryClicked: () -> Unit,
    onNavigateToPayment: () -> Unit,
    onLogout: () -> Unit,
)
{
    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            when (uiState)
            {
                ProfileUiState.Loading -> ProfileLoadingState(
                    modifier = Modifier.fillMaxSize(),
                )

                is ProfileUiState.Content -> ProfileContentState(
                    userFullName = uiState.userFullName,
                    emailAddress = uiState.emailAddress,
                    onNavigateToPayment = onNavigateToPayment,
                    onLogout = onLogout,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                )

                is ProfileUiState.Error -> ProfileErrorState(
                    message = uiState.message,
                    onRetryClicked = onRetryClicked,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                )
            }
        }
    }
}

@Composable
private fun ProfileLoadingState(
    modifier: Modifier = Modifier,
)
{
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.semantics { contentDescription = "Profile loading indicator" },
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Loading profile…",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun ProfileContentState(
    userFullName: String,
    emailAddress: String,
    onNavigateToPayment: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
)
{
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val imageBackgroundColor = MaterialTheme.colorScheme.surfaceVariant
            val imageIconColor = MaterialTheme.colorScheme.onSurfaceVariant

            Surface(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(imageBackgroundColor)
                    .semantics { contentDescription = "Profile image" },
                color = imageBackgroundColor,
                shape = CircleShape,
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = imageIconColor,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = userFullName,
                style = MaterialTheme.typography.headlineSmall,
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = emailAddress,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onNavigateToPayment,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Pay now button" },
        ) {
            Text(text = "Pay Now")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Logout button" },
        ) {
            Text(text = "Logout")
        }
    }
}

@Composable
private fun ProfileErrorState(
    message: String,
    onRetryClicked: () -> Unit,
    modifier: Modifier = Modifier,
)
{
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Something went wrong",
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.semantics { contentDescription = "Profile error message" },
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onRetryClicked,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Retry button" },
        ) {
            Text(text = "Retry")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileContentPreview()
{
    FirebenderStudyTheme {
        ProfileScreen(
            uiState = ProfileUiState.Content(
                userFullName = "Preeti Tundiwala",
                emailAddress = "preeti@example.com",
            ),
            onRetryClicked = {},
            onNavigateToPayment = {},
            onLogout = {},
        )
    }
}
