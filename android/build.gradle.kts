allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

val newBuildDir: Directory =
    rootProject.layout.buildDirectory
        .dir("../../build")
        .get()
rootProject.layout.buildDirectory.value(newBuildDir)

subprojects {
    val newSubprojectBuildDir: Directory = newBuildDir.dir(project.name)
    project.layout.buildDirectory.value(newSubprojectBuildDir)
}
subprojects {
    project.evaluationDependsOn(":app")
}

// Inyecta automaticamente el namespace en plugins nativos antiguos que no lo declaran
// (requerido por AGP 8+). Ej: flutter_inappwebview 5.x, isar_flutter_libs 3.0.5
fun org.gradle.api.Project.injectNamespaceIfMissing() {
    val androidExt = extensions.findByName("android") ?: return
    val currentNamespace = runCatching {
        androidExt.javaClass.getMethod("getNamespace").invoke(androidExt) as? String
    }.getOrNull()
    if (currentNamespace.isNullOrEmpty()) {
        runCatching {
            androidExt.javaClass
                .getMethod("setNamespace", String::class.java)
                .invoke(androidExt, group.toString())
        }
    }
}

subprojects {
    plugins.withId("com.android.application") { injectNamespaceIfMissing() }
    plugins.withId("com.android.library") { injectNamespaceIfMissing() }
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
