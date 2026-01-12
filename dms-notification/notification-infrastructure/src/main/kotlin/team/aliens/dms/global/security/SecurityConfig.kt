package team.aliens.dms.global.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import team.aliens.dms.global.filter.FilterConfig

@Configuration
class SecurityConfig(
    private val authenticationEntryPoint: CustomAuthenticationEntryPoint,
    private val accessDeniedHandler: CustomAccessDeniedHandler,
    private val filterConfig: FilterConfig
) {

    @Bean
    protected fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .cors(Customizer.withDefaults())
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }

        http
            .authorizeHttpRequests { authorize ->
                authorize
                    // /notification
                    .requestMatchers(HttpMethod.DELETE, "/notifications/{notification-of-user-id}").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/notifications").authenticated()
                    .requestMatchers(HttpMethod.POST, "/notifications/token").authenticated()
                    .requestMatchers(HttpMethod.POST, "/notifications/topic").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/notifications/topic").authenticated()
                    .requestMatchers(HttpMethod.GET, "/notifications/topic").authenticated()
                    .requestMatchers(HttpMethod.PATCH, "/notifications/topic").authenticated()
                    .requestMatchers(HttpMethod.GET, "/notifications").authenticated()
                    .requestMatchers(HttpMethod.PATCH, "/notifications/{notification-of-user-id}/read").authenticated()
                    .requestMatchers(HttpMethod.PATCH, "/notifications/topic/toggle").authenticated()
            }
        http
            .apply(filterConfig)

        http
            .exceptionHandling { exceptionHandling ->
                exceptionHandling
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
            }
        return http.build()
    }

    @Bean
    protected fun passwordEncoder() = BCryptPasswordEncoder()
}
