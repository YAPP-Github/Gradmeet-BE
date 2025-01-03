package com.dobby.backend.domain.exception

open class OauthException (
    errorCode: ErrorCode,
) : DomainException(errorCode)
class OAuth2AuthenticationException: OauthException(ErrorCode.OAUTH_USER_NOT_FOUND)
class OAuth2ProviderMissingException: OauthException(ErrorCode.OAUTH_PROVIDER_MISSING)
class OAuth2ProviderNotSupportedException: OauthException(ErrorCode.OAUTH_PROVIDER_NOT_FOUND)
class OAuth2EmailNotFoundException : OauthException(ErrorCode.OAUTH_EMAIL_NOT_FOUND)
class OAuth2NameNotFoundException : OauthException(ErrorCode.OAUTH_NAME_NOT_FOUND)

