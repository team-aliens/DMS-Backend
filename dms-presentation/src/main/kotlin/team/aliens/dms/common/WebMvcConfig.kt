package team.aliens.dms.common

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowedOrigins(
                "http://localhost:3000",
                "http://localhost:3001",
                "http://localhost:3002",
                "https://www.aliens-dms.com",
                "https://admin.aliens-dms.com",
                "https://admin-dev.aliens-dms.com",
                "https://admin.dms-dsm.com",
                "https://admin-dev.dms-dsm.com",
                "https://webview.dms-dsm.com"
            )
            .exposedHeaders(HttpHeaders.CONTENT_DISPOSITION)
    }
}
