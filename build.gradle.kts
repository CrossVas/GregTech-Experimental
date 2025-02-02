import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseArtifact
import com.matthewprenger.cursegradle.CurseRelation
import fr.brouillard.oss.jgitver.GitVersionCalculator
import fr.brouillard.oss.jgitver.Strategies
import net.minecraftforge.gradle.common.util.RunConfig
import wtf.gofancy.fancygradle.script.extensions.curse
import wtf.gofancy.fancygradle.script.extensions.deobf
import java.time.LocalDateTime

buildscript {
    dependencies {
        classpath(group = "fr.brouillard.oss", name = "jgitver", version = "0.14.0")
    }
}

plugins {
    java
    `maven-publish`
    id("net.minecraftforge.gradle") version "5.1.+"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("wtf.gofancy.fancygradle") version "1.1.+"
    id("com.matthewprenger.cursegradle") version "1.4.+"
    id("com.modrinth.minotaur") version "2.+"
    id("wtf.gofancy.git-changelog") version "1.1.+"
}

val versionMc: String by project
val curseForgeId: String by project
val modrinthId: String by project
val modrinthVersionIC2: String by project
val versionIC2: String by project
val versionBuildCraft: String by project
val versionJEI: String by project
val versionCodeChickenLib: String by project
val versionCoFHCore: String by project
val versionCoFHWorld: String by project
val versionRF: String by project
val versionThermalFoundation: String by project
val versionThermalExpansion: String by project
val versionAE2: String by project
val versionMantle: String by project
val versionTConstruct: String by project
val versionEnergyControl: String by project
val versionRailcraft: String by project
val versionThaumcraft: String by project
val versionCraftTweaker: String by project

group = "mods.su5ed"
version = getGitVersion()
setProperty("archivesBaseName", "gregtechmod")

val api: SourceSet by sourceSets.creating
val apiDep: Configuration by configurations.creating

val shade: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

val manifestAttributes = mapOf(
    "Specification-Title" to project.name,
    "Specification-Vendor" to "Su5eD",
    "Specification-Version" to "1",
    "Implementation-Title" to project.name,
    "Implementation-Version" to project.version,
    "Implementation-Vendor" to "Su5eD",
    "Implementation-Timestamp" to LocalDateTime.now()
)
val publishReleaseType = System.getenv("PUBLISH_RELEASE_TYPE") ?: "beta"
val changelogText = changelog.generateChangelog(1, true)

minecraft {
    mappings("stable", "39-1.12")

    runs {
        val config = Action<RunConfig> {
            property("forge.logging.console.level", "debug")
            workingDirectory = project.file("run").canonicalPath
            source(sourceSets.main.get())
            forceExit = false
        }

        create("client", config)
        create("server", config)
    }
}

fancyGradle {
    patches {
        resources
        coremods
        codeChickenLib
        asm
        mergetool
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))

    withSourcesJar()
}

configurations {
    "apiImplementation" {
        extendsFrom(apiDep, configurations.minecraft.get())
    }

    apiElements {
        setExtendsFrom(setOf(apiDep, shade))
    }

    shadowRuntimeElements {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
}

val devJar by tasks.registering(ShadowJar::class) {
    dependsOn("classes", "apiClasses")

    configurations = listOf(shade)
    manifest.attributes(manifestAttributes)

    from(sourceSets.main.get().output)
    from(api.output)

    archiveClassifier.set("dev")
}

val apiJar by tasks.registering(Jar::class) {
    finalizedBy("reobfApiJar")

    from(api.allSource, api.output)
    exclude("META-INF/**")
    archiveClassifier.set("api")
}

val relocateShadowJar by tasks.registering(ConfigureShadowRelocation::class) {
    target = tasks.shadowJar.get()
    prefix = "mods.gregtechmod.repack"
}

tasks {
    jar {
        from(api.output)

        manifest.attributes(manifestAttributes)

        archiveClassifier.set("slim")
    }

    shadowJar {
        dependsOn(relocateShadowJar)
        finalizedBy("reobfShadowJar")

        configurations = listOf(shade)
        manifest.attributes(manifestAttributes)

        from(api.output)

        archiveClassifier.set("")
    }

    named<Jar>("sourcesJar") {
        from(api.allSource)
    }

    processResources {
        inputs.properties(
            "version" to project.version,
            "mcversion" to versionMc
        )

        filesMatching("mcmod.info") {
            expand(
                "version" to project.version,
                "mcversion" to versionMc
            )
        }
    }

    assemble {
        dependsOn(shadowJar, devJar, apiJar)
    }
}

reobf {
    create("shadowJar")
    create("apiJar")
}

repositories {
    exclusiveRepo("https://maven.ic2.player.to", "net.industrial-craft")
    exclusiveRepo("https://maven.covers1624.net", "cofh", "codechicken")
    exclusiveRepo("https://mod-buildcraft.com/maven", "com.mod-buildcraft")
    exclusiveRepo("https://cursemaven.com", "curse.maven")
    exclusiveRepo("https://maven.su5ed.dev/releases", "one.util")
    exclusiveRepo("https://maven.blamejared.com", "CraftTweaker2")
    exclusiveRepo("https://modmaven.dev", "mezz.jei", "slimeknights", "slimeknights.mantle")
    mavenCentral()
}

dependencies {
    minecraft(group = "net.minecraftforge", name = "forge", version = "1.12.2-14.23.5.2860")

    implementation(api.output)
    implementation(fg.deobf(curse(mod = "industrial-craft", projectId = 242638, fileId = 3838713)))
    compileOnly(fg.deobf(group = "cofh", name = "RedstoneFlux", version = versionRF, classifier = "universal"))
    compileOnly(fg.deobf(group = "cofh", name = "CoFHCore", version = versionCoFHCore, classifier = "universal")) {
        exclude(group = "mezz.jei")
    }
    compileOnly(fg.deobf(group = "cofh", name = "CoFHWorld", version = versionCoFHWorld, classifier = "universal"))
    compileOnly(fg.deobf(group = "cofh", name = "ThermalFoundation", version = versionThermalFoundation, classifier = "universal"))
    compileOnly(fg.deobf(group = "codechicken", name = "CodeChickenLib", version = versionCodeChickenLib, classifier = "universal"))
    compileOnly(fg.deobf(group = "cofh", name = "ThermalExpansion", version = versionThermalExpansion, classifier = "universal")) {
        exclude(group = "mezz.jei")
    }
    runtimeOnly(fg.deobf(group = "mezz.jei", name = "jei_$versionMc", version = versionJEI))
    compileOnly(group = "mezz.jei", name = "jei_$versionMc", version = versionJEI, classifier = "api")
    compileOnly(fg.deobf(group = "com.mod-buildcraft", name = "buildcraft-main", version = versionBuildCraft))
    implementation(fg.deobf(curse(mod = "energy-control", projectId = 373450, fileId = versionEnergyControl.toLong())))
    compileOnly(fg.deobf(curse(mod = "railcraft", projectId = 51195, fileId = versionRailcraft.toLong())))
    compileOnly(fg.deobf(curse(mod = "applied-energistics-2", projectId = 223794, fileId = versionAE2.toLong())))
    compileOnly(fg.deobf(curse(mod = "thaumcraft", projectId = 223628, fileId = versionThaumcraft.toLong())))
    compileOnly(fg.deobf(group = "slimeknights.mantle", name = "Mantle", version = versionMantle))
    compileOnly(fg.deobf(group = "slimeknights", name = "TConstruct", version = versionTConstruct))
    compileOnly(fg.deobf(group = "CraftTweaker2", name = "CraftTweaker2-MC1120-Main", version = "1.12-$versionCraftTweaker"))
    compileOnly(group = "CraftTweaker2", name = "CraftTweaker2-API", version = versionCraftTweaker)
    compileOnly(group = "CraftTweaker2", name = "ZenScript", version = versionCraftTweaker)

    apiDep(shade(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = "2.9.0"))
    shade(group = "com.fasterxml.jackson.dataformat", name = "jackson-dataformat-yaml", version = "2.9.0")
    apiDep(shade(group = "one.util", name = "streamex", version = "0.8.1"))
}

afterEvaluate {
    val component = components["java"] as AdhocComponentWithVariants
    component.withVariantsFromConfiguration(configurations.runtimeElements.get(), ConfigurationVariantDetails::skip)
}

curseforge {
    apiKey = System.getenv("CURSEFORGE_TOKEN") ?: "UNKNOWN"
    project(closureOf<CurseProject> {
        id = curseForgeId
        changelogType = "markdown"
        changelog = changelogText
        releaseType = publishReleaseType
        mainArtifact(tasks.shadowJar.get(), closureOf<CurseArtifact> {
            displayName = "GregTech Experimental ${project.version}"
            relations(closureOf<CurseRelation> {
                requiredDependency("industrial-craft")

                optionalDependency("jei")
                optionalDependency("thermal-expansion")
                optionalDependency("buildcraft")
                optionalDependency("energy-control")
                optionalDependency("railcraft")
                optionalDependency("applied-energistics-2")
                optionalDependency("thaumcraft")
                optionalDependency("tinkers-construct")
                optionalDependency("crafttweaker")
            })
        })
        addGameVersion("Forge")
        addGameVersion(versionMc)
    })
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set(modrinthId)
    versionName.set("GregTech Experimental ${project.version}")
    versionType.set(publishReleaseType)
    uploadFile.set(tasks.shadowJar.get())
    gameVersions.addAll(versionMc)
    dependencies {
        required.version("wTncj5gs", modrinthVersionIC2) // IC2
        optional.project("u6dRKJwZ") // JEI
        optional.project("rxIIYO6c") // Tinkers' Construct
    }
    changelog.set(changelogText)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = project.group as String
            artifactId = "gregtechmod"
            version = project.version as String

            from(components["java"])

            artifact(devJar)
            artifact(apiJar)
        }
    }

    repositories {
        maven {
            credentials {
                username = project.findProperty("MAVEN_USER") as String?
                password = project.findProperty("MAVEN_TOKEN") as String?
            }
            name = "Su5eD"
            url = uri("https://maven.su5ed.dev/releases")
        }
    }
}

fun getGitVersion(): String {
    val jgitver = GitVersionCalculator.location(rootDir)
        .setNonQualifierBranches("forge-1.12.2")
        .setStrategy(Strategies.SCRIPT)
        .setScript("print \"\${metadata.CURRENT_VERSION_MAJOR};\${metadata.CURRENT_VERSION_MINOR};\${metadata.CURRENT_VERSION_PATCH + metadata.COMMIT_DISTANCE}\"")
    return jgitver.version
}

// Adapted from https://gist.github.com/pupnewfster/6c21401789ca6d74f9892be8c1c505c9
fun RepositoryHandler.exclusiveRepo(location: String, vararg groups: String) {
    exclusiveRepo(location) {
        for (group in groups) {
            includeGroup(group)
        }
    }
}
fun RepositoryHandler.exclusiveRepo(location: String, config: Action<InclusiveRepositoryContentDescriptor>) {
    exclusiveContent {
        forRepositories(maven { url = uri(location) }, fg.repository)
        filter(config)
    }
}