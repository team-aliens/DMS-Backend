package team.aliens.dms.global.config

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration
import team.aliens.dms.global.security.CorsProperties
import team.aliens.dms.global.security.SecurityProperties
import team.aliens.dms.thirdparty.AwsProperties
import team.aliens.dms.thirdparty.email.SmtpProperties
import team.aliens.dms.thirdparty.storage.AwsS3Properties

@ConfigurationPropertiesScan(
    basePackageClasses = [
        SecurityProperties::class,
        CorsProperties::class,
        AwsS3Properties::class,
        SmtpProperties::class,
        AwsProperties::class,
    ]
)
@Configuration
class PropertiesScanConfig
