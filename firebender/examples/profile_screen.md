# Profile Screen

Create a user profile screen with the following requirements.

UI Elements:
- Circular profile image placeholder
- User name text
- Email address text
- Pay Now button
- Logout button

Behavior:
- Show loading indicator while profile data is loading
- Show error message if profile loading fails
- Disable Pay Now and Logout buttons while loading
- On Pay Now button click, navigate to the Payment screen
- On Logout button click, navigate back to Login screen

State:
- Loading
- Content (name, email)
- Error with message

Navigation / Redirection:
- Expose onNavigateToPayment callback for Pay Now action
- Expose onLogout callback for Logout action
- Navigation must be triggered from UI layer only
- Navigation must react to
