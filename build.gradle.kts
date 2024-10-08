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
        implementation(Dependencies.JAVA_SERVLET)

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

        compileJava {
            sourceCompatibility = JavaVersion.VERSION_17.majorVersion

            options.isFork = true
        }

        test {
            useJUnitPlatform()
        }
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
                    executionData(this)
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