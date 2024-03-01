package team.aliens.dms.thirdparty.email

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("cloud.aws.ses")
class AwsSESProperties(
    val source: String,
)
