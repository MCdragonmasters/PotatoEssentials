import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("java")
}

group = "com.mcdragonmasters"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // JDA
    implementation("net.dv8tion:JDA:5.3.1") {
        exclude(module="opus-java")
    }
    // Gson
    implementation("com.google.code.gson:gson:2.12.1")
    // Commons
    implementation("commons-io:commons-io:2.14.0")
    // Paper
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")

    compileOnly(project(":PotatoEssentials"))
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        mergeServiceFiles()
        archiveClassifier.set("")
        relocate("net.dv8tion.jda", "com.mcdragonmasters.potatodiscordlink.jda")
    }
    compileJava {
        dependsOn(":PotatoEssentials:shadowJar")
    }
    processResources {
        filter<ReplaceTokens>("tokens" to mapOf(
            "version" to project.version.toString()))
    }
}