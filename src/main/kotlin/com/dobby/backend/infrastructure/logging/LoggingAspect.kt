package com.dobby.backend.infrastructure.logging

import com.dobby.backend.infrastructure.identifier.TsidIdGenerator
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.net.URLDecoder
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Aspect
@Component
class LoggingAspect(
    private val tsidIdGenerator: TsidIdGenerator
) {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    @Suppress("EmptyFunctionBlock")
    @Pointcut("execution(* com.dobby.backend.presentation.api.controller..*Controller.*(..))")
    private fun allController() {}

    @Around("allController()")
    fun logRequest(joinPoint: ProceedingJoinPoint): Any? {
        val taskId = generateTaskId()
        MDC.put("taskId", taskId)

        val request = (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes)?.request
        val method = request?.method ?: "UNKNOWN_METHOD"
        val requestURI = request?.requestURI ?: "UNKNOWN_URI"
        val decodedURI = URLDecoder.decode(requestURI, "UTF-8")

        val args = joinPoint.args.map { SensitiveDataMasker.mask(it) }

        log.info("[{}] {} -> {} args={}", taskId, method, decodedURI, args)

        try {
            return joinPoint.proceed()
        } finally {
            MDC.remove("taskId") // 요청이 끝난 후 MDC에서 taskId 제거 (메모리 누수 방지)
        }
    }

    @AfterReturning(
        value = "execution(* com.dobby.backend.presentation.api.controller..*Controller.*(..))",
        returning = "result"
    )
    fun logResponse(joinPoint: JoinPoint, result: Any?) {
        val taskId = MDC.get("taskId") ?: generateTaskId()
        val methodSignature = joinPoint.signature as MethodSignature
        val controllerMethodName = methodSignature.method.name

        log.info("[{}] <- method: {}, result: {}", taskId, controllerMethodName, SensitiveDataMasker.mask(result))
    }

    /**
     * 타임스탬프 + Nano ID 기반 taskId 생성
     */
    private fun generateTaskId(): String {
        val timestamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
            .format(ZonedDateTime.now(ZoneId.of("Asia/Seoul")))
        val tsid = tsidIdGenerator.generateId()
        return "$timestamp-$tsid"
    }
}
