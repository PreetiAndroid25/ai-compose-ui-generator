# Payment Screen

Create a payment screen with the following requirements.

UI Elements:
- Amount input field
    - Numeric keyboard
    - Required field
- Payment method selector
    - Options: Card, UPI, Wallet
- Primary action button
    - Label: Pay Now

Behavior:
- Disable Pay button when amount is empty or invalid
- Show loading indicator while payment is processing
- Show error message if payment fails
- On successful payment, navigate to the next screen (e.g. Profile or Success screen)

State:
- Idle
- Loading
- Success
- Error with message

Accessibility:
- Content descriptions for all interactive elements
- Proper keyboard actions for amount input

Theme:
- Material 3
- Supports light and dark mode

Architecture:
- Use MVVM
- UI must be stateless
- Use sealed UiState
- State managed by ViewModel
- Navigation must be triggered from UI based on Success state
