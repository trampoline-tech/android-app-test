# Jodhpur Rugs — Android app

A minimal Kotlin WebView wrapper for the Jodhpur Rugs storefront
(`testing-fr-vtxocn90.myshopify.com`): full-screen store, splash screen,
hardware-back walks web history, pull-to-refresh, offline retry page,
external links handed to the system.

## Build

```bash
./gradlew :app:assembleDebug      # APK: app/build/outputs/apk/debug/app-debug.apk
./gradlew :app:testDebugUnitTest  # UrlPolicy unit tests
```

Requires JDK 17 and an Android SDK (platform 35); point `local.properties`
at your SDK or set `ANDROID_HOME`. Sideload with `adb install`.

## Structure

- `app/src/main/java/.../MainActivity.kt` — WebView host
- `app/src/main/java/.../UrlPolicy.kt` — in-app vs external URL decisions (unit-tested)
- `app/src/main/assets/error.html` — offline page
