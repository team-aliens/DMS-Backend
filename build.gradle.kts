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
            classDirectories.setFrom(project.the<org.gradle.api.tasks.SourceSetContainer>().getByName("main").output.classesDirs)
            sourceDirectories.setFrom(project.the<org.gradle.api.tasks.SourceSetContainer>().getByName("main").allSource)

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

// 전체 프로젝트 통합 리포트 (중복 클래스 문제로 인해 서비스별 리포트 사용 권장)
tasks.register<JacocoReport>("jacocoSubReports") {
    group = "verification"
    description = "Generate Jacoco coverage report for all modules (excluding core modules with duplicate classes)"

    // core 모듈을 제외한 모듈만 포함 (중복 클래스 방지)
    val jacocoProjects = subprojects.filter {
        it.pluginManager.hasPlugin("jacoco") &&
        !it.childProjects.any() && // 하위 프로젝트가 없는 리프 모듈만
        !it.path.endsWith("-core") // core 모듈 제외
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
    classDirectories.setFrom(sourceSets.map { it.output.classesDirs }.flatten())

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
    classDirectories.setFrom(sourceSets.map { it.output.classesDirs }.flatten())

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

    // 테스트가 없어도 리포트 생성
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
    classDirectories.setFrom(sourceSets.map { it.output.classesDirs }.flatten())

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
    classDirectories.setFrom(sourceSets.map { it.output.classesDirs }.flatten())

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)

        xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/gateway-service/jacocoTestReport.xml"))
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/gateway-service/html"))
    }
}

// 모든 서비스 리포트 한번에 생성
tasks.register("jacocoAllServiceReports") {
    group = "verification"
    description = "Generate Jacoco coverage reports for all services"
    
    dependsOn(
        "jacocoMainServiceReport",
        "jacocoNotificationServiceReport", 
        "jacocoGatewayServiceReport"
    )
}


tasks.register<Copy>("installGitHooks") {
    from(file("$rootDir/.githooks"))
    into(file("$rootDir/.git/hooks"))
    fileMode = "0775".toInt()
}