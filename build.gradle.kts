import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.api.tasks.Copy
import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    java
    id("com.gradleup.shadow") version "8.3.3"
    id("io.freefair.lombok") version "8.12.1"
}

group = "com.mcdragonmasters"
version = "1.0.0"

repositories {
    mavenCentral()
    mavenLocal()
    // Paper
    maven("https://repo.papermc.io/repository/maven-public/")
    // VaultAPI
    maven("https://jitpack.io")
    // ConfigUpdater
    maven("https://oss.sonatype.org/content/groups/public")
}

dependencies {
    // Paper
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    // CommandAPI
    implementation("dev.jorel:commandapi-bukkit-shade-mojang-mapped:9.7.0")
    // VaultAPI
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    // Config-Updater
    implementation("com.tchristofferson:ConfigUpdater:2.2-SNAPSHOT")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.shadowJar {
    archiveClassifier.set(null as String?)
    relocate("dev.jorel.commandapi", "com.mcdragonmasters.potatoessentials.commandapi")
    relocate("com.tchristofferson.configupdater", "com.mcdragonmasters.potatoessentials.configupdater")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}
tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.addAll(listOf("-source", "21", "-target", "21"))
    options.compilerArgs.addAll(listOf("-Xlint:unchecked", "-Xlint:deprecation"))
}

tasks.register<Copy>("server") {
    from(tasks.shadowJar)
    into("\\\\192.168.1.46\\dev\\plugins") // Change this to wherever you want your jar to build
}

tasks.processResources {
    filter<ReplaceTokens>("tokens" to mapOf("version" to project.version.toString()))
}
