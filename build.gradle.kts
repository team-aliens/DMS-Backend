import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    kotlin("jvm") version PluginVersions.JVM_VERSION
    id("io.gitlab.arturbosch.detekt").version(PluginVersions.DETEKT_VERSION)
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        version = PluginVersions.JVM_VERSION
    }

    apply {
        plugin("org.jetbrains.kotlin.kapt")
        version = PluginVersions.KAPT_VERSION
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

    apply(plugin = "jacoco")

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

    tasks.withType<Test> {
        useJUnitPlatform()
        javaLauncher.set(
            javaToolchains.launcherFor {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        )
    }

    repositories {
        mavenCentral()
    }
}

tasks.register<JacocoReport>("jacocoRootReport") {
    subprojects {
        this@subprojects.plugins.withType<JacocoPlugin>().configureEach {
            this@subprojects.tasks.matching {
                it.extensions.findByType<JacocoTaskExtension>() != null
            }
                .configureEach {
                    sourceSets(this@subprojects.the<SourceSetContainer>().named("main").get())
                    executionData.setFrom(
                        executionData.files.filter { it.exists() }
                    )
                }
        }
    }

    reports {
        xml.outputLocation.set(File("${buildDir}/reports/jacoco/test/jacocoTestReport.xml"))
        xml.required.set(true)
        html.required.set(false)
    }
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

tasks.register<Copy>("installGitHooks") {
    from(file("$rootDir/.githooks"))
    into(file("$rootDir/.git/hooks"))
    fileMode = "0775".toInt()
}