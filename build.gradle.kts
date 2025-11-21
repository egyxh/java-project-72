plugins {
    id("java")
    id("application")
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:5.6.3")
    implementation("org.slf4j:slf4j-simple:2.0.9")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("com.h2database:h2:2.2.224")
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("gg.jte:jte:3.1.9")
}

application {
    mainClass.set("hexlet.code.App")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    jar {
        manifest {
            attributes("Main-Class" to application.mainClass.get())
        }
    }

    val fatJar = register<Jar>("fatJar") {
        dependsOn.addAll(listOf("compileJava", "processResources"))
        archiveClassifier.set("standalone")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest {
            attributes("Main-Class" to application.mainClass.get())
        }
        val sourcesMain = sourceSets.main.get()
        val contents = configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it) } +
                sourcesMain.output
        from(contents)
    }

    build {
        dependsOn(fatJar)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
