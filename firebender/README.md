# Firebender – AI UI Automation for Android

This folder contains all AI prompt configurations used to automatically generate
Jetpack Compose UI screens from natural language descriptions.

## 📌 Purpose
The goal is to demonstrate how AI (via Firebender) can:
- Convert plain English screen descriptions into Jetpack Compose UI
- Automatically generate ViewModel and UiState
- Follow MVVM architecture
- Ensure accessibility and dark mode support

## 📂 Folder Structure

firebender/
├── system_prompt.md              # Defines AI behavior and coding rules
├── ui_generator_prompt.md        # Generic UI → Compose generation prompt
├── examples/                     # Sample screen descriptions
│   ├── login_screen.md
│   ├── payment_screen.md
│   └── profile_screen.md

## 🧠 How It Works

1. `system_prompt.md` defines **who the AI is**
2. `ui_generator_prompt.md` defines **what the AI should generate**
3. Any file inside `examples/` provides **screen-specific requirements**
4. Firebender combines all inputs and generates Kotlin files automatically

## ✅ Output Generated

For each screen description, Firebender generates:
- `<ScreenName>Screen.kt`
- `<ScreenName>ViewModel.kt`
- `<ScreenName>UiState.kt`

## 🚀 Usage

To generate a new screen:
1. Add a new `.md` file inside `examples/`
2. Describe the screen in plain English
3. Run Firebender
4. Review generated Compose code

This setup is tool-agnostic and works for demos, interviews, and real projects.