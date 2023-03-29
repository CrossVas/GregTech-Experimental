pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            name = "MinecraftForge"
            url = uri("https://maven.minecraftforge.net/")
        }
        maven {
            name = "Garden of Fancy"
            url = uri("https://gitlab.com/api/v4/projects/26758973/packages/maven")
        }
    }
}

rootProject.name = "GregTech-Experimental"
