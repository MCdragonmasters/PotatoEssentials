import io.papermc.paperweight.userdev.ReobfArtifactConfiguration
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    java
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18"
    `maven-publish`
}

group = "com.mcdragonmasters"
version = "1.0.0"

repositories {
    mavenCentral()
    // Paper
    maven("https://repo.papermc.io/repository/maven-public/")
    // VaultAPI
    maven("https://jitpack.io")
    // ConfigUpdater
    maven("https://oss.sonatype.org/content/groups/public")
}

dependencies {
    // Paper
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
    // Paper NMS
    paperweight.paperDevBundle("1.21.8-R0.1-SNAPSHOT")
    // VaultAPI
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    // Config-Updater
    implementation("com.tchristofferson:ConfigUpdater:2.2-SNAPSHOT")
    // CommandAPI
    implementation("dev.jorel:commandapi-bukkit-shade-mojang-mapped:10.1.2")
    // Commons Collections
    implementation("org.apache.commons:commons-collections4:4.5.0")
    // bStats
    implementation("org.bstats:bstats-bukkit:3.1.0")
}

paperweight {
    reobfArtifactConfiguration = ReobfArtifactConfiguration.MOJANG_PRODUCTION
}

publishing.publications.create<MavenPublication>("maven") {
    groupId = "com.mcdragonmasters"
    artifactId = "PotatoEssentials"
    version = project.version.toString()
    artifact(tasks.shadowJar)
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        archiveClassifier.set("")
        val libPrefix = "com.mcdragonmasters.potatoessentials.libs"
        relocate("dev.jorel.commandapi",
            "${libPrefix}.commandapi")
        relocate("com.tchristofferson.configupdater",
            "${libPrefix}.configupdater")
        relocate("org.bstats", "${libPrefix}.bstats")
    }
    withType<JavaCompile>().configureEach {
        options.compilerArgs.addAll(listOf("-source", "21", "-target", "21"))
        options.compilerArgs.addAll(listOf("-Xlint:unchecked", "-Xlint:deprecation"))
    }
    processResources {
        filter<ReplaceTokens>("tokens" to mapOf(
            "version" to project.version.toString()))
    }
    named("publishMavenPublicationToMavenLocal") {
        dependsOn(jar)
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

