## Goals

- Implement end-to-end MMS receive, parse, display, compose, and send
- Preserve SMS workflows while adding MMS seamlessly to threading
- Keep everything local-only and privacy-preserving

---

## Architecture overview

- Layers
    - Data: Room entities for Message, Part, Thread, Draft, Attachment; MMS APN config model
    - Domain: Use cases for ReceiveMms, ParseMmsPdu, IngestMmsIntoThread, SendMms
    - Presentation: Compose screens for Conversation, Composer with media picker, Attachments preview
- Key components
    - Receivers: WAP_PUSH_RECEIVED_ACTION BroadcastReceiver for MMS
    - Workers: DownloadMmsWorker, SendMmsWorker (WorkManager) for background reliability and retries
    - Services/Utils: MmsPduParser, MmsRepository, ApnResolver, MmsHttpClient
- Storage
    - Room + SQLCipher (placeholder, wired fully in Iteration 4)
    - Media stored in app-scoped storage with EncryptedFile

---

## Data model (Room)

- MessageEntity: id, threadId, transportType(SMS|MMS), box(INBOX|SENT|DRAFT), date, address, subject, body, status, errorCode, mediaPresent(bool)
- MmsPartEntity: messageId FK, seq, contentType, filename, text, fileUri
- ThreadEntity: id, lastMessageSnippet, lastTimestamp, participants, unreadCount
- DraftEntity: threadId, text, attachments

Use Paging 3 for conversation lists and thread messages.

---

## Receiving MMS

1. Register BroadcastReceiver for WAP_PUSH_RECEIVED_ACTION (application exported=false where possible)
2. Extract PDUs and content-type application/vnd.wap.mms-message
3. Parse with MmsPduParser to NotificationInd and RetrieveConf
4. If NotificationInd, schedule DownloadMmsWorker via WorkManager with constraints
5. Download via HTTP to MMSC using APN from Telephony.Carriers.CONTENT_URI
6. Persist RetrieveConf parts to MmsPartEntity and create MessageEntity
7. Post new message to Thread and show system notifications

Security best practices

- Validate content types and sizes, enforce max size limits
- Use HTTPS if carrier supports; otherwise limit redirects, set timeouts, forbid cleartext unless necessary and user-approved
- Avoid intent injection by checking intent action, package, permission

---

## Sending MMS

- Resolve APN and MMSC URL via ApnResolver
- Build PDU with SMIL if multiple parts, else simple
- Compress images client-side, cap to carrier limits
- POST to MMSC with proper headers (X-MMS-*), handle 2xx and retry on 5xx using WorkManager backoff
- Store message and parts optimistically as OUTBOX, update SENT or FAILED

---

## Compose UI updates

- Add Attachment bar with camera, gallery, file
- Thumbnails grid with remove action, size indicators
- Subject line optional
- Accessibility: content descriptions, tap targets 48dp, high contrast

---

## Permissions and roles

- ROLE_SMS via RoleManager
- READ_SMS, RECEIVE_MMS, READ_PHONE_STATE, SEND_SMS, RECEIVE_SMS
- Manage post-Android 13 notification permission

---

## Telephony and APN handling

- Query Telephony.Carriers with READ_APN_SETTINGS where available, fallback to default APN
- Cache APN with encryption
- Handle multi-SIM via SubscriptionManager and user setting

---

## Error handling and robustness

- Queue and retry downloads with WorkManager
- Timeouts, exponential backoff, circuit breaker after N failures
- Large attachments: downscale with quality tradeoffs
- No SIM or airplane mode: surface non-dismissable warning in composer

---

## Testing

- Unit: PDU parsing with golden samples
- Integration: Fake MMSC server (MockWebServer) for send/receive
- UI: Compose UI tests for attachment pick and send flow

---

## Example code map

- data/mms/MmsPduParser.kt
- data/mms/MmsRepository.kt
- data/apn/ApnResolver.kt
- workers/DownloadMmsWorker.kt
- workers/SendMmsWorker.kt
- receivers/MmsReceiver.kt
- ui/conversation/ComposerBar.kt

---

## References

- Android MMS and telephony APIs[[1]](https://developer.android.com/reference/android/Manifest.permission)
- Kotlin language docs[[2]](https://kotlinlang.org/docs/home.html)
- Gradle build docs[[3]](https://docs.gradle.org/current/userguide/userguide.html)
- Ktlint style guide[[4]](https://pinterest.github.io/ktlint/latest/)

---

## Definition of done

- Can receive, parse and display MMS with image and text
- Can compose and send MMS with one or more images
- Robust retries and clear errors to user
- Logged in CHANGELOG and covered by unit and UI tests
