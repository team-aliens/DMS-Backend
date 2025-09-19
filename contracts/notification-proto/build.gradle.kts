
plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt")
    id("com.google.protobuf") version PluginVersions.GRPC_PLUGIN_VERSION
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.1"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.64.0"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                create("grpc")
            }
        }
    }
}

dependencies {
    implementation(Dependencies.GRPC_STUB)
    implementation(Dependencies.GRPC_PROTOBUF)
    implementation(Dependencies.GRPC_NETTY_SHADED)
}

