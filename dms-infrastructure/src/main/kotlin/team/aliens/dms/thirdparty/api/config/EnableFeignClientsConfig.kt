package team.aliens.dms.thirdparty.api.config

import feign.codec.ErrorDecoder
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import team.aliens.dms.thirdparty.api.error.FeignClientErrorDecoder

@ImportAutoConfiguration(FeignAutoConfiguration::class)
@EnableFeignClients(basePackages = ["team.aliens.dms.thirdparty.api"])
@Configuration
class EnableFeignClientsConfig {

    @Bean
    @ConditionalOnMissingBean(value = [ErrorDecoder::class])
    fun commonFeignErrorDecoder() = FeignClientErrorDecoder()
}
