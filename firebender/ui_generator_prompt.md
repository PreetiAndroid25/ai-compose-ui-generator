# Generic UI → Compose + Navigation Generator

Generate a Jetpack Compose screen using the description below.

## Screen Description
{{SCREEN_DESCRIPTION}}

## Responsibilities
- Identify all UI components from the description
- Create a stateless composable screen
- Create a ViewModel to manage state and events
- Create a sealed UiState representing all screen states
- Use StateFlow for state exposure
- Apply state hoisting correctly

## UI State Handling
- Include Loading, Error, and Success states when applicable
- UI must react to UiState changes only
- No business logic inside composables

## Navigation / Redirection
- If the screen describes a successful action (login, submit, save, complete):
    - Expose a callback such as onSuccess or onNavigateNext
    - Trigger the callback using LaunchedEffect when UiState enters Success
- If the screen describes logout or exit:
    - Expose an onLogout or onNavigateBack callback
- Do not perform navigation inside ViewModel
- Navigation must be controlled by the calling composable or NavHost

## Accessibility
- Add content descriptions to interactive elements
- Support keyboard actions and IME options where relevant

## Theme
- Use Material 3
- Ensure automatic light and dark mode support

## Architecture
- MVVM
- Unidirectional data flow
- UI depends only on UiState and callbacks

## Output Files
- <ScreenName>Screen.kt
- <ScreenName>ViewModel.kt
- <ScreenName>UiState.kt

## Optional Enhancements (when applicable)
- Show loading indicators during async work
- Disable actions during loading
- Display error messages in UI
