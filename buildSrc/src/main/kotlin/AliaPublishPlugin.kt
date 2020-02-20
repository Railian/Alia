import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.jfrog.bintray.gradle.BintrayExtension
import groovy.util.Node
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ExcludeRule
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.JavadocMemberLevel
import org.gradle.external.javadoc.StandardJavadocDocletOptions
import org.gradle.kotlin.dsl.*

open class AliaPublishPluginExtension {
    var groupId: String? = null
    var artifactId: String? = null
    var version: String? = null
    val bintray = AliaBintrayExtension()
}

fun AliaPublishPluginExtension.bintray(conf: AliaBintrayExtension.() -> Unit) = conf(bintray)

open class AliaBintrayExtension {
    var user: String? = null
    var key: String? = null
    var override: Boolean? = null
    var publish: Boolean? = null
    var repositoryName: String? = null
    var packageName: String? = null
}

class AliaPublishPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {

        val extension = extensions.create<AliaPublishPluginExtension>("aliaPublishing")

        //region setup sources and Java Docs
        if (isAndroidProject) {

            val android = the<LibraryExtension>()

            tasks.create<Javadoc>("androidJavadocs") {
                title = "${project.name} ${project.version} API"
                description = "Generates Javadoc"
                source(android.sourceSets["main"].java.srcDirs)
                classpath += files(android.bootClasspath)

                android.libraryVariants.all {
                    if (name == "release") classpath += javaCompileProvider.get().classpath
                }

                exclude("**/R.html", "**/R.*.html", "**/index.html", "**/*.kt")

                options {
                    windowTitle = "${project.name} ${project.version} API"
                    locale = "en_US"
                    encoding = "UTF-8"

                    (this as? StandardJavadocDocletOptions)?.apply {
                        charSet = "UTF_8"
                        links("http://docs.oracle.com/javase/7/docs/api/")
                        linksOffline(
                            "http://d.android.com/reference",
                            "${android.sdkDirectory}/docs/reference"
                        )
                    }

                    memberLevel = JavadocMemberLevel.PUBLIC
                }
            }

            tasks.create<Jar>("androidJavadocsJar") {
                dependsOn("androidJavadocs")
                from(tasks["androidJavadocs"])
                archiveClassifier.set("javadoc")
            }

            tasks.create<Jar>("androidSourcesJar") {
                from(android.sourceSets["main"].java.srcDirs)
                archiveClassifier.set("sources")
            }

        } else {

            val sourceSets = the<SourceSetContainer>()

            tasks.create<Jar>("javadocJar") {
                from(tasks["javadoc"])
                archiveClassifier.set("javadoc")
            }

            tasks.create<Jar>("sourcesJar") {
                from(sourceSets["main"].allJava)
                archiveClassifier.set("sources")
            }

        }
        //endregion

        //region configure publishing
        apply(plugin = "maven-publish")

        configure<PublishingExtension> {
            publications {
                create<MavenPublication>(project.name) {
                    afterEvaluate {
                        groupId = extension.groupId ?: project.group.toString()
                        artifactId = extension.artifactId ?: project.name
                        version = extension.version ?: project.version.toString()
                        if (isAndroidProject) {
                            artifact(tasks["bundleReleaseAar"])
                            artifact(tasks["androidJavadocsJar"])
                            artifact(tasks["androidSourcesJar"])
                            pom.decorateAsAndroidProject(project)
                        } else {
                            from(components["kotlin"])
                            artifact(tasks["sourcesJar"])
                            artifact(tasks["javadocJar"])
                        }
                    }
                }
            }
        }
        //endregion

        //region configure bintray
        apply(plugin = "com.jfrog.bintray")

        configure<BintrayExtension> {
            afterEvaluate {
                val localProperties = gradleLocalProperties(project.rootDir)
                user = extension.bintray.user ?: localProperties.getProperty("bintray.user")
                key = extension.bintray.key ?: localProperties.getProperty("bintray.key")
                setPublications(project.name)
                override = extension.bintray.override ?: true
                publish = extension.bintray.publish ?: true
                pkg.apply {
                    repo = extension.bintray.repositoryName ?: rootProject.name
                    name = extension.bintray.packageName ?: project.name
                    version.name = extension.version ?: project.version.toString()
                }
            }
        }
        //endregion

        Unit
    }

}

private val Project.isAndroidProject: Boolean
    get() = plugins.hasPlugin("com.android.application") ||
            plugins.hasPlugin("com.android.library")

private fun MavenPom.decorateAsAndroidProject(project: Project) {
    addDependencies(project.configurations, this)
}

private fun manageConfigurations(
    configurations: ConfigurationContainer,
    addDependency: (dep: Dependency, scope: String) -> Unit
) {
    fun findConfiguration(name: String) = configurations.findByName(name)
    findConfiguration("compile")?.dependencies?.forEach { addDependency(it, "compile") }
    findConfiguration("api")?.dependencies?.forEach { addDependency(it, "compile") }
    findConfiguration("implementation")?.dependencies?.forEach { addDependency(it, "runtime") }
    findConfiguration("testImplementation")?.dependencies?.forEach { addDependency(it, "test") }
    findConfiguration("testCompile")?.dependencies?.forEach { addDependency(it, "test") }
}

private fun addDependencies(
    configurations: ConfigurationContainer,
    pom: MavenPom
) = pom.withXml {
    val dependenciesNode = asNode().appendNode("dependencies")
    manageConfigurations(configurations) { dependency: Dependency, scope: String ->
        val isDependencySpecified = dependency.name != "unspecified" &&
                dependency.group != null && dependency.version != null
        if (isDependencySpecified && dependency is ModuleDependency) {
            dependency.artifacts.takeIf { it.isNotEmpty() }?.forEach {
                addDependencyNode(
                    dependenciesNode,
                    dependency,
                    scope,
                    it.classifier,
                    it.extension
                )
            } ?: addDependencyNode(dependenciesNode, dependency, scope)
        }
    }
}

private fun addDependencyNode(
    dependenciesNode: Node,
    dependency: Dependency,
    scope: String,
    classifier: String? = null,
    extension: String? = null
) {
    dependenciesNode
        .appendNode("dependency").apply {
            appendNode("groupId", dependency.group)
            appendNode("artifactId", dependency.name)
            appendNode("version", dependency.version)
            appendNode("scope", scope)
            classifier?.let { appendNode("classifier", it) }
            extension?.let { appendNode("type", it) }

            if (dependency is ModuleDependency) when {
                dependency.isTransitive -> {
                    // If this dependency is transitive, we should force exclude
                    // all its dependencies them from the POM
                    appendNode("exclusions")
                        .appendNode("exclusion").apply {
                            appendNode("artifactId", "*")
                            appendNode("groupId", "*")
                        }
                }
                dependency.excludeRules.isNotEmpty() -> {
                    // Otherwise add specified exclude rules
                    appendNode("exclusions").apply {
                        dependency.excludeRules.forEach { rule: ExcludeRule ->
                            appendNode("exclusion").apply {
                                appendNode("artifactId", rule.module ?: "*")
                                appendNode("groupId", rule.group ?: "*")
                            }
                        }
                    }
                }
            }
        }
}