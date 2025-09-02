plugins {
    kotlin("plugin.jpa") version PluginVersions.JPA_PLUGIN_VERSION
    kotlin("plugin.spring") version PluginVersions.SPRING_PLUGIN_VERSION
}

dependencies {
    // impl project
    implementation(project(":dms-main:main-core"))

    // notification-contract
    implementation(project(":contracts:notification-contract"))

    // database
    implementation(Dependencies.SPRING_DATA_JPA)
    runtimeOnly(Dependencies.MYSQL_CONNECTOR)
    implementation(Dependencies.REDIS)
    implementation(Dependencies.SPRING_REDIS)

    // querydsl
    implementation(Dependencies.QUERYDSL)
    kapt(Dependencies.QUERYDSL_PROCESSOR)
    kapt(Dependencies.JAKARTA_ANNOTATION_PROCESSOR)
    kapt(Dependencies.JAKARTA_PERSISTENCE_PROCESSOR)

    // time based uuid
    implementation(Dependencies.UUID_TIME)

    // flyway
    implementation(Dependencies.FLYWAY)
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}