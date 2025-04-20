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
    implementation("net.dv8tion:JDA:5.3.1") {
        exclude(module="opus-java")
    }
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("com.google.code.gson:gson:2.12.1")
    implementation("commons-io:commons-io:2.14.0")
    compileOnly(project(":PotatoEssentials"))
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        mergeServiceFiles()
        archiveClassifier.set("")
    }
    compileJava {
        dependsOn(":PotatoEssentials:shadowJar")
    }
    processResources {
        filter<ReplaceTokens>("tokens" to mapOf(
            "version" to project.version.toString()))
    }
}