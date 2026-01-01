package com.study.firebenderstudy.ui.payment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.study.firebenderstudy.ui.theme.FirebenderStudyTheme

@Composable
fun PaymentRoute(
    modifier: Modifier = Modifier,
    viewModel: PaymentViewModel = viewModel(),
    onPaymentSuccess: () -> Unit,
)
{
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val successState = uiState as? PaymentUiState.Success
    if (successState != null)
    {
        LaunchedEffect(successState.paidAmount, successState.paymentMethod) {
            onPaymentSuccess()
        }
    }

    PaymentScreen(
        uiState = uiState,
        modifier = modifier,
        onAmountInputChanged = viewModel::onAmountInputChanged,
        onPaymentMethodSelected = viewModel::onPaymentMethodSelected,
        onPayNowClicked = viewModel::onPayNowClicked,
        onErrorDismissed = viewModel::onErrorDismissed,
    )
}

@Composable
fun PaymentScreen(
    uiState: PaymentUiState,
    modifier: Modifier = Modifier,
    onAmountInputChanged: (String) -> Unit,
    onPaymentMethodSelected: (PaymentMethod) -> Unit,
    onPayNowClicked: () -> Unit,
    onErrorDismissed: () -> Unit,
)
{
    val focusManager = LocalFocusManager.current
    val isLoading = uiState is PaymentUiState.Loading

    val amountInputErrorMessage: String? = when (uiState)
    {
        is PaymentUiState.Idle -> uiState.amountInputErrorMessage
        is PaymentUiState.Error -> uiState.amountInputErrorMessage
        is PaymentUiState.Loading -> null
        is PaymentUiState.Success -> null
    }

    val errorMessage: String? = (uiState as? PaymentUiState.Error)?.message

    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Payment",
                    style = MaterialTheme.typography.headlineMedium,
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = uiState.amountInput,
                    onValueChange = onAmountInputChanged,
                    enabled = !isLoading,
                    singleLine = true,
                    label = { Text(text = "Amount") },
                    isError = amountInputErrorMessage != null,
                    supportingText = {
                        if (amountInputErrorMessage != null)
                        {
                            Text(text = amountInputErrorMessage)
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            onPayNowClicked()
                        },
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { contentDescription = "Amount input" },
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Payment method",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .semantics { contentDescription = "Payment method selector" },
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectableGroup(),
                ) {
                    PaymentMethod.values().forEach { paymentMethod ->
                        val label = when (paymentMethod)
                        {
                            PaymentMethod.Card -> "Card"
                            PaymentMethod.Upi -> "UPI"
                            PaymentMethod.Wallet -> "Wallet"
                        }

                        val isSelected = uiState.selectedPaymentMethod == paymentMethod

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = isSelected,
                                    enabled = !isLoading,
                                    onClick = { onPaymentMethodSelected(paymentMethod) },
                                )
                                .padding(vertical = 6.dp)
                                .semantics { contentDescription = "Payment method $label" },
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = null,
                                enabled = !isLoading,
                            )
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 12.dp),
                            )
                        }
                    }
                }

                if (errorMessage != null)
                {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentDescription = "Payment error message" },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onErrorDismissed,
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentDescription = "Dismiss payment error" },
                    ) {
                        Text(text = "Dismiss")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        onPayNowClicked()
                    },
                    enabled = uiState.isPayEnabled && !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { contentDescription = "Pay now button" },
                ) {
                    if (isLoading)
                    {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .height(18.dp)
                                .semantics { contentDescription = "Payment loading indicator" },
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    } else
                    {
                        Text(text = "Pay Now")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PaymentPreview()
{
    FirebenderStudyTheme {
        PaymentScreen(
            uiState = PaymentUiState.Idle(
                amountInput = "250",
                selectedPaymentMethod = PaymentMethod.Upi,
                isPayEnabled = true,
                amountInputErrorMessage = null,
            ),
            onAmountInputChanged = {},
            onPaymentMethodSelected = {},
            onPayNowClicked = {},
            onErrorDismissed = {},
        )
    }
}
