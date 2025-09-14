# GitHub Actions Workflows

This directory contains GitHub Actions workflows for continuous integration and deployment of the AutoReplyMessenger Android application.

## Workflows Overview

### 1. Build Workflow (`build.yml`)

**Purpose**: Automates the building and testing of the Android application.

**Triggers**:
- Push to `main` or `develop` branches
- Pull requests to `main` or `develop` branches

**Steps**:
1. Sets up JDK 17 (Temurin distribution)
2. Caches Gradle packages for faster builds
3. Builds the debug APK
4. Runs unit tests
5. Uploads build artifacts (APK) and test results

**Artifacts**:
- `app-debug.apk`: Debug version of the application
- Test reports and results

### 2. Code Quality Check (`code-check.yml`)

**Purpose**: Ensures code quality through automated linting and static analysis.

**Triggers**:
- Push to `main` or `develop` branches
- Pull requests to `main` or `develop` branches

**Steps**:
1. Sets up JDK 17 (Temurin distribution)
2. Caches Gradle packages
3. Runs ktlint for Kotlin code style checking
4. Runs detekt for static code analysis
5. Runs Android lint for platform-specific checks
6. Executes unit tests
7. Uploads analysis reports

**Reports Generated**:
- ktlint results
- detekt HTML/XML reports
- Android lint results
- Test results

### 3. Release Workflow (`release.yml`)

**Purpose**: Automates the creation of GitHub releases with built artifacts.

**Triggers**:
- Push of version tags (e.g., `v1.0.0`)
- Manual trigger via GitHub Actions UI

**Steps**:
1. Sets up JDK 17
2. Builds release APK and AAB (Android App Bundle)
3. Creates release archives
4. Generates basic release notes
5. Creates a GitHub release
6. Uploads APK and AAB as release assets

**Outputs**:
- GitHub release with version tag
- Downloadable APK and AAB files
- Release notes

### 4. Dependency Updates (`dependency-updates.yml`)

**Purpose**: Monitors and reports available dependency updates.

**Triggers**:
- Scheduled weekly (Mondays at 9 AM UTC)
- Manual trigger via GitHub Actions UI

**Steps**:
1. Sets up JDK 17
2. Runs Gradle dependency updates check
3. Generates dependency report
4. Uploads report as artifact
5. Comments on PRs with available updates (if triggered by PR)

**Reports Generated**:
- JSON report of outdated dependencies
- Artifact containing update information

## Prerequisites

### Repository Secrets
No additional secrets are required for these workflows. They use the default `GITHUB_TOKEN` for repository access.

### Branch Protection
Consider setting up branch protection rules for `main` and `develop` branches to require:
- Status checks to pass
- Pull request reviews
- Up-to-date branches

### Required Permissions
The workflows require the following permissions (automatically granted for public repositories):
- `contents: read` - For accessing repository contents
- `pull-requests: write` - For commenting on PRs (dependency updates workflow)

## Configuration Files

The workflows rely on several configuration files in the repository:

- `.editorconfig` - Code formatting standards
- `config/detekt/detekt.yml` - Detekt static analysis rules
- `gradle.properties` - Gradle build properties
- `build.gradle` files - Project dependencies and build configuration

## Usage

### Running Workflows Manually
1. Go to the "Actions" tab in your GitHub repository
2. Select the desired workflow
3. Click "Run workflow"
4. Fill in any required parameters (e.g., version for release workflow)

### Monitoring Workflow Runs
- Check the "Actions" tab for workflow status
- View detailed logs for each step
- Download artifacts from successful runs
- Review reports for code quality issues

### Troubleshooting
- **Build Failures**: Check Gradle logs for dependency or compilation errors
- **Test Failures**: Review test reports for failing test details
- **Lint Failures**: Examine ktlint/detekt reports for code style issues
- **Release Issues**: Ensure proper tagging format (e.g., `v1.0.0`)

## Customization

### Modifying Triggers
Edit the `on:` section in each workflow file to change when workflows run.

### Adding New Checks
Extend existing workflows or create new ones by adding steps to the `steps:` section.

### Environment Variables
Add environment variables using the `env:` section in workflow steps.

## Support

For issues with these workflows:
1. Check GitHub Actions documentation
2. Review workflow logs for error messages
3. Ensure all required files and configurations are present
4. Verify repository permissions and secrets

## Contributing

When modifying workflows:
- Test changes on a feature branch first
- Update this documentation if adding new workflows
- Follow GitHub Actions best practices
- Ensure workflows work with both push and pull request events