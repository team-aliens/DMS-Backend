plugins {
	id("org.springframework.boot") version PluginVersions.SPRING_BOOT_VERSION
	id("io.spring.dependency-management") version PluginVersions.DEPENDENCY_MANAGER_VERSION
	kotlin("plugin.spring") version PluginVersions.SPRING_PLUGIN_VERSION
}

dependencies {

	// Spring Cloud Gateway
	implementation(Dependencies.SPRING_CLOUD_GATEWAY)

	// Spring WebFlux
	implementation(Dependencies.WEB_FLUX)

	// configuration
	kapt(Dependencies.CONFIGURATION_PROCESSOR)
}

tasks.getByName<Jar>("jar") {
	enabled = false
}
