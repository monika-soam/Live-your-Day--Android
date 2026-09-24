# Live your Day — Android app

This project packages the finalized Live your Day V8 web experience as an Android app using a native WebView wrapper.

## Build on Android Studio
1. Open this folder in Android Studio.
2. Let Gradle sync/download Android dependencies.
3. Connect an Android phone with USB debugging enabled, or create an emulator.
4. Run the `app` configuration.
5. For an APK: **Build → Build APK(s)**.

The APK will be under `app/build/outputs/apk/debug/`.

## Included
- Final V8 UI and themes
- Fixed bottom navigation + visible quick-add area
- Path to a Brighter Day launcher icon
- LocalStorage persistence from the web app
- Android native speech-recognition bridge for microphone input
- Portrait-first mobile layout

For a production Play Store release, next add signing, backup/cloud sync, notification/reminder channels, and Play Console metadata.
