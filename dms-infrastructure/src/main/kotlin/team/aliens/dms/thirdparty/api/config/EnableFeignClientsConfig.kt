package team.aliens.dms.thirdparty.api.config

import feign.codec.ErrorDecoder
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import team.aliens.dms.thirdparty.api.error.FeignClientErrorDecoder

@EnableFeignClients(basePackages = ["team.aliens.dms.thirdparty.api"])
@Configuration
class EnableFeignClientsConfig {

    @Bean
    @ConditionalOnMissingBean(value = [ErrorDecoder::class])
    fun commonFeignErrorDecoder() = FeignClientErrorDecoder()
}
