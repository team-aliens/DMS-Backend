package team.aliens.dms

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DmsGatewayApplication

fun main(args: Array<String>) {
    runApplication<DmsGatewayApplication>(*args)
}
