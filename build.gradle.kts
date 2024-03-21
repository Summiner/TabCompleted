import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

group = "rs.jamie"
version = "1.0.0"

plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    `java-library`
    `maven-publish`
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }

    maven {
        url = uri("https://repo.codemc.io/repository/maven-releases/")
    }

    maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
}


configurations.named("shadow") {
    extendsFrom(configurations.getByName("implementation"))
}

dependencies {
    shadow("com.github.retrooper.packetevents:spigot:2.2.0") // Packets
    shadow("space.arim.dazzleconf:dazzleconf-ext-snakeyaml:1.2.1")
    shadow("org.yaml:snakeyaml:1.28")
    compileOnly("me.clip:placeholderapi:2.11.5")
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("plugin.yml") {
            expand(getProperties())
            expand(mutableMapOf("version" to project.version))
        }
    }
    named<ShadowJar>("shadowJar") {
        configurations = listOf(project.configurations.getByName("shadow"))
        archiveFileName.set("TabCompleted.jar")
        mergeServiceFiles()
        relocate("dev.dejvokep.boostedyaml", "rs.jamie.tabcompleted.libs.boostedyaml")
        relocate("com.github.retrooper", "rs.jamie.tabcompleted.libs.retrooper.com")
        relocate("io.github.retrooper", "rs.jamie.tabcompleted.libs.retrooper.io")
        relocate("space.arim.dazzleconf", "rs.jamie.tabcompleted.libs.dazzleconf")
        relocate("org.yaml.snakeyaml", "rs.jamie.tabcompleted.libs.snakeyaml")
        dependencies {
            exclude("net.kyori")
        }
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
