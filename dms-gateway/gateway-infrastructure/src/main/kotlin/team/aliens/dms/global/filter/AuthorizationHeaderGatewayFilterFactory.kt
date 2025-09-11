package team.aliens.dms.global.filter

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import team.aliens.dms.domain.auth.model.Passport
import team.aliens.dms.global.client.MainServiceClient
import team.aliens.dms.global.security.HeaderProperties
import team.aliens.dms.global.security.SecurityPaths
import team.aliens.dms.global.security.exception.InvalidTokenException
import team.aliens.dms.global.security.token.JwtParser
import team.aliens.dms.global.security.token.JwtProperties

@Component
class AuthorizationHeaderGatewayFilterFactory(
    private val jwtParser: JwtParser,
    private val objectMapper: ObjectMapper,
    private val mainServiceClient: MainServiceClient
) : AbstractGatewayFilterFactory<AuthorizationHeaderGatewayFilterFactory.Config>(Config::class.java) {

    private val pathMatcher = AntPathMatcher()

    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            val requestPath = exchange.request.uri.path
            val isPermitAllPath = SecurityPaths.PERMIT_ALL_PATHS.any { pathMatcher.match(it, requestPath) }

            if (isPermitAllPath) {
                return@GatewayFilter chain.filter(exchange)
            }

            Mono.fromCallable { resolveToken(exchange) }
                .flatMap { token ->
                    jwtParser.extractUserInfo(token)
                        .flatMap { _ ->
                            mainServiceClient.getPassport(token)
                        }
                        .flatMap { passport ->
                            Mono.fromCallable { serializePassport(passport) }
                                .map { serializedPassport ->
                                    val modifiedExchange = exchange.mutate()
                                        .request {
                                            it.header(HeaderProperties.PASSPORT_HEADER, serializedPassport)
                                        }
                                        .build()
                                    modifiedExchange
                                }
                                .flatMap { modifiedExchange ->
                                    chain.filter(modifiedExchange)
                                }
                        }
                }
        }
    }

    private fun resolveToken(exchange: ServerWebExchange): String {
        val authorizationHeader = exchange.request.headers.getFirst(JwtProperties.HEADER)
            ?: throw InvalidTokenException

        if (!authorizationHeader.startsWith(JwtProperties.PREFIX)) {
            throw InvalidTokenException
        }

        return authorizationHeader.removePrefix(JwtProperties.PREFIX)
    }

    private fun serializePassport(passport: Passport): String {
        return objectMapper.writeValueAsString(passport)
    }

    class Config
}
