package com.study.firebenderstudy.ui.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.math.BigDecimal
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PaymentViewModel : ViewModel() {

    private val mutableUiState: MutableStateFlow<PaymentUiState> = MutableStateFlow(
        PaymentUiState.Idle(
            amountInput = "",
            selectedPaymentMethod = PaymentMethod.Card,
            isPayEnabled = false,
            amountInputErrorMessage = null,
        ),
    )

    val uiState: StateFlow<PaymentUiState> = mutableUiState.asStateFlow()

    fun onAmountInputChanged(amountInput: String) {
        val sanitizedInput = amountInput
            .replace(',', '.')
            .filter { character -> character.isDigit() || character == '.' }

        mutableUiState.update { currentState ->
            currentState.copyFormValues(amountInput = sanitizedInput)
        }
    }

    fun onPaymentMethodSelected(paymentMethod: PaymentMethod) {
        mutableUiState.update { currentState ->
            currentState.copyFormValues(selectedPaymentMethod = paymentMethod)
        }
    }

    fun onPayNowClicked() {
        val currentState = mutableUiState.value
        if (currentState is PaymentUiState.Loading) {
            return
        }

        val parsedAmount = parseAmount(currentState.amountInput)
        val amountErrorMessage = validateAmount(parsedAmount)
        if (amountErrorMessage != null) {
            mutableUiState.update {
                it.asError(
                    message = amountErrorMessage,
                    amountInputErrorMessage = amountErrorMessage,
                )
            }
            return
        }

        val amountToPay = parsedAmount ?: return

        mutableUiState.update {
            PaymentUiState.Loading(
                amountInput = it.amountInput,
                selectedPaymentMethod = it.selectedPaymentMethod,
                isPayEnabled = false,
            )
        }

        viewModelScope.launch {
            delay(1_000)

            val shouldFail =
                amountToPay == BigDecimal("13") ||
                    (currentState.selectedPaymentMethod == PaymentMethod.Wallet && amountToPay > BigDecimal("1000"))

            if (shouldFail) {
                mutableUiState.update {
                    it.asError(
                        message = "Payment failed. Please try a different amount or method.",
                        amountInputErrorMessage = null,
                    )
                }
                return@launch
            }

            mutableUiState.value = PaymentUiState.Success(
                paidAmount = amountToPay,
                paymentMethod = currentState.selectedPaymentMethod,
            )
        }
    }

    fun onErrorDismissed() {
        mutableUiState.update { currentState ->
            PaymentUiState.Idle(
                amountInput = currentState.amountInput,
                selectedPaymentMethod = currentState.selectedPaymentMethod,
                isPayEnabled = currentState.isPayEnabled,
                amountInputErrorMessage = null,
            )
        }
    }

    private fun PaymentUiState.copyFormValues(
        amountInput: String = this.amountInput,
        selectedPaymentMethod: PaymentMethod = this.selectedPaymentMethod,
    ): PaymentUiState {
        val parsedAmount = parseAmount(amountInput)
        val amountErrorMessage = validateAmount(parsedAmount)

        val isPayEnabled = amountErrorMessage == null
        return when (this) {
            is PaymentUiState.Idle -> copy(
                amountInput = amountInput,
                selectedPaymentMethod = selectedPaymentMethod,
                isPayEnabled = isPayEnabled,
                amountInputErrorMessage = amountErrorMessage,
            )

            is PaymentUiState.Loading -> copy(
                amountInput = amountInput,
                selectedPaymentMethod = selectedPaymentMethod,
                isPayEnabled = false,
            )

            is PaymentUiState.Error -> copy(
                amountInput = amountInput,
                selectedPaymentMethod = selectedPaymentMethod,
                isPayEnabled = isPayEnabled,
                message = message,
                amountInputErrorMessage = amountInputErrorMessage,
            )

            is PaymentUiState.Success -> PaymentUiState.Idle(
                amountInput = amountInput,
                selectedPaymentMethod = selectedPaymentMethod,
                isPayEnabled = isPayEnabled,
                amountInputErrorMessage = amountErrorMessage,
            )
        }
    }

    private fun PaymentUiState.asError(
        message: String,
        amountInputErrorMessage: String?,
    ): PaymentUiState.Error {
        val parsedAmount = parseAmount(amountInput)
        val validationErrorMessage = validateAmount(parsedAmount)

        return PaymentUiState.Error(
            amountInput = amountInput,
            selectedPaymentMethod = selectedPaymentMethod,
            isPayEnabled = validationErrorMessage == null,
            message = message,
            amountInputErrorMessage = amountInputErrorMessage ?: validationErrorMessage,
        )
    }
}

private fun parseAmount(amountInput: String): BigDecimal? {
    if (amountInput.isBlank()) {
        return null
    }

    return try {
        BigDecimal(amountInput)
    } catch (_: NumberFormatException) {
        null
    }
}

private fun validateAmount(amount: BigDecimal?): String? {
    if (amount == null) {
        return "Amount is required."
    }

    if (amount <= BigDecimal.ZERO) {
        return "Amount must be greater than 0."
    }

    if (amount.scale() > 2) {
        return "Amount can have at most 2 decimal places."
    }

    return null
}
