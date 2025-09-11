package team.aliens.dms.global.client

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import team.aliens.dms.domain.auth.model.Passport
import team.aliens.dms.global.client.exception.MainServerInternalException
import team.aliens.dms.global.client.exception.MainServiceConnectionException
import team.aliens.dms.global.client.exception.MainServiceTimeoutException
import team.aliens.dms.global.client.exception.MainServiceUnavailableException
import team.aliens.dms.global.client.exception.PassportRetrievalException
import team.aliens.dms.global.config.properties.ServicesProperties
import team.aliens.dms.global.security.exception.InvalidTokenException
import team.aliens.dms.global.security.token.JwtProperties
import java.net.ConnectException
import java.util.concurrent.TimeoutException

@Component
class MainServiceClient(
    private val webClient: WebClient,
    private val servicesProperties: ServicesProperties
) {

    fun getPassport(token: String): Mono<Passport> {
        return webClient.post()
            .uri("${servicesProperties.mainUrl}/auth/passport")
            .header(JwtProperties.HEADER, token)
            .retrieve()
            .bodyToMono(Passport::class.java)
            .onErrorMap { ex ->
                when (ex) {
                    is WebClientResponseException.Unauthorized -> InvalidTokenException
                    is WebClientResponseException.InternalServerError -> MainServerInternalException
                    is WebClientResponseException.ServiceUnavailable -> MainServiceUnavailableException
                    is ConnectException -> MainServiceConnectionException
                    is TimeoutException -> MainServiceTimeoutException
                    else -> PassportRetrievalException
                }
            }
    }
}
