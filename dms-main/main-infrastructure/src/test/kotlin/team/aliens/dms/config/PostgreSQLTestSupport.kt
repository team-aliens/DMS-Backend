package team.aliens.dms.config

import org.springframework.test.context.DynamicPropertyRegistry
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import java.time.Duration

object PostgreSQLTestContainer {
    val instance: PostgreSQLContainer<*> = PostgreSQLContainer(
        DockerImageName.parse("pgvector/pgvector:pg16").asCompatibleSubstituteFor("postgres")
    ).apply {
        withDatabaseName("dms")
        withUsername("test")
        withPassword("test")
        withInitScript("db/init-pgvector.sql")
        withStartupTimeout(Duration.ofMinutes(5))
        start()
    }

    fun configure(registry: DynamicPropertyRegistry) {
        registry.add("spring.datasource.url", instance::getJdbcUrl)
        registry.add("spring.datasource.username", instance::getUsername)
        registry.add("spring.datasource.password", instance::getPassword)
        registry.add("spring.datasource.driver-class-name") { "org.postgresql.Driver" }
        registry.add("spring.jpa.hibernate.ddl-auto") { "create-drop" }
    }
}
