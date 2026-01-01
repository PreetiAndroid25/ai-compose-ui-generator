package com.study.firebenderstudy.ui.payment

import java.math.BigDecimal

sealed interface PaymentUiState
{
    val amountInput: String
    val selectedPaymentMethod: PaymentMethod
    val isPayEnabled: Boolean

    data class Idle(
        override val amountInput: String,
        override val selectedPaymentMethod: PaymentMethod,
        override val isPayEnabled: Boolean,
        val amountInputErrorMessage: String?,
    ) : PaymentUiState

    data class Loading(
        override val amountInput: String,
        override val selectedPaymentMethod: PaymentMethod,
        override val isPayEnabled: Boolean,
    ) : PaymentUiState

    data class Success(
        val paidAmount: BigDecimal,
        val paymentMethod: PaymentMethod,
    ) : PaymentUiState
    {
        override val amountInput: String = paidAmount.toPlainString()
        override val selectedPaymentMethod: PaymentMethod = paymentMethod
        override val isPayEnabled: Boolean = false
    }

    data class Error(
        override val amountInput: String,
        override val selectedPaymentMethod: PaymentMethod,
        override val isPayEnabled: Boolean,
        val message: String,
        val amountInputErrorMessage: String?,
    ) : PaymentUiState
}

enum class PaymentMethod
{
    Card,
    Upi,
    Wallet,
}
