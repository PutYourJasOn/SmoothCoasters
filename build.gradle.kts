plugins {
    java
    id("fabric-loom")
    id("maven-publish")
}

group = "me.m56738"
version = "1.17-${System.getenv("BUILD_NUMBER") ?: "SNAPSHOT"}"

dependencies {
    minecraft("com.mojang:minecraft:1.17")
    mappings("net.fabricmc:yarn:1.17+build.1:v2")
    modImplementation("net.fabricmc:fabric-loader:0.11.3")
    modImplementation(fabricApi.module("fabric-networking-api-v1", "0.34.9+1.17"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand(Pair("version", project.version))
        }
    }
}
