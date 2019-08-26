import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {

    // Add assignments like below, to overwrite values,
    // which are configured in "versions.gradle.kts", but only inside current root project and it's subprojects.
    
	// https://mvnrepository.com/artifact/com.github.mvysny.karibudsl/karibu-dsl-v10
    rootProject.extra["karibudsl10Version"] = "0.7.0"
	
	// https://mvnrepository.com/artifact/com.vaadin/vaadin-spring-boot-starter
    rootProject.extra["springBootStarterVaadinVersion"] = "14.0.1"

	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot
    rootProject.extra["spring_bootVersion"] = "2.1.7.RELEASE"

	// https://mvnrepository.com/artifact/javax.xml.bind/jaxb-api
    rootProject.extra["jaxbapiVersion"] = "2.3.1"
	
	// https://mvnrepository.com/artifact/com.fasterxml.jackson.module/jackson-module-kotlin
    rootProject.extra["jacksonModule_kotlinVersion"] = "2.9.9"
	
	// https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
    rootProject.extra["logbackVersion"] = "1.2.3"
	
	// https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api
    rootProject.extra["junit5Version"] = "5.5.1"
	
	// https://mvnrepository.com/artifact/org.amshove.kluent/kluent
    rootProject.extra["kluent_Version"] = "1.49"

    // https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-css
    // https://bintray.com/kotlin/kotlin-js-wrappers/kotlin-css
    rootProject.extra["kotlinCssVersion"] = "1.0.0-pre.82-kotlin-1.3.41"
}

plugins {

	// https://plugins.gradle.org/plugin/com.github.ben-manes.versions
	id("com.github.ben-manes.versions") version "0.22.0"

	// https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-gradle-plugin
    kotlin("jvm") version "1.3.41"
	
	java
	application
	
    id("org.springframework.boot") version "2.1.7.RELEASE"
}


repositories {

	jcenter()
	mavenLocal()
	mavenCentral()

	maven {
		url = uri("https://plugins.gradle.org/m2/")
	}
	maven {
		url = uri("https://kotlin.bintray.com/kotlin-js-wrappers")
	}

}

dependencies {

	compile(kotlin("stdlib-jdk8"))
	implementation(kotlin("stdlib-jdk7"))
	implementation(kotlin("reflect"))

	compile("com.fasterxml.jackson.module:jackson-module-kotlin:${rootProject.extra["jacksonModule_kotlinVersion"]}")

	testImplementation(kotlin("test"))
	testImplementation(kotlin("test-junit"))

	implementation("javax.xml.bind:jaxb-api:${rootProject.extra["jaxbapiVersion"]}")

	compile("ch.qos.logback:logback-classic:${rootProject.extra["logbackVersion"]}")

	setOf(
			"org.junit.jupiter:junit-jupiter-api" to "junit5Version",
			"org.amshove.kluent:kluent" to "kluent_Version"
	).forEach {
		testImplementation("${it.first}:${rootProject.extra[it.second]}")
	}

//	setOf(
//			"org.junit.jupiter:junit-jupiter-api:${rootProject.extra["junit5Version"]}",
//			"org.amshove.kluent:kluent:${rootProject.extra["kluent_Version"]}"
//	).forEach {
//		testImplementation(it)
//	}

//	testImplementation("org.junit.jupiter:junit-jupiter-api:${rootProject.extra["junit5Version"]}")
//	testImplementation("org.amshove.kluent:kluent:${rootProject.extra["kluent_Version"]}")

	implementation("org.jetbrains:kotlin-css-jvm:${rootProject.extra["kotlinCssVersion"]}")

	// implementation: spring core specific features, depend on spring core framework
    implementation("org.springframework.boot:spring-boot-starter:${rootProject.extra["spring_bootVersion"]}")

    implementation("com.github.mvysny.karibudsl:karibu-dsl-v10:${rootProject.extra["karibudsl10Version"]}") {
        exclude("javax.servlet", "javax.servlet-api")
    }

    // implementation: Vaadin specific features, depend on vaadin flow framework
    // Tomcat by default
    implementation("com.vaadin:vaadin-spring-boot-starter:${rootProject.extra["springBootStarterVaadinVersion"]}") {
        exclude("javax.servlet", "javax.servlet-api")
    }

}

java {
	sourceCompatibility = JavaVersion.VERSION_12
	targetCompatibility = JavaVersion.VERSION_12
}

tasks.withType<KotlinCompile>().all {
	kotlinOptions {
		jvmTarget = "12"
		// freeCompilerArgs = listOf("-Xjsr305=strict")
	}
}

group = "vlfsoft"
version = 1.0 // -> vlfsoft.module-1.0.jar

val appPackage = "issue0005"

application {
    mainClassName = "vlfsoft.$appPackage.ApplicationKt"
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = application.mainClassName
        attributes["Automatic-Module-Name"] = "vlfsoft.$appPackage.app"
    }
}

tasks {

	// https://stackoverflow.com/questions/31405818/want-to-specify-jar-name-and-version-both-in-build-gradle
	// https://github.com/barlog-m/spring-boot-2-example-app/blob/master/build.gradle.kts
	// https://stackoverflow.com/questions/55575264/creating-a-fat-jar-in-gradle-with-kotlindsl
    bootJar {
        // defaults to project.name
        archiveBaseName.set("vlfsoft.$appPackage.app")

        // defaults to all, so removing this overrides the normal, non-fat jar
        // archiveClassifier.set("")
    }
}


