# AutoReplyMessenger

This project is a WIP to create an open source varient of an app that I developed and use personally

this project is made for entertainment reasons so work may be spotty

[![Build Status](https://github.com/awfixer/AutoReplyMessenger/workflows/Build%20Android%20App/badge.svg)](https://github.com/awfixer/AutoReplyMessenger/actions)
[![Code Quality](https://github.com/awfixer/AutoReplyMessenger/workflows/Kotlin%20Code%20Quality%20Check/badge.svg)](https://github.com/awfixer/AutoReplyMessenger/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

An open-source Android application that serves as a full-featured SMS, MMS, and Phone app with advanced automation for replying to incoming messages and calls.

## Features

- **SMS/MMS Handling**: Receive, send, and manage SMS and MMS messages.
- **Phone Call Handling**: Handle incoming and outgoing calls with auto-reply features.
- **Automation**: Set up custom auto-replies based on conditions.
- **Privacy-Focused**: Local-only, no cloud dependencies.
- **Material Design 3**: Modern UI with Compose.
- **MVVM Architecture**: Clean architecture with Room, Hilt, and Coroutines.
- **Code Quality**: Automated linting with ktlint and detekt.

## CI/CD Workflows

This project uses GitHub Actions for continuous integration and deployment:

### Build Workflow (`build.yml`)
- **Triggers**: Push and pull requests to `main` and `develop` branches
- **JDK**: Java 17 (Temurin distribution)
- **Tasks**:
  - Gradle cache optimization
  - Build debug APK
  - Run unit tests
  - Upload build artifacts and test results

### Code Quality Workflow (`code-check.yml`)
- **Triggers**: Push and pull requests to `main` and `develop` branches
- **JDK**: Java 17 (Temurin distribution)
- **Tasks**:
  - ktlint code style checking
  - detekt static analysis
  - Android lint
  - Unit tests
  - Upload analysis reports

## Setup Instructions

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 17
- Android SDK API 35

### Clone and Build
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/AutoReplyMessenger.git
   cd AutoReplyMessenger
   ```

2. Open the project in Android Studio

3. Build the project:
   ```bash
   ./gradlew build
   ```

4. Run tests:
   ```bash
   ./gradlew testDebugUnitTest
   ```

5. Run code quality checks:
   ```bash
   ./gradlew ktlintCheck
   ./gradlew detekt
   ./gradlew lintDebug
   ```

## Development

### Project Structure
```
app/
├── src/main/kotlin/com/awfixer/autoreplymessenger/
│   ├── data/
│   │   ├── dao/          # Room Data Access Objects
│   │   ├── model/        # Data models
│   │   ├── repository/   # Repository layer
│   │   └── AppDatabase.kt
│   ├── di/               # Dependency injection modules
│   ├── services/         # Background services and receivers
│   ├── ui/               # UI layer
│   │   ├── composables/  # Compose UI components
│   │   ├── theme/        # Material Design theme
│   │   └── viewmodels/   # ViewModels
│   └── utils/            # Utility classes
└── src/main/res/         # Android resources
```

### Architecture
- **MVVM**: Model-View-ViewModel pattern
- **Room**: Local database for data persistence
- **Hilt**: Dependency injection
- **Compose**: Modern UI toolkit
- **Coroutines & Flow**: Asynchronous programming
- **Material Design 3**: Latest design system

### Code Quality Tools
- **ktlint**: Kotlin code style checking
- **detekt**: Static code analysis
- **Android Lint**: Android-specific code analysis
- **JUnit**: Unit testing framework

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Workflow
- All code must pass CI checks (build, tests, linting)
- Follow Kotlin coding conventions
- Write unit tests for new features
- Update documentation as needed

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Material Design 3 for the design system
- Jetpack Compose for modern UI development
- Hilt for dependency injection
- Room for data persistence
- ktlint and detekt for code quality
