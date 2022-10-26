package team.aliens.dms.global.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("cloud.aws.credentials")
@ConstructorBinding
class AwsProperties(
    val accessKey: String,
    val secretKey: String
)