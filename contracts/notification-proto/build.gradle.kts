
plugins {
    kotlin("jvm")
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
}
