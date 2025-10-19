package team.aliens.dms

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DmsBackendApplication

fun main(args: Array<String>) {
    runApplication<DmsBackendApplication>(*args)
}
