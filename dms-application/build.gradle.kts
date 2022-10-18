plugins {
    kotlin("plugin.allopen") version PluginVersions.ALLOPEN_VERSION
}

dependencies {
    // impl project
    implementation(project(":dms-domain"))

    // transaction
    implementation(Dependencies.SPRING_TRANSACTION)
}

allOpen {
    annotation("team.aliens.dms.global.annotation.UseCase")
    annotation("team.aliens.dms.global.annotation.UseCase")
}