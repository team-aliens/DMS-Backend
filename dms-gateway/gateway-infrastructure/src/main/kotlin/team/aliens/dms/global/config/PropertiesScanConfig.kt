package team.aliens.dms.global.config

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration
import team.aliens.dms.global.config.properties.ServicesProperties
import team.aliens.dms.global.security.SecurityProperties

@ConfigurationPropertiesScan(
    basePackageClasses = [
        SecurityProperties::class,
        ServicesProperties::class
    ]
)
@Configuration
class PropertiesScanConfig
