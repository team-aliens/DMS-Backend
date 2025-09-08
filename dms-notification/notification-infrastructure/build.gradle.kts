plugins {
    id("org.springframework.boot") version PluginVersions.SPRING_BOOT_VERSION
    id("io.spring.dependency-management") version PluginVersions.DEPENDENCY_MANAGER_VERSION
    kotlin("plugin.spring") version PluginVersions.SPRING_PLUGIN_VERSION
    kotlin("plugin.jpa") version PluginVersions.JPA_PLUGIN_VERSION
    id("io.gitlab.arturbosch.detekt") version PluginVersions.DETEKT_VERSION
}

dependencies {
    // impl project
    implementation(project(":dms-notification:notification-persistence"))
    implementation(project(":dms-notification:notification-core"))

    // contract
    implementation(project(":contracts:notification-contract"))

    // logging
    implementation(Dependencies.SENTRY)

    // notification
    implementation(Dependencies.FCM)

    // rabbitmq
    implementation(Dependencies.RABBITMQ)

    // security
    implementation(Dependencies.SPRING_SECURITY)

    // jwt
    implementation(Dependencies.JWT)

    implementation(Dependencies.SPRING_VALIDATION)
    runtimeOnly(Dependencies.JWT_IMPL)
    runtimeOnly(Dependencies.JWT_JACKSON)
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

detekt {
    ignoreFailures = true
}
