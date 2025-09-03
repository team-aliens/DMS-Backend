plugins {
    id("org.springframework.boot") version PluginVersions.SPRING_BOOT_VERSION
    id("io.spring.dependency-management") version PluginVersions.DEPENDENCY_MANAGER_VERSION
    kotlin("plugin.spring") version PluginVersions.SPRING_PLUGIN_VERSION
    kotlin("plugin.jpa") version PluginVersions.JPA_PLUGIN_VERSION
}

dependencies {
    // impl project
    implementation(project(":dms-main:main-persistence"))
    implementation(project(":dms-main:main-core"))
    implementation(project(":dms-main:main-presentation"))

    // notification contract
    implementation(project(":contracts:notification-contract"))

    // validation
    implementation(Dependencies.SPRING_VALIDATION)

    // transaction
    implementation(Dependencies.SPRING_TRANSACTION)

    // thymeleaf
    implementation(Dependencies.SPRING_THYMELEAF)

    // security
    implementation(Dependencies.SPRING_SECURITY)

    // jwt
    implementation(Dependencies.JWT)
    runtimeOnly(Dependencies.JWT_IMPL)
    runtimeOnly(Dependencies.JWT_JACKSON)

    // aws
    implementation(Dependencies.AWS_SES)
    implementation(Dependencies.SPRING_AWS)

    // slack
    implementation(Dependencies.SLACK)

    // configuration
    kapt(Dependencies.CONFIGURATION_PROCESSOR)

    // excel
    implementation(Dependencies.APACHE_POI)
    implementation(Dependencies.APACHE_POI_OOXML)

    // open feign
    implementation(Dependencies.OPEN_FEIGN)

    // gson
    implementation(Dependencies.GSON)

    // jackson
    implementation(Dependencies.JACKSON_TYPE)

    // aop
    implementation(Dependencies.AOP)

    // logging
    implementation(Dependencies.SENTRY)

    // notification
    implementation(Dependencies.FCM)

    // RabbitMq
    implementation(Dependencies.RABBITMQ)
}

tasks.getByName<Jar>("jar") {
    enabled = false
}