package team.aliens.dms.global.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.services")
class ServicesProperties(
    main: String
) {
    val mainUrl: String = main
}
