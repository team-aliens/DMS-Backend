package team.aliens.dms.thirdparty.email

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.Properties

@Configuration
class SmtpConfig(
    private val smtpProperties: SmtpProperties
) {

    @Bean
    fun javaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()

        mailSender.host = smtpProperties.host
        mailSender.port = smtpProperties.port
        mailSender.username = smtpProperties.username
        mailSender.password = smtpProperties.password

        val props: Properties = mailSender.javaMailProperties
        smtpProperties.properties.forEach { (key, value) ->
            props[key] = value
        }

        return mailSender
    }
}
