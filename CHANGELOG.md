<file_path>
autoreplyapp/CHANGELOG.md
</file_path>

<edit_description>
Create CHANGELOG.md
</edit_description>

# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Initial project setup with Gradle and dependencies.
- Basic Android project structure with Kotlin and Compose.
- Hilt dependency injection setup.
- Room database for storing messages and conversations.
- SMS receiving via BroadcastReceiver.
- Basic UI with Material Design 3 and Compose.
- Inbox screen displaying conversations.
- Navigation between screens (placeholder for compose, dialer, settings).

### Changed

### Deprecated

### Removed

### Fixed

### Security

## [1.0.0] - 2023-10-01

### Added
- Core SMS handling: Receive incoming SMS and store in local database.
- Basic UI: Inbox screen with list of conversations.
- Data models: Message and Conversation entities.
- Repositories: MessageRepository and ConversationRepository.
- DAOs: MessageDao and ConversationDao.
- ViewModel: InboxViewModel for managing UI state.
- Services: SmsReceiver for handling SMS broadcasts.
- Dependency injection modules: DatabaseModule and RepositoryModule.
- Application class with Hilt annotation.
- AndroidManifest with necessary permissions and receivers.
- Theme setup with Material Design 3 colors and typography.

### Iteration 1: Core SMS handling and basic UI
- Implemented SMS receiving and parsing.
- Created basic conversation threading.
- Added simple UI for displaying conversations.
- Set up MVVM architecture with Room and Hilt.
- Ensured privacy-focused, local-only storage.