plugins {
    id("org.springframework.boot") version PluginVersions.SPRING_BOOT_VERSION
    kotlin("plugin.spring") version PluginVersions.SPRING_PLUGIN_VERSION
}

dependencies {
    //core
    implementation(project(":dms-gateway:gateway-core"))

    //infra
    implementation(project(":dms-gateway:gateway-infrastructure"))

    // Spring Cloud Gateway
    implementation(Dependencies.SPRING_CLOUD_GATEWAY)
}

tasks.getByName<Jar>("jar") {
    enabled = false
}