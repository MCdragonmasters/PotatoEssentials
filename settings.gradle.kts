rootProject.name = "PotatoEssentialsMain"
setupSubproject("PotatoEssentials")
setupSubproject("PotatoDiscordLink")

fun setupSubproject(moduleName: String) {
    include(moduleName)
    val proj = project(":$moduleName")
    proj.name = moduleName
    proj.projectDir = file(moduleName)
}