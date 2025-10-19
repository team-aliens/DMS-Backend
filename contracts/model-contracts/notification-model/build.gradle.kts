plugins {
    id("java-library")
}

dependencies {
    // contract
    implementation(project(":contracts:enum-contracts:notification-enum"))
}

tasks.named<JavaCompile>("compileJava") {
    mustRunAfter("compileKotlin")
}
