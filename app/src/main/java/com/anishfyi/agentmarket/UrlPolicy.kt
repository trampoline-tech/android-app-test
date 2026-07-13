package com.anishfyi.agentmarket

import java.net.URI

enum class UrlAction { LOAD_IN_APP, OPEN_EXTERNAL, SYSTEM_INTENT }

object UrlPolicy {
    private val internalSuffixes = listOf(".myshopify.com", ".shopify.com", ".shop.app")

    fun actionFor(url: String, storeHost: String): UrlAction {
        val lower = url.lowercase()
        if (lower.startsWith("mailto:") || lower.startsWith("tel:") || lower.startsWith("sms:") || lower.startsWith("intent:")) {
            return UrlAction.SYSTEM_INTENT
        }
        if (!lower.startsWith("http://") && !lower.startsWith("https://")) return UrlAction.SYSTEM_INTENT
        val host = runCatching { URI(url).host?.lowercase() }.getOrNull() ?: return UrlAction.OPEN_EXTERNAL
        if (host == storeHost.lowercase()) return UrlAction.LOAD_IN_APP
        if (internalSuffixes.any { host == it.removePrefix(".") || host.endsWith(it) }) return UrlAction.LOAD_IN_APP
        return UrlAction.OPEN_EXTERNAL
    }
}
