import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar.Companion.shadowJar

plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.21"
    id("com.gradleup.shadow") version "9.4.1"
    id("xyz.jpenilla.run-paper") version "3.0.2"
    `maven-publish`
}

group = "de.minebench"
version = buildString {
    append(project.properties["plugin_version"])

    if (project.properties["is_snapshot"].toString().toBoolean()) {
        append("-Snapshot")
    }
}
description = "SyncInv"

java {
    // Configure the java toolchain. This allows gradle to auto-provision JDK 25 on systems that only have JDK 8 installed for example.
    toolchain.languageVersion.set(JavaLanguageVersion.of("${project.properties["java_version"]}"))
    sourceCompatibility = JavaVersion.toVersion(project.properties["java_version"]!!)
}

repositories {
    mavenLocal()

    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://repo.minebench.de/")
    }

    maven {
        url = uri("https://maven.greensurvivors.de/releases")
    }
}

dependencies {
    paperweight.paperDevBundle("${project.properties["minecraft_version"]}.build.+")

    compileOnly("org.projectlombok:lombok:${project.properties["lombok_version"]}")
    annotationProcessor("org.projectlombok:lombok:${project.properties["lombok_version"]}")

    api("io.lettuce:lettuce-core:${project.properties["lettuce_version"]}") {
        // included in server
        exclude("io.netty")
    }
    compileOnly("com.lishid:openinvplugin:${project.properties["openInv_version"]}")
    compileOnly("de.greensurvivors:GreenSocket:+")
    compileOnly("de.greensurvivors:Dienstmodus:+")
}

// include the unfiltered resources
sourceSets {
    main {
        resources {
            srcDirs(
                "src/main/unfiltered-resources"
            )
        }
    }
}

tasks {
    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything

        expand("project" to project,
            "minecraft_version" to project.properties["minecraft_version"]!!)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything

        // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
        // See https://openjdk.java.net/jeps/247 for more information.
        options.release.set(JavaLanguageVersion.of("${project.properties["java_version"]}").asInt())
    }

    // disable in favour of shadowJar
    jar {
        enabled = false
    }

    shadowJar {
        archiveClassifier = ""

        relocate ("io.lettuce", "de.minebench.syncinv.lib.lettuce")
        relocate ("reactor", "de.minebench.syncinv.lib.reactor")
        relocate ("org.reactivestreams", "de.minebench.syncinv.lib.reactivestreams")

        minimize()
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }

    runServer {
        //javaLauncher = javaToolchains. {}

        pluginJars.from(
            configurations.runtimeClasspath.map { configuration ->
                configuration.files.filter { file ->
                    file.name.startsWith("GreenSocket", ignoreCase = true) ||
                    file.name.startsWith("Dienstmodus", ignoreCase = true)
                }
            }
        )

        // disable bstats, as it isn't needed for dev environment
        doFirst { // this happens after downloading the plugins above, but before the server starts
            val bStatsCfg = runDirectory.get().asFile.resolve("plugins/bStats/config.yml")
            if (!bStatsCfg.exists()) {
                bStatsCfg.parentFile.mkdirs()
                bStatsCfg.createNewFile()
            }
            bStatsCfg.writeText("enabled: false\n")
        }
        // automatically agree to eula
        jvmArgs("-Dcom.mojang.eula.agree=true")
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["shadow"])
    }
}

