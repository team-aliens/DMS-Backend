import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.testing.jacoco.tasks.JacocoReport

plugins {
    kotlin("jvm") version PluginVersions.JVM_VERSION
    id("io.gitlab.arturbosch.detekt").version(PluginVersions.DETEKT_VERSION)
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        version = PluginVersions.JVM_VERSION
    }

    if (!project.path.contains(":contracts:")) {
        apply {
            plugin("org.jetbrains.kotlin.kapt")
            version = PluginVersions.KAPT_VERSION
        }
    }

    apply {
        plugin("io.gitlab.arturbosch.detekt")
        version = PluginVersions.DETEKT_VERSION
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    detekt {
        toolVersion = PluginVersions.DETEKT_VERSION
        buildUponDefaultConfig = true
        autoCorrect = true
        config = files("config/detekt/detekt.yml")
    }

    dependencies {

        // kotlin
        implementation(Dependencies.KOTLIN_REFLECT)
        implementation(Dependencies.KOTLIN_JDK)
        implementation(Dependencies.JACKSON)

        // java servlet
        if (!project.path.contains("dms-gateway")) {
            implementation(Dependencies.JAVA_SERVLET)
        }

        // test
        testImplementation(Dependencies.SPRING_TEST)
        testImplementation(Dependencies.MOCKK)
        testImplementation(Dependencies.SPRING_KOTEST)
        testImplementation(Dependencies.KOTEST)
        testImplementation(Dependencies.KOTEST_ASSERTIONS)

        detektPlugins(Dependencies.DETEKT)
    }
}

allprojects {
    group = "team.aliens"
    version = "0.0.1-SNAPSHOT"

    tasks {
        compileKotlin {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "17"
            }
        }

        compileTestKotlin {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    if (!project.path.contains(":contracts:")) {
        apply(plugin = "jacoco")

        tasks.withType<Test> {
            useJUnitPlatform()
            javaLauncher.set(
                javaToolchains.launcherFor {
                    languageVersion.set(JavaLanguageVersion.of(17))
                }
            )
            outputs.upToDateWhen { false }
            finalizedBy("jacocoTestReport")
        }

        tasks.named<org.gradle.testing.jacoco.tasks.JacocoReport>("jacocoTestReport") {
            dependsOn(tasks.named("test"))
            executionData.setFrom(fileTree(project.layout.buildDirectory.get().asFile).include("jacoco/*.exec"))

            val mainSourceSet = project.the<org.gradle.api.tasks.SourceSetContainer>().getByName("main")
            sourceDirectories.setFrom(mainSourceSet.allSource)

            classDirectories.setFrom(
                files(mainSourceSet.output.classesDirs.files.map { dir ->
                    fileTree(dir) {
                        exclude(
                            "**/global/config/**",
                            "**/thirdparty/**/config/**",
                            "**/scheduler/config/**",
                            "**/persistence/**/entity/**",
                            "**/*Application.class",
                            "**/*Application\$*.class",
                            "**/*Properties.class",
                            "**/*Properties\$*.class",
                            "**/stub/**"
                        )
                    }
                })
            )

            reports {
                xml.required.set(true)
                html.required.set(true)
                csv.required.set(false)
            }

            outputs.upToDateWhen { false }
        }
    }



    repositories {
        mavenCentral()
    }
}

tasks.register<JacocoReport>("jacocoSubReports") {
    group = "verification"
    description = "Generate Jacoco coverage report for all modules (excluding core modules with duplicate classes)"

    val jacocoProjects = subprojects.filter {
        it.pluginManager.hasPlugin("jacoco") &&
        !it.childProjects.any() &&
        !it.path.endsWith("-core")
    }

    dependsOn(jacocoProjects.map { it.tasks.named("test") })
    dependsOn(jacocoProjects.map { it.tasks.named<JacocoReport>("jacocoTestReport") })

    executionData.setFrom(
        jacocoProjects.map { project ->
            fileTree(project.layout.buildDirectory.get().asFile).include("jacoco/*.exec")
        }
    )

    val sourceSets = jacocoProjects.map {
        it.the<org.gradle.api.tasks.SourceSetContainer>().getByName("main")
    }

    sourceDirectories.setFrom(sourceSets.map { it.allSource.srcDirs }.flatten())

    classDirectories.setFrom(
        files(sourceSets.map { sourceSet ->
            sourceSet.output.classesDirs.files.map { dir ->
                fileTree(dir) {
                    exclude(
                        "**/global/config/**",
                        "**/thirdparty/**/config/**",
                        "**/scheduler/config/**",
                        "**/persistence/**/entity/**",
                        "**/*Application.class",
                        "**/*Application\$*.class",
                        "**/*Properties.class",
                        "**/*Properties\$*.class",
                        "**/stub/**"
                    )
                }
            }
        }.flatten())
    )

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)

        xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/test/jacocoTestReport.xml"))
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/test/html"))
    }
}

// dms-main 서비스 통합 리포트
tasks.register<JacocoReport>("jacocoMainServiceReport") {
    group = "verification"
    description = "Generate Jacoco coverage report for dms-main service"
    
    val mainProjects = subprojects.filter { 
        it.path.startsWith(":dms-main") && it.pluginManager.hasPlugin("jacoco")
    }

    dependsOn(mainProjects.map { it.tasks.named("test") })
    dependsOn(mainProjects.map { it.tasks.named<JacocoReport>("jacocoTestReport") })

    executionData.setFrom(
        mainProjects.map { project ->
            fileTree(project.layout.buildDirectory.get().asFile).include("jacoco/*.exec")
        }
    )

    val sourceSets = mainProjects.map {
        it.the<org.gradle.api.tasks.SourceSetContainer>().getByName("main")
    }

    sourceDirectories.setFrom(sourceSets.map { it.allSource.srcDirs }.flatten())

    classDirectories.setFrom(
        files(sourceSets.map { sourceSet ->
            sourceSet.output.classesDirs.files.map { dir ->
                fileTree(dir) {
                    exclude(
                        "**/global/config/**",
                        "**/thirdparty/**/config/**",
                        "**/scheduler/config/**",
                        "**/persistence/**/entity/**",
                        "**/*Application.class",
                        "**/*Application\$*.class",
                        "**/*Properties.class",
                        "**/*Properties\$*.class",
                        "**/stub/**"
                    )
                }
            }
        }.flatten())
    )

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)

        xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/main-service/jacocoTestReport.xml"))
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/main-service/html"))
    }
}

// dms-notification 서비스 통합 리포트
tasks.register<JacocoReport>("jacocoNotificationServiceReport") {
    group = "verification"
    description = "Generate Jacoco coverage report for dms-notification service"

    val notificationProjects = subprojects.filter {
        it.path.startsWith(":dms-notification") && it.pluginManager.hasPlugin("jacoco")
    }

    dependsOn(notificationProjects.map { it.tasks.named("test") })
    dependsOn(notificationProjects.map { it.tasks.named<JacocoReport>("jacocoTestReport") })

    onlyIf {
        executionData.files.any { it.exists() }
    }

    executionData.setFrom(
        notificationProjects.map { project ->
            fileTree(project.layout.buildDirectory.get().asFile).include("jacoco/*.exec")
        }
    )

    val sourceSets = notificationProjects.map {
        it.the<org.gradle.api.tasks.SourceSetContainer>().getByName("main")
    }

    sourceDirectories.setFrom(sourceSets.map { it.allSource.srcDirs }.flatten())

    classDirectories.setFrom(
        files(sourceSets.map { sourceSet ->
            sourceSet.output.classesDirs.files.map { dir ->
                fileTree(dir) {
                    exclude(
                        "**/global/config/**",
                        "**/thirdparty/**/config/**",
                        "**/scheduler/config/**",
                        "**/persistence/**/entity/**",
                        "**/*Application.class",
                        "**/*Application\$*.class",
                        "**/*Properties.class",
                        "**/*Properties\$*.class",
                        "**/stub/**"
                    )
                }
            }
        }.flatten())
    )

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)

        xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/notification-service/jacocoTestReport.xml"))
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/notification-service/html"))
    }
}

// dms-gateway 서비스 통합 리포트
tasks.register<JacocoReport>("jacocoGatewayServiceReport") {
    group = "verification"
    description = "Generate Jacoco coverage report for dms-gateway service"
    
    val gatewayProjects = subprojects.filter { 
        it.path.startsWith(":dms-gateway") && it.pluginManager.hasPlugin("jacoco")
    }

    dependsOn(gatewayProjects.map { it.tasks.named("test") })
    dependsOn(gatewayProjects.map { it.tasks.named<JacocoReport>("jacocoTestReport") })

    executionData.setFrom(
        gatewayProjects.map { project ->
            fileTree(project.layout.buildDirectory.get().asFile).include("jacoco/*.exec")
        }
    )

    val sourceSets = gatewayProjects.map {
        it.the<org.gradle.api.tasks.SourceSetContainer>().getByName("main")
    }

    sourceDirectories.setFrom(sourceSets.map { it.allSource.srcDirs }.flatten())

    classDirectories.setFrom(
        files(sourceSets.map { sourceSet ->
            sourceSet.output.classesDirs.files.map { dir ->
                fileTree(dir) {
                    exclude(
                        "**/global/config/**",
                        "**/thirdparty/**/config/**",
                        "**/scheduler/config/**",
                        "**/persistence/**/entity/**",
                        "**/*Application.class",
                        "**/*Application\$*.class",
                        "**/*Properties.class",
                        "**/*Properties\$*.class",
                        "**/stub/**"
                    )
                }
            }
        }.flatten())
    )

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)

        xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/gateway-service/jacocoTestReport.xml"))
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/gateway-service/html"))
    }
}

tasks.register("jacocoAllServiceReports") {
    group = "verification"
    description = "Generate Jacoco coverage reports for all services"

    dependsOn(
        "jacocoMainServiceReport",
        "jacocoNotificationServiceReport",
        "jacocoGatewayServiceReport"
    )

    doLast {
        println()
        println("========================================")
        println("Jacoco Reports Generated:")
        println("  - Main Service: build/reports/jacoco/main-service/html/index.html")
        println("  - Notification Service: build/reports/jacoco/notification-service/html/index.html")
        println("  - Gateway Service: build/reports/jacoco/gateway-service/html/index.html")
        println("========================================")
    }
}


tasks.register<Copy>("installGitHooks") {
    from(file("$rootDir/.githooks"))
    into(file("$rootDir/.git/hooks"))
    fileMode = "0775".toInt()
}