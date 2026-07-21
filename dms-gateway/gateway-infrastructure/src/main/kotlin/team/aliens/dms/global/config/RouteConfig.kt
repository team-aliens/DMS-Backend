package team.aliens.dms.global.config

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import team.aliens.dms.global.config.properties.ServicesProperties
import team.aliens.dms.global.filter.AuthorizationHeaderGatewayFilterFactory

@Configuration
class RouteConfig(
    private val authorizationHeaderGatewayFilterFactory: AuthorizationHeaderGatewayFilterFactory,
    private val servicesProperties: ServicesProperties
) {

    @Bean
    fun customRouteLocator(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes()
            .route("dms-main") { r ->
                r.path("/**")
                    .filters { f ->
                        f.filter(
                            authorizationHeaderGatewayFilterFactory.apply(
                                AuthorizationHeaderGatewayFilterFactory.Config()
                            )
                        )
                    }
                    .uri(servicesProperties.mainUrl)
            }
            .build()
    }
}
