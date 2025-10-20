package team.aliens.dms

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class DmsNotificationApplication

fun main(args: Array<String>) {
    runApplication<DmsNotificationApplication>(*args)
}
