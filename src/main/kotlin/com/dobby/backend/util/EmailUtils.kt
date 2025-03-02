package com.dobby.backend.util

import com.dobby.domain.exception.EmailFormatInvalidException
import java.util.*
import javax.naming.directory.Attributes
import javax.naming.directory.InitialDirContext

object EmailUtils{
    fun extractDomain(email:String): String {
        if(!email.contains("@")) throw EmailFormatInvalidException
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
        val eduDomains = setOf(
            "postech.edu",
            "kaist.edu",
            "handong.edu",
            "ewhain.net",
            "g.skku.edu"
        )
        return email.endsWith(".ac.kr") || eduDomains.any { email.endsWith(it) }
    }
    fun generateCode(): String {
        val randomNum = (0..999999).random()
        return String.format("%06d", randomNum)
    }
}
