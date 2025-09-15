# Welcome to the AutoReplyMessenger Repo!

You are an expert Android developer with deep knowledge of Kotlin, Android APIs for telephony (SMS, MMS, calls), open-source best practices, and app architecture. Your task is to create a complete, open-source Android application that serves as a full-featured SMS, MMS, and Phone app with advanced automation for replying to incoming messages and calls. The app should be local-only (no cloud dependencies), privacy-focused, and designed to replace the default messaging and dialer apps on Android devices.

### Project Overview
- **App Name**: AutoReplyMessenger
- **Target SDK**: Android 15 (API 35) minimum, with backward compatibility to Android 10 (API 29).
- **Language**: Kotlin exclusively.
- **Architecture**: Use MVVM (Model-View-ViewModel) pattern with Jetpack components: Room for local database (to store conversations, settings, and logs), LiveData/Flow for reactive UI, Coroutines for async operations, Navigation Component for UI flows, and Dagger/Hilt for dependency injection.
- **Open Source**: Structure the project for GitHub hosting. Include a MIT license, README.md with setup instructions, screenshots, contribution guidelines, and a CHANGELOG.md. Use Git for version control (assume initial commit).
- **Core Functionality**: The app must handle SMS, MMS, and phone calls as the default handler. It automates replies to incoming SMS/MMS/calls with customizable messages, e.g., "I'm using [alternative platform like Signal or a new number] instead—please contact me there at [custom number/platform]."
- **User Intent**: This app helps users migrate away from traditional SMS/calls by auto-responding and directing contacts to alternatives, while still providing full messaging/dialer features.
- **Development Approach**: Build the app iteratively. Start with Iteration 1: Core SMS handling and basic UI. Iteration 2: Add MMS and phone call features. Iteration 3: Implement automation rules and advanced UI. Iteration 4: Focus on security enhancements, testing, and polish. Document each iteration in CHANGELOG.md and ensure modular code for easy extension.

### Key Features
1. **SMS/MMS Handling**:
   - Act as default SMS/MMS app: Request ROLE_SMS via RoleManager.
   - Receive and parse incoming SMS (using BroadcastReceiver with Telephony.Sms.Intents.SMS_RECEIVED_ACTION).
   - Receive and parse MMS (using WAP_PUSH_RECEIVED_ACTION, parse PDUs with PduParser, extract text/media via ContentResolver querying content://mms/part).
   - Send SMS/MMS: Use SmsManager for SMS; for MMS, handle MMSC/APN fetching from Telephony.Carriers.CONTENT_URI and use HttpURLConnection for sending (implement fallback to SMS if no media).
   - Conversation Threading: Query content://mms-sms/conversations to display threaded views with search, sorting (by date/contact), and media previews.
   - Auto-Reply: On incoming SMS/MMS, check conditions (e.g., always, during do-not-disturb hours, specific contacts) and send customizable reply. Store replies in app settings (SharedPreferences or Room).

2. **Phone Call Handling**:
   - Act as default dialer: Request ROLE_DIALER via RoleManager.
   - Detect incoming calls (BroadcastReceiver with TelephonyManager.ACTION_PHONE_STATE_CHANGED).
   - Auto-Reply for Calls: On ringing, optionally decline (using TelecomManager.endCall() with ANSWER_PHONE_CALLS permission) and send SMS reply to the caller (e.g., "Switching to [alternative]—call me at [new number]").
   - Full Dialer UI: Include dial pad, call logs (read CALL_LOG), contacts integration (read CONTACTS), and in-call screen (extend InCallService for UI during calls).
   - Call Screening: Implement CallScreeningService to filter/block calls before ringing, based on user rules.

3. **Automation and Customization**:
   - Settings Screen: Toggle auto-reply on/off, set custom reply templates (support placeholders like {alternative_platform}, {new_number}), define rules (whitelist/blacklist contacts, time schedules using AlarmManager, keyword triggers).
   - Conditions for Auto-Reply: Integrate with device states (e.g., Do Not Disturb via NotificationManager, battery level via BatteryManager).
   - Logging: Maintain a local log of auto-replies (stored in Room DB) for user review.
   - Fallbacks: If auto-reply fails (e.g., no signal), queue and retry using WorkManager.

4. **UI/UX**:
   - Material Design 3 Expressive: Use Compose for all UI (no XML layouts) for modern, responsive design. Emphasize expressive elements like motion transitions (e.g., shared element animations between screens), dynamic typography scaling, vibrant color schemes with high contrast ratios, and adaptive layouts for foldables/tablets. Incorporate expressive feedback like ripple effects, elevation changes on interactions, and animated icons for states (e.g., sending/received).
   - Main Screens:
     - Inbox: List of conversations with unread badges, search bar.
     - Compose: Message editor with MMS attachments (images/videos from gallery/camera).
     - Dialer: Tabbed interface for dial pad, recent calls, contacts.
     - Settings: Categories for auto-reply rules, themes (light/dark), backups (export conversations to JSON/CSV).
   - Accessibility: Support TalkBack, large text, color contrasts.
   - Themes: Dynamic color theming based on wallpaper (Material You), with expressive custom palettes for branding.

5. **Permissions and Security**:
   - Runtime Permissions: REQUEST_SMS, SEND_SMS, READ_SMS, RECEIVE_SMS, READ_PHONE_STATE, CALL_PHONE, ANSWER_PHONE_CALLS, READ_CALL_LOG, PROCESS_OUTGOING_CALLS, READ_CONTACTS.
   - Handle Denials: Graceful fallbacks (e.g., disable features if not default app).
   - Privacy: No data collection; all local. Encrypt sensitive data (e.g., custom numbers, logs, and any stored messages) with EncryptedSharedPreferences or Jetpack Security library (e.g., EncryptedFile for attachments). Use secure random for any keys.
   - Security Emphasis: Follow OWASP Mobile Top 10 practices—validate all inputs (e.g., sanitize sender numbers to prevent injection), use HTTPS for any internal HTTP (e.g., MMS sending), protect against reverse engineering (e.g., ProGuard/R8 obfuscation in build.gradle), secure broadcast receivers (set exported=false where possible, use LocalBroadcastManager), audit for vulnerabilities like insecure data storage or improper permission handling. In Iteration 4, add security audits: Integrate static analysis (e.g., via lint rules) and runtime checks (e.g., SafetyNet or Play Integrity API if applicable, but keep local-only). Encrypt Room DB with SQLCipher. Ensure no hardcoded secrets; use buildConfig for any constants.

6. **Advanced Features**:
   - Integration: Optional RCS support if available (check TelephonyManager.isRcsSupported); fallback to SMS.
   - Group Messaging: Handle group MMS/SMS threads.
   - Media Handling: Preview/download MMS attachments; compress images before sending.
   - Export/Import: Backup settings and logs to external storage.
   - Testing Utils: Include unit tests (JUnit), UI tests (Espresso/Compose UI Testing), and debug modes.

7. **Edge Cases and Robustness**:
   - Handle: No SIM, airplane mode, international numbers, MMS with large attachments, call forwarding.
   - Error Handling: Toast notifications for failures; log exceptions with Timber or similar.
   - Performance: Optimize queries with paging (Paging 3 library for conversation lists).
   - Compatibility: Test on emulators (API 29-35) and physical devices; handle Samsung/Huawei quirks if possible.

### Development Guidelines
- **Dependencies**: Add via build.gradle:
  - Jetpack: androidx.core, androidx.appcompat, androidx.lifecycle, androidx.room, androidx.navigation, androidx.work, com.google.dagger:hilt-android, androidx.compose.material3, etc.
  - Telephony: No extras needed; use built-in android.telephony.
  - Security: androidx.security:security-crypto, com.squareup.sqldelight:android-driver for SQLCipher if encrypting DB.
  - Avoid third-party libs unless essential (e.g., Glide for image loading if Compose doesn't suffice; Timber for logging).
- **Project Structure**:
  - app/src/main/kotlin/com/example/autoreplymessenger/
    - data: Repos, DAOs (Room).
    - ui: Composables, ViewModels.
    - services: Receivers, Services (e.g., for background replies).
    - utils: Helpers for PDU parsing, APN fetching.
- **Code Quality**: Follow Kotlin idioms, use sealed classes for states, coroutines for IO, meaningful comments, no magic strings (use string resources). In each iteration, refactor for security (e.g., avoid direct string concatenation in queries).
- **Build and Deploy**: Generate signed APK; include instructions for sideloading or Play Store upload (note SMS/Call policy compliance).

### Documentation Reference and Guide
- **KLint**
  - https://pinterest.github.io/ktlint/latest/
- **Gradle**
  - https://docs.gradle.org/current/userguide/userguide.html
- **Kotlin**
  - https://kotlinlang.org/docs/home.html
- **Android Permissions**
  - https://developer.android.com/reference/android/Manifest.permission

### Output Requirements
Generate the complete source code for the app, organized by files (e.g., MainActivity.kt, SmsReceiver.kt, etc.). Include build.gradle files, AndroidManifest.xml, and all necessary resources (strings.xml, themes.xml). Provide a step-by-step setup guide in README.md. Ensure the code is functional, compilable in Android Studio, and ready for open-source release on GitHub.

If any clarifications are needed, ask questions, but aim to produce a self-contained project. Start coding now!
