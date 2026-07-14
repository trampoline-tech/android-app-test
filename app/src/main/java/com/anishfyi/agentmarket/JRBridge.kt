package com.anishfyi.agentmarket

import android.webkit.JavascriptInterface

/**
 * JS↔native bridge exposed to the WebView as `JRBridge`.
 *
 * The web layer opens full-screen overlays (AI search, Maestro concierge) that
 * lock the page scroll at 0. While one is open a pull-down gesture would trip
 * the app's SwipeRefreshLayout, so the page signals overlay state here and the
 * host suppresses pull-to-refresh accordingly.
 *
 * Thread-safety: the flag is written from the WebView's JS thread and read from
 * the UI thread, so its backing field is @Volatile to guarantee cross-thread
 * visibility. It is exposed to Kotlin as the read-only `overlayOpen`; a public
 * `var overlayOpen` would auto-generate a `setOverlayOpen` setter that clashes
 * (same JVM signature) with the `@JavascriptInterface fun setOverlayOpen`, so
 * the mutable state lives in a private backing field.
 */
class JRBridge {
    @Volatile
    private var overlayOpenState = false

    val overlayOpen: Boolean
        get() = overlayOpenState

    @JavascriptInterface
    fun setOverlayOpen(open: Boolean) {
        overlayOpenState = open
    }
}
