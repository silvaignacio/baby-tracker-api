package com.babymonitor.security

import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class SecurityContextRepository(
    private val authenticationManager: JwtAuthenticationManager
) : ServerSecurityContextRepository {

    override fun save(exchange: ServerWebExchange, context: SecurityContext): Mono<Void> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {
        val request = exchange.request
        val authHeader = request.headers.getFirst(HttpHeaders.AUTHORIZATION)

        return if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val authToken = authHeader.substring(7)
            val auth = UsernamePasswordAuthenticationToken(authToken, authToken)

            authenticationManager.authenticate(auth)
                .cast(org.springframework.security.core.Authentication::class.java)
                .map { authentication -> SecurityContextImpl(authentication) }
        } else {
            Mono.empty()
        }
    }
}

