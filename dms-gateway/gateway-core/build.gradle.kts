dependencies {
    // Spring Cloud Gateway
    implementation(Dependencies.SPRING_CLOUD_GATEWAY)
}

tasks.getByName<Jar>("jar") {
    enabled = true
    archiveClassifier.set("")
}