package team.aliens.dms.thirdparty.email

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsyncClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import team.aliens.dms.thirdparty.AwsProperties

@Configuration
class AwsSesConfig(
    private val awsProperties: AwsProperties
) {

    @Bean
    fun amazonSimpleEmailService(): AmazonSimpleEmailServiceAsync? {
        val basicAWSCredentials = BasicAWSCredentials(awsProperties.accessKey, awsProperties.secretKey)

        return AmazonSimpleEmailServiceAsyncClient.asyncBuilder()
            .withCredentials(AWSStaticCredentialsProvider(basicAWSCredentials))
            .withRegion(Regions.US_WEST_2)
            .build()
    }
}