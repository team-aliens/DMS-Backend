plugins {
    kotlin("plugin.jpa") version PluginVersions.JPA_PLUGIN_VERSION
    kotlin("plugin.spring") version PluginVersions.SPRING_PLUGIN_VERSION
    id("io.gitlab.arturbosch.detekt") version PluginVersions.DETEKT_VERSION
}

dependencies {
    // impl project
    implementation(project(":dms-notification:notification-core"))

    // contract
    implementation(project(":contracts:enum-contracts:notification-enum"))

    // database
    implementation(Dependencies.SPRING_DATA_JPA)
    runtimeOnly(Dependencies.MYSQL_CONNECTOR)

    // querydsl
    implementation(Dependencies.QUERYDSL)
    kapt(Dependencies.QUERYDSL_PROCESSOR)
    kapt(Dependencies.JAKARTA_ANNOTATION_PROCESSOR)
    kapt(Dependencies.JAKARTA_PERSISTENCE_PROCESSOR)

    // flyway
    implementation(Dependencies.FLYWAY)

    // time based uuid
    implementation(Dependencies.UUID_TIME)
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

kapt {
    arguments {
        arg("querydsl.generatedAnnotationClass", "jakarta.annotation.Generated")
    }
}

detekt {
    ignoreFailures = true
}
