plugins {
	id("org.springframework.boot") version PluginVersions.SPRING_BOOT_VERSION
	id("io.spring.dependency-management") version PluginVersions.DEPENDENCY_MANAGER_VERSION
	kotlin("plugin.spring") version PluginVersions.SPRING_PLUGIN_VERSION
}

dependencies {

	//core
	implementation(project(":dms-gateway:gateway-core"))

	// jwt
	implementation(Dependencies.JWT)
	runtimeOnly(Dependencies.JWT_IMPL)
	runtimeOnly(Dependencies.JWT_JACKSON)

	// Spring Cloud Gateway
	implementation(Dependencies.SPRING_CLOUD_GATEWAY)

	// validation
	implementation(Dependencies.SPRING_VALIDATION)

	// configuration
	kapt(Dependencies.CONFIGURATION_PROCESSOR)
}

tasks.bootJar {
	enabled = false
}