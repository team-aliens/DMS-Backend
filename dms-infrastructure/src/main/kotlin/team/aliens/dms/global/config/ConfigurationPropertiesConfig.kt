package team.aliens.dms.global.config

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration
import team.aliens.dms.global.security.SecurityProperties
import team.aliens.dms.thirdparty.storage.AwsS3Properties

@ConfigurationPropertiesScan(
    basePackageClasses = [
        SecurityProperties::class,
        AwsS3Properties::class
    ]
)
@Configuration
class ConfigurationPropertiesConfig