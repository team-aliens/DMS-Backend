package team.aliens.dms.global.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.config.annotation.SecurityConfigurer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import team.aliens.dms.global.filter.ExceptionFilter
import team.aliens.dms.global.filter.JwtFilter
import team.aliens.dms.global.security.token.JwtParser

@Component
class FilterConfig(
    private val jwtParser: JwtParser,
    private val objectMapper: ObjectMapper
) : SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity> {
    override fun init(builder: HttpSecurity?) {}

    override fun configure(builder: HttpSecurity) {
        builder.addFilterBefore(JwtFilter(jwtParser), UsernamePasswordAuthenticationFilter::class.java)
        builder.addFilterBefore(ExceptionFilter(objectMapper), JwtFilter::class.java)
    }
}
