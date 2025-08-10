package com.babymonitor.config

import com.babymonitor.security.JwtAuthenticationManager
import com.babymonitor.security.SecurityContextRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    private val authenticationManager: JwtAuthenticationManager,
    private val securityContextRepository: SecurityContextRepository
) {

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .exceptionHandling { exchanges ->
                exchanges
                    .authenticationEntryPoint { _, ex ->
                        Mono.fromRunnable {
                            ex.response.statusCode = HttpStatus.UNAUTHORIZED
                        }
                    }
                    .accessDeniedHandler { _, ex ->
                        Mono.fromRunnable {
                            ex.response.statusCode = HttpStatus.FORBIDDEN
                        }
                    }
            }
            .authorizeExchange { exchanges ->
                exchanges
                    .pathMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
                    .pathMatchers(HttpMethod.POST, "/api/v1/users/register").permitAll()
                    .pathMatchers("/actuator/**").permitAll()
                    .pathMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .anyExchange().authenticated()
            }
            .authenticationManager(authenticationManager)
            .securityContextRepository(securityContextRepository)
            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOriginPatterns = listOf("*")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}

