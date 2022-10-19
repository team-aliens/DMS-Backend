plugins {
    id("org.springframework.boot") version PluginVersions.SPRING_BOOT_VERSION
    id("io.spring.dependency-management") version PluginVersions.DEPENDENCY_MANAGER_VERSION
    kotlin("plugin.spring") version PluginVersions.SPRING_PLUGIN_VERSION
    kotlin("plugin.jpa") version PluginVersions.JPA_PLUGIN_VERSION
}

dependencies {
    // impl project
    implementation(project(":dms-domain"))
    implementation(project(":dms-application"))
    implementation(project(":dms-presentation"))

    // kotlin
    implementation(Dependencies.JACKSON)

    // validation
    implementation(Dependencies.SPRING_VALIDATION)

    // security
    implementation(Dependencies.SPRING_SECURITY)

    // database
    implementation(Dependencies.SPRING_DATA_JPA)
    runtimeOnly(Dependencies.MYSQL_CONNECTOR)
    implementation(Dependencies.REDIS)
    implementation(Dependencies.SPRING_REDIS)

    // time based uuid
    implementation(Dependencies.UUID_TIME)

    // jwt
    implementation(Dependencies.JWT)

    // configuration
    implementation(Dependencies.CONFIGURATION_PROCESSOR)

    // aws
    implementation(Dependencies.SPRING_AWS)
}

kapt {
    arguments {
        arg("mapstruct.defaultComponentModel", "spring")
        arg("mapstruct.unmappedTargetPolicy", "ignore")
    }
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

noArg {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

tasks.getByName<Jar>("jar") {
    enabled = false
}
