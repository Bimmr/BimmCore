plugins {
    id("java")
}

group = "me.bimmr"
version = "1.1.1"

configurations.all {
    isTransitive = false
}

repositories {
    mavenCentral()
    mavenLocal()

    //Spigot
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    //Mojang
    maven("https://libraries.minecraft.net/")

    //Can't remember
    maven("https://repo.dmulloy2.net/nexus/repository/public/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.codemc.org/repository/maven-public/")

    //Vault
    maven("https://jitpack.io")
}

dependencies {

    //Libraries
    compileOnly("com.google.guava", "guava", "31.1-jre")
    compileOnly("com.google.code.gson", "gson", "2.10.1")
    compileOnly("io.netty", "netty-all", "5.0.0.Alpha2")
    compileOnly("org.yaml", "snakeyaml", "2.0")
    compileOnly("org.netbeans.external", "org-apache-commons-lang", "RELEASE113")

    //Spigot
    compileOnly("org.spigotmc","spigot-api","1.19.4-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc","spigot","1.19.4-R0.1-SNAPSHOT")
    compileOnly("net.md-5","bungeecord-chat","1.19-R0.1-SNAPSHOT")
    //Mojang
    compileOnly("com.mojang", "authlib", "1.5.21")
    compileOnly("com.mojang", "datafixerupper", "1.0.20")

    //HikariCP
    implementation("com.zaxxer","HikariCP","5.0.1")

    //Quality Armory
//    compileOnly("me.zombie_striker:QualityArmory:1.1.168")

    compileOnly("com.comphenix.protocol", "ProtocolLib", "4.8.0")

    //Vault
    compileOnly("com.github.MilkBowl","VaultAPI","1.7") {
        exclude("org.bukkit", "bukkit")
    }

    //Local Files
    compileOnly(files("../../Spigot Dependencies/CrackShot.jar"))
    compileOnly(files("../../Spigot Dependencies/MassiveCore.jar"))
    compileOnly(files("../../Spigot Dependencies/Factions.jar"))
    compileOnly(files("../../Spigot Dependencies/QualityArmory.jar"))
    compileOnly(files("../../Buildtools/craftbukkit-1.19.4.jar"))
    compileOnly(files("../../Buildtools/spigot-1.19.4.jar"))
}

tasks {
    processResources {
        filesMatching("plugin.yml") { expand("version" to project.version) }
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    register<Copy>("copyJar") {
        dependsOn("build")
        from(layout.buildDirectory.dir("libs"))
        into(layout.buildDirectory.dir("../../../Outputs"))
    }

}