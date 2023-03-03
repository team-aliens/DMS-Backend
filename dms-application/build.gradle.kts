plugins {
    kotlin("plugin.allopen") version PluginVersions.ALLOPEN_VERSION
}

dependencies {
    // impl project
    implementation(project(":dms-domain"))

    // transaction
    implementation(Dependencies.SPRING_TRANSACTION)

    // aop
    implementation(Dependencies.SPRING_AOP)
}

allOpen {
    annotation("team.aliens.dms.common.annotation.UseCase")
    annotation("team.aliens.dms.common.annotation.ReadOnlyUseCase")
}