package com.anishfyi.agentmarket

import org.junit.Assert.assertEquals
import org.junit.Test

class UrlPolicyTest {
    private val host = "testing-fr-vtxocn90.myshopify.com"

    @Test fun storePagesLoadInApp() {
        assertEquals(UrlAction.LOAD_IN_APP, UrlPolicy.actionFor("https://$host/products/aria-sofa", host))
        assertEquals(UrlAction.LOAD_IN_APP, UrlPolicy.actionFor("https://$host/cart", host))
    }

    @Test fun checkoutHostsLoadInApp() {
        assertEquals(UrlAction.LOAD_IN_APP, UrlPolicy.actionFor("https://checkout.shopify.com/x", host))
        assertEquals(UrlAction.LOAD_IN_APP, UrlPolicy.actionFor("https://shop.app/pay", host))
    }

    @Test fun mailtoAndTelGoToSystem() {
        assertEquals(UrlAction.SYSTEM_INTENT, UrlPolicy.actionFor("mailto:x@y.z", host))
        assertEquals(UrlAction.SYSTEM_INTENT, UrlPolicy.actionFor("tel:+441234567890", host))
    }

    @Test fun foreignHostsOpenExternally() {
        assertEquals(UrlAction.OPEN_EXTERNAL, UrlPolicy.actionFor("https://example.com/", host))
    }
}
