<file_path>
autoreplyapp/CHANGELOG.md
</file_path>

<edit_description>
Update CHANGELOG.md to document CI/CD setup
</edit_description>

# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- GitHub Actions workflows for CI/CD:
  - `build.yml`: Automated building and testing on push/PR
  - `code-check.yml`: Code quality checks with ktlint, detekt, and Android lint
  - `release.yml`: Automated release creation with APK/AAB artifacts
  - `dependency-updates.yml`: Weekly dependency update checks
- Code quality tools integration:
  - ktlint for Kotlin code style checking
  - detekt for static code analysis
  - Android lint for platform-specific checks
- Configuration files:
  - `.editorconfig` for consistent code formatting
  - `config/detekt/detekt.yml` for detekt rules
- Updated `gradle.properties` with performance optimizations
- Enhanced `.gitignore` with comprehensive Android project exclusions
- Updated `README.md` with CI/CD documentation and badges

### Changed
- Improved build configuration for better CI/CD performance
- Updated dependency management with proper BOM usage

### Fixed
- Resolved all unresolved errors in the codebase
- Fixed package naming inconsistencies
- Corrected Flow handling in repositories
- Fixed icon imports and other minor issues

## [1.0.0] - 2023-10-01

### Added
- Core SMS handling: Receive incoming SMS and store in local database
- Basic UI: Inbox screen with list of conversations
- Data models: Message and Conversation entities
- Repositories: MessageRepository and ConversationRepository
- DAOs: MessageDao and ConversationDao
- ViewModel: InboxViewModel for managing UI state
- Services: SmsReceiver for handling SMS broadcasts
- Dependency injection modules: DatabaseModule and RepositoryModule
- Application class with Hilt annotation
- AndroidManifest with necessary permissions and receivers
- Theme setup with Material Design 3 colors and typography

### Iteration 1: Core SMS handling and basic UI
- Implemented SMS receiving and parsing
- Created basic conversation threading
- Added simple UI for displaying conversations
- Set up MVVM architecture with Room and Hilt
- Ensured privacy-focused, local-only storage
