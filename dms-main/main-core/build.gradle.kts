plugins {
    kotlin("plugin.allopen") version PluginVersions.ALLOPEN_VERSION
}

dependencies {

    // transaction
    implementation(Dependencies.SPRING_TRANSACTION)

    // aop
    implementation(Dependencies.AOP)

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