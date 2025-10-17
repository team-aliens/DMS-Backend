import com.google.protobuf.gradle.*

plugins {
    kotlin("jvm")
    id("com.google.protobuf") version PluginVersions.GRPC_PLUGIN_VERSION
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.21.12"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.50.0"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                create("grpc")
            }
            task.builtins {
                maybeCreate("java").apply {
                    option("annotate_code=false")
                }
            }
        }
    }
}

dependencies {
    implementation(Dependencies.GRPC_STUB)
    implementation(Dependencies.GRPC_PROTOBUF)

    implementation("javax.annotation:javax.annotation-api:1.3.2")
}

sourceSets {
    main {
        java {
            srcDirs(
                file("$buildDir/generated/source/proto/main/java"),
                file("$buildDir/generated/source/proto/main/grpc")
            )
        }
    }
}

