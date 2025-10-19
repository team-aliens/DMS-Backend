plugins {
    id("org.springframework.boot") version PluginVersions.SPRING_BOOT_VERSION
    id("io.spring.dependency-management") version PluginVersions.DEPENDENCY_MANAGER_VERSION
    kotlin("plugin.spring") version PluginVersions.SPRING_PLUGIN_VERSION
}

dependencies {
    // layer
    implementation(project(":dms-main:main-core"))

    // contracts
    implementation(project(":contracts:enum-contracts:notification-enum"))

    // web
    implementation(Dependencies.SPRING_WEB)

    // validation
    implementation(Dependencies.SPRING_VALIDATION)
}

tasks.getByName<Jar>("bootJar") {
    enabled = false
}