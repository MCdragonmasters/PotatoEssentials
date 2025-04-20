import io.papermc.paperweight.userdev.ReobfArtifactConfiguration
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    java
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.14"
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
    // Paper NMS
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
    // VaultAPI
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    // Config-Updater
    implementation("com.tchristofferson:ConfigUpdater:2.2-SNAPSHOT")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
paperweight {
    reobfArtifactConfiguration = ReobfArtifactConfiguration.MOJANG_PRODUCTION
}
tasks.shadowJar {
    archiveClassifier.set(null as String?)
    relocate("dev.jorel.commandapi",
        "com.mcdragonmasters.potatoessentials.libs.commandapi")
    relocate("com.tchristofferson.configupdater",
        "com.mcdragonmasters.potatoessentials.libs.configupdater")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}
tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.addAll(listOf("-source", "21", "-target", "21"))
    options.compilerArgs.addAll(listOf("-Xlint:unchecked", "-Xlint:deprecation"))
}

tasks.processResources {
    filter<ReplaceTokens>("tokens" to mapOf(
        "version" to project.version.toString()))
}
