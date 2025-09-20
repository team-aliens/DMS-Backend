object Dependencies {

    // kotlin
    const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect"
    const val KOTLIN_JDK = "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
    const val JACKSON = "com.fasterxml.jackson.module:jackson-module-kotlin:${DependencyVersions.JACKSON_VERSION}"
    const val JACKSON_TYPE = "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${DependencyVersions.JACKSON_VERSION}"

    // java servlet
    const val JAVA_SERVLET = "jakarta.servlet:jakarta.servlet-api:${DependencyVersions.SERVLET_VERSION}"

    // web
    const val SPRING_WEB = "org.springframework.boot:spring-boot-starter-web"

    // validation
    const val SPRING_VALIDATION = "org.springframework.boot:spring-boot-starter-validation"

    // thymeleaf
    const val SPRING_THYMELEAF = "org.springframework.boot:spring-boot-starter-thymeleaf"

    // transaction
    const val SPRING_TRANSACTION = "org.springframework:spring-tx:${DependencyVersions.SPRING_TRANSACTION_VERSION}"

    // configuration
    const val CONFIGURATION_PROCESSOR = "org.springframework.boot:spring-boot-configuration-processor"

    // database
    const val SPRING_DATA_JPA = "org.springframework.boot:spring-boot-starter-data-jpa:${PluginVersions.SPRING_BOOT_VERSION}"
    const val MYSQL_CONNECTOR = "mysql:mysql-connector-java:${DependencyVersions.MYSQL}"
    const val SPRING_REDIS = "org.springframework.boot:spring-boot-starter-data-redis:${PluginVersions.SPRING_BOOT_VERSION}"
    const val REDIS = "org.springframework.data:spring-data-redis:${DependencyVersions.REDIS_VERSION}"

    // querydsl
    const val QUERYDSL = "com.querydsl:querydsl-jpa:${DependencyVersions.QUERYDSL_VERSION}:jakarta"
    const val QUERYDSL_PROCESSOR = "com.querydsl:querydsl-apt:${DependencyVersions.QUERYDSL_VERSION}:jakarta"
    const val JAKARTA_ANNOTATION_PROCESSOR = "jakarta.annotation:jakarta.annotation-api:${DependencyVersions.JAKARTA_ANNOTATION_VERSION}"
    const val JAKARTA_PERSISTENCE_PROCESSOR = "jakarta.persistence:jakarta.persistence-api:${DependencyVersions.JAKARTA_PERSISTENCE_VERSION}"

    // security
    const val SPRING_SECURITY = "org.springframework.boot:spring-boot-starter-security"

    // jwt
    const val JWT = "io.jsonwebtoken:jjwt-api:${DependencyVersions.JWT_VERSION}"
    const val JWT_IMPL = "io.jsonwebtoken:jjwt-impl:${DependencyVersions.JWT_VERSION}"
    const val JWT_JACKSON = "io.jsonwebtoken:jjwt-jackson:${DependencyVersions.JWT_VERSION}"

    // aws
    const val SPRING_AWS = "io.awspring.cloud:spring-cloud-starter-aws:${DependencyVersions.AWS_VERSION}"
    const val AWS_SES = "com.amazonaws:aws-java-sdk-ses:${DependencyVersions.SES_VERSION}"

    // test
    const val SPRING_TEST = "org.springframework.boot:spring-boot-starter-test:${PluginVersions.SPRING_BOOT_VERSION}"
    const val MOCKK = "io.mockk:mockk:${DependencyVersions.MOCKK_VERSION}"
    const val SPRING_KOTEST = "io.kotest.extensions:kotest-extensions-spring:${DependencyVersions.SPRING_KOTEST_VERSION}"
    const val KOTEST = "io.kotest:kotest-runner-junit5:${DependencyVersions.KOTEST_VERSION}"
    const val KOTEST_ASSERTIONS = "io.kotest:kotest-assertions-core:${DependencyVersions.KOTEST_VERSION}"

    // time based uuid
    const val UUID_TIME = "com.fasterxml.uuid:java-uuid-generator:${DependencyVersions.UUID_TIME_VERSION}"

    // excel
    const val APACHE_POI = "org.apache.poi:poi:${DependencyVersions.APACHE_POI_VERSION}"
    const val APACHE_POI_OOXML = "org.apache.poi:poi-ooxml:${DependencyVersions.APACHE_POI_VERSION}"

    // aop
    const val AOP = "org.aspectj:aspectjweaver:${DependencyVersions.ASPECTJ_VERSION}"

    // detekt
    const val DETEKT = "io.gitlab.arturbosch.detekt:detekt-formatting:${PluginVersions.DETEKT_VERSION}"

    // open feign
    const val OPEN_FEIGN = "org.springframework.cloud:spring-cloud-starter-openfeign:${DependencyVersions.OPEN_FEIGN_VERSION}"

    // gson
    const val GSON = "com.google.code.gson:gson"

    // sentry
    const val SENTRY = "io.sentry:sentry-spring-boot-starter:${DependencyVersions.SENTRY_VERSION}"

    // notification
    const val FCM = "com.google.firebase:firebase-admin:${DependencyVersions.FCM_VERSION}"

    // flyway
    const val FLYWAY = "org.flywaydb:flyway-mysql:${DependencyVersions.FLYWAY_VERSION}"

    // slack
    const val SLACK = "com.slack.api:slack-api-client:${DependencyVersions.SLACK_VERSION}"

    // RabbitMq
    const val RABBITMQ = "org.springframework.boot:spring-boot-starter-amqp:${DependencyVersions.RABBITMQ_VERSION}"

    // grpc
    const val GRPC_SERVER = "net.devh:grpc-server-spring-boot-starter:${DependencyVersions.GRPC_STARTER_VERSION}"
    const val GRPC_CLIENT = "net.devh:grpc-client-spring-boot-starter:${DependencyVersions.GRPC_STARTER_VERSION}"
    const val GRPC_PROTOBUF = "io.grpc:grpc-protobuf:${DependencyVersions.GRPC_VERSION}"
    const val GRPC_STUB = "io.grpc:grpc-stub:${DependencyVersions.GRPC_VERSION}"
    const val GRPC_NETTY_SHADED = "io.grpc:grpc-netty-shaded:${DependencyVersions.GRPC_VERSION}"

    // spring cloud gateway
    const val SPRING_CLOUD_GATEWAY = "org.springframework.cloud:spring-cloud-starter-gateway:${DependencyVersions.SPRING_CLOUD_GATEWAY_VERSION}"
}