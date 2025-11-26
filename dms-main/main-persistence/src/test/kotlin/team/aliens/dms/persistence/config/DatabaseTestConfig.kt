package team.aliens.dms.persistence.config

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.utility.DockerImageName

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
abstract class DatabaseTestConfig : DescribeSpec() {

    override fun extensions() = listOf(SpringExtension)

    companion object {
        private val mysql = MySQLContainer(DockerImageName.parse("mysql:8.0.28"))
            .apply {
                withDatabaseName("dms")
                withUsername("test")
                withPassword("test")
                start()
            }

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", mysql::getJdbcUrl)
            registry.add("spring.datasource.username", mysql::getUsername)
            registry.add("spring.datasource.password", mysql::getPassword)
            registry.add("spring.datasource.driver-class-name") { "com.mysql.cj.jdbc.Driver" }
            registry.add("spring.jpa.hibernate.ddl-auto") { "create-drop" }
        }
    }
}
