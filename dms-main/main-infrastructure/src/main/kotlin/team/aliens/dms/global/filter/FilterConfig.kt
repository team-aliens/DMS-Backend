package team.aliens.dms.global.filter

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.config.annotation.SecurityConfigurer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import team.aliens.dms.global.security.passport.PassportValidator

@Component
class FilterConfig(
    private val passportValidator: PassportValidator,
    private val objectMapper: ObjectMapper,
) : SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity> {
    override fun init(builder: HttpSecurity?) {}

    override fun configure(builder: HttpSecurity) {
        builder.addFilterBefore(PassportFilter(passportValidator, objectMapper), UsernamePasswordAuthenticationFilter::class.java)
        builder.addFilterBefore(ExceptionFilter(objectMapper), PassportFilter::class.java)
    }
}
