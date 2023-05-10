package team.aliens.dms.thirdparty.encrypt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("cloud.aws.kms")
@ConstructorBinding
class AwsKMSProperties(
    val key: String
)
