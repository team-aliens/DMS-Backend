package team.aliens.dms.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import team.aliens.dms.global.security.CustomAccessDeniedHandler
import team.aliens.dms.global.security.CustomAuthenticationEntryPoint

@Configuration
class SecurityConfig(
    private val filterConfig: FilterConfig,
    private val authenticationEntryPoint: CustomAuthenticationEntryPoint,
    private val accessDeniedHandler: CustomAccessDeniedHandler
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
                    .requestMatchers("/**").authenticated()
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
}
