package com.babymonitor.security

import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationManager(
    private val jwtTokenProvider: JwtTokenProvider
) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        val authToken = authentication.credentials.toString()

        return try {
            if (jwtTokenProvider.validateToken(authToken)) {
                val userId = jwtTokenProvider.getUserIdFromToken(authToken)
                val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))

                Mono.just(
                    UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        authorities
                    )
                )
            } else {
                Mono.empty()
            }
        } catch (e: Exception) {
            Mono.empty()
        }
    }
}

