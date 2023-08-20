import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.kapt) apply false
    alias(libs.plugins.detekt)
}

allprojects {

    apply(plugin = rootProject.project.libs.plugins.detekt.get().pluginId)
    apply(plugin = "base")

    dependencies {
        /**using rootProject.project instead of
         * simple libs because gradle don't fix problem with access
         * see: https://github.com/gradle/gradle/issues/16634#issuecomment-809345790
         **/
        detektPlugins(rootProject.project.libs.util.detektPlugin)
        detektPlugins(rootProject.project.libs.util.detektFormatting)
    }

    tasks.withType<Detekt>().configureEach {
        jvmTarget = JavaVersion.VERSION_17.toString()
        setSource(files("src"))
        exclude("**/androidTest/**")
        exclude("**/assets/**")
        exclude("**/res/**")
        exclude("**/test/**")
        exclude("**/test-ui-*/**")

    }
    tasks.withType<DetektCreateBaselineTask>().configureEach {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    detekt {
        toolVersion = rootProject.project.libs.versions.detekt.get()
        buildUponDefaultConfig = true
        autoCorrect = true
    }
}
tasks.register("projectClean", Delete::class) {
    delete(setOf(rootProject.buildDir))
}

true // Needed to make the Suppress annotation work for the plugins block
