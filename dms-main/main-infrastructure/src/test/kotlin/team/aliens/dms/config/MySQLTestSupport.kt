package team.aliens.dms.config

import org.springframework.test.context.DynamicPropertyRegistry
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.utility.DockerImageName

object MySQLTestContainer {
    val instance: MySQLContainer<*> = MySQLContainer(DockerImageName.parse("mysql:8.0.28"))
        .apply {
            withDatabaseName("dms")
            withUsername("test")
            withPassword("test")
            start()
        }

    fun configure(registry: DynamicPropertyRegistry) {
        registry.add("spring.datasource.url", instance::getJdbcUrl)
        registry.add("spring.datasource.username", instance::getUsername)
        registry.add("spring.datasource.password", instance::getPassword)
        registry.add("spring.datasource.driver-class-name") { "com.mysql.cj.jdbc.Driver" }
        registry.add("spring.jpa.hibernate.ddl-auto") { "create-drop" }
    }
}
