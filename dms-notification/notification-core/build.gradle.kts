plugins {
    kotlin("plugin.allopen") version PluginVersions.ALLOPEN_VERSION
    id("io.gitlab.arturbosch.detekt") version PluginVersions.DETEKT_VERSION
}

dependencies {
    // transaction
    implementation(Dependencies.SPRING_TRANSACTION)

    // contract
    implementation(project(":contracts:model-contracts:notification-model"))
    implementation(project(":contracts:enum-contracts:notification-enum"))

}

allOpen {
    annotation("team.aliens.dms.common.annotation.UseCase")
    annotation("team.aliens.dms.common.annotation.ReadOnlyUseCase")
    annotation("team.aliens.dms.common.annotation.SchedulerUseCase")
    annotation("team.aliens.dms.common.annotation.Service")
}

detekt {
    ignoreFailures = true
}
