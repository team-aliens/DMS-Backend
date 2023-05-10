package team.aliens.dms.thirdparty.encrypt

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.kms.AWSKMSAsync
import com.amazonaws.services.kms.AWSKMSAsyncClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import team.aliens.dms.thirdparty.AwsProperties

@Configuration
class AwsKMSConfiguration(
    private val awsProperties: AwsProperties
) {
    @Bean
    fun awsKMSAsync(): AWSKMSAsync {
        val basicAWSCredentials = BasicAWSCredentials(awsProperties.accessKey, awsProperties.secretKey)

        return AWSKMSAsyncClient.asyncBuilder()
            .withCredentials(AWSStaticCredentialsProvider(basicAWSCredentials))
            .withRegion(Regions.AP_NORTHEAST_2)
            .build()
    }
}
