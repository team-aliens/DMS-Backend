package team.aliens.dms.domain

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckWebAdapter {

    @GetMapping("/main")
    fun healthCheck() = "OK"
}
