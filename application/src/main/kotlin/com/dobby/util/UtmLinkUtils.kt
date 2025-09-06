package com.dobby.util

object UtmLinkUtils {
    private const val EMAIL_UTM = "utm_source=email&utm_medium=daily"

    fun add(url: String, utm: String = EMAIL_UTM) : String {
        if (url.contains("utm_source=") || url.contains("utm_medium=")) return url
        val sep = if (url.contains("?")) "&" else "?"
        return url + sep + utm
    }
}
