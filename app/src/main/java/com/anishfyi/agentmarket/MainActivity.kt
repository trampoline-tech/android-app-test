package com.anishfyi.agentmarket

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : ComponentActivity() {
    private lateinit var web: WebView
    private lateinit var swipe: SwipeRefreshLayout
    private lateinit var brandSplash: View

    private val mainHandler = Handler(Looper.getMainLooper())
    private var brandSplashDismissed = false

    private val storeUrl = "https://testing-fr-vtxocn90.myshopify.com/"
    private val storeHost = "testing-fr-vtxocn90.myshopify.com"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        swipe = findViewById(R.id.swipe)
        web = findViewById(R.id.web)
        brandSplash = findViewById(R.id.brandSplash)
        web.settings.javaScriptEnabled = true
        web.settings.domStorageEnabled = true

        web.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val url = request.url.toString()
                return when (UrlPolicy.actionFor(url, storeHost)) {
                    UrlAction.LOAD_IN_APP -> false
                    UrlAction.OPEN_EXTERNAL, UrlAction.SYSTEM_INTENT -> {
                        runCatching { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }
                        true
                    }
                }
            }

            override fun onPageFinished(view: WebView, url: String) {
                swipe.isRefreshing = false
                hideBrandSplash()
            }

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                if (request.isForMainFrame) {
                    hideBrandSplash()
                    view.loadUrl("file:///android_asset/error.html")
                }
            }
        }

        swipe.setOnRefreshListener { web.reload() }
        onBackPressedDispatcher.addCallback(this) {
            if (web.canGoBack()) web.goBack() else finish()
        }

        // Fallback so the brand moment never traps the user (~0.5–2.5s max).
        mainHandler.postDelayed({ hideBrandSplash() }, 2500)

        if (savedInstanceState == null) web.loadUrl(storeUrl) else web.restoreState(savedInstanceState)
    }

    /** Fade out the branded opening overlay exactly once. */
    private fun hideBrandSplash() {
        if (brandSplashDismissed) return
        brandSplashDismissed = true
        mainHandler.removeCallbacksAndMessages(null)
        brandSplash.animate()
            .alpha(0f)
            .setDuration(300)
            .withEndAction { brandSplash.visibility = View.GONE }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        web.saveState(outState)
    }
}
