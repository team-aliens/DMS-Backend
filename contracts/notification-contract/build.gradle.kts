plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt")
}

dependencies {
    detektPlugins(Dependencies.DETEKT)
}

detekt {
    source.setFrom(files("src/main/kotlin/team/aliens/dms/contract/notification"))
    config.setFrom(files()) // Do not use any config file, rely on defaults
}

