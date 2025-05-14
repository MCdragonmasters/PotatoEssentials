plugins {
    java
    id("com.gradleup.shadow") version "8.3.3"
    id("io.freefair.lombok") version "8.12.1"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}
repositories {
    mavenCentral()
}

tasks {
    register<Copy>("serverDevelopment") {
        from(project(":PotatoEssentials").tasks.shadowJar, project(":PotatoDiscordLink").tasks.shadowJar)
        into("\\\\192.168.1.46\\dev\\plugins") // Change this to wherever you want your jar to build
    }
    register<Copy>("serverProduction") {
        from(project(":PotatoEssentials").tasks.shadowJar, project(":PotatoDiscordLink").tasks.shadowJar)
        into("\\\\192.168.1.46\\event\\plugins")
    }
    jar.configure {
        enabled = false
    }
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.21.4")
        pluginJars.from(project(":PotatoEssentials").tasks.shadowJar, project(":PotatoDiscordLink").tasks.shadowJar)
    }
}

lombok.disableConfig = true

subprojects {
    apply(plugin = "java")
    apply(plugin = "com.gradleup.shadow")
    apply(plugin = "io.freefair.lombok")
    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    dependencies {
        // Paper
        compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    }
}
