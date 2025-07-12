package com.dobby.util

import com.dobby.exception.EmailFormatInvalidException
import java.util.*
import javax.naming.directory.Attributes
import javax.naming.directory.InitialDirContext

object EmailUtils {
    fun extractDomain(email: String): String {
        if (!email.contains("@")) throw EmailFormatInvalidException
        return email.substringAfter("@")
    }
    fun isDomainExists(email: String): Boolean {
        val domain = extractDomain(email)
        return try {
            val env = Hashtable<String, String>()
            env["java.naming.factory.initial"] = "com.sun.jndi.dns.DnsContextFactory"
            val ctx = InitialDirContext(env)
            val attributes: Attributes = ctx.getAttributes(domain, arrayOf("MX"))
            val mxRecords = attributes["MX"]
            mxRecords != null
        } catch (ex: Exception) {
            false
        }
    }

    fun isUnivMail(email: String): Boolean {
        val univDomainExcpetions = setOf(
            "ewhain.net"
        )
        return email.endsWith(".ac.kr") ||
            email.endsWith(".edu") ||
            email.endsWith("ac.uk") ||
            email.endsWith(".edu.au") ||
            univDomainExcpetions.any { email.endsWith(it) }
    }

    fun generateCode(): String {
        val randomNum = (0..999999).random()
        return String.format("%06d", randomNum)
    }
}
