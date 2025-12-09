plugins {
    id("org.springframework.boot") version PluginVersions.SPRING_BOOT_VERSION
    id("io.spring.dependency-management") version PluginVersions.DEPENDENCY_MANAGER_VERSION
    kotlin("plugin.spring") version PluginVersions.SPRING_PLUGIN_VERSION
    kotlin("plugin.jpa") version PluginVersions.JPA_PLUGIN_VERSION
}

dependencies {
    // layer
    implementation(project(":dms-main:main-persistence"))
    implementation(project(":dms-main:main-core"))
    implementation(project(":dms-main:main-presentation"))

    // contract
    implementation(project(":contracts:model-contracts:notification-model"))
    implementation(project(":contracts:remote-contracts:notification-remote:rabbitmq-message"))
    implementation(project(":contracts:enum-contracts:notification-enum"))

    // validation
    implementation(Dependencies.SPRING_VALIDATION)

    // transaction
    implementation(Dependencies.SPRING_TRANSACTION)

    // thymeleaf
    implementation(Dependencies.SPRING_THYMELEAF)

    // mail
    implementation(Dependencies.SPRING_MAIL)

    // security
    implementation(Dependencies.SPRING_SECURITY)

    // jwt
    implementation(Dependencies.JWT)
    runtimeOnly(Dependencies.JWT_IMPL)
    runtimeOnly(Dependencies.JWT_JACKSON)

    // aws
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

    // rabbit mq
    implementation(Dependencies.RABBITMQ)

    // jackson
    implementation(Dependencies.JACKSON_TYPE)

    // test
    testImplementation(Dependencies.SPRING_TEST)
    testImplementation(Dependencies.KOTEST)
    testImplementation(Dependencies.KOTEST_ASSERTIONS)
    testImplementation(Dependencies.SPRING_KOTEST)
    testImplementation(Dependencies.TESTCONTAINERS)
    testImplementation(Dependencies.TESTCONTAINERS_MYSQL)
    testImplementation(Dependencies.TESTCONTAINERS_RABBITMQ)
    testImplementation(Dependencies.SPRING_DATA_JPA)
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = true
    classifier = "boot"
}

tasks.getByName<Jar>("jar") {
    enabled = true
}