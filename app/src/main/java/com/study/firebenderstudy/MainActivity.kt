package com.study.firebenderstudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.study.firebenderstudy.ui.login.LoginRoute
import com.study.firebenderstudy.ui.payment.PaymentRoute
import com.study.firebenderstudy.ui.profile.ProfileRoute
import com.study.firebenderstudy.ui.theme.FirebenderStudyTheme

class MainActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirebenderStudyTheme {
                var destination: DemoDestination by rememberSaveable {
                    mutableStateOf(DemoDestination.Login)
                }

                when (destination)
                {
                    DemoDestination.Login ->
                    {
                        LoginRoute(
                            onLoginSuccess = {
                                destination = DemoDestination.Profile
                            },
                        )
                    }

                    DemoDestination.Profile ->
                    {
                        ProfileRoute(
                            onNavigateToPayment = {
                                destination = DemoDestination.Payment
                            },
                            onLogout = {
                                destination = DemoDestination.Login
                            },
                        )
                    }

                    DemoDestination.Payment ->
                    {
                        PaymentRoute(
                            onPaymentSuccess = {
                                destination = DemoDestination.Profile
                            },
                        )
                    }
                }
            }
        }
    }
}

private enum class DemoDestination
{
    Login,
    Profile,
    Payment,
}

@Preview(showBackground = true)
@Composable
private fun LoginPreview()
{
    FirebenderStudyTheme {
        LoginRoute()
    }
}