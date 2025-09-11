package team.aliens.dms.global.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration
import java.util.concurrent.TimeUnit

@Configuration
class WebClientConfig(
    private val objectMapper: ObjectMapper
) {

    @Bean
    fun webClient(): WebClient {

        val httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3_000)
            .responseTimeout(Duration.ofSeconds(5))
            .doOnConnected { conn ->
                conn.addHandlerLast(ReadTimeoutHandler(5, TimeUnit.SECONDS))
                    .addHandlerLast(WriteTimeoutHandler(5, TimeUnit.SECONDS))
            }

        val strategies = ExchangeStrategies.builder()
            .codecs { codecs ->
                codecs.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper))
                codecs.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper))
            }
            .build()

        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .exchangeStrategies(strategies)
            .build()
    }
}
