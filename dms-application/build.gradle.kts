dependencies {
    // impl project
    implementation(project(":dms-domain"))

    // transaction
    implementation(Dependencies.SPRING_TRANSACTION)
}