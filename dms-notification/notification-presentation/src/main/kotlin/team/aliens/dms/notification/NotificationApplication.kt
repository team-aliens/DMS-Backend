package team.aliens.dms.notification

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class NotificationApplication

fun main(args: Array<String>) {
    runApplication<NotificationApplication>(*args)
}
