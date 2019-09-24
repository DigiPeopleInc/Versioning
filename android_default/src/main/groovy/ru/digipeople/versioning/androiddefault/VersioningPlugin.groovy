package ru.digipeople.versioning.androiddefault

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.Plugin
import org.gradle.api.Project

class VersioningPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        // Add the 'versioning' extension object
        def versioning = project.extensions.create('versioning', VersioningExtension)

        // Get the 'android' extension object
        def android = project.getExtensions().getByName("android") as AppExtension

        // Apply modifications
        String versionName = ""
        android.getApplicationVariants().all { ApplicationVariant variant ->

            if (versionName.isEmpty()) {
                // Append buildNumber to versionName
                def buildNumber = getBuildNumber(project)
                versionName = "${android.defaultConfig.versionName}.${buildNumber}"
                // Set teamcity buildNumber
                println "##teamcity[buildNumber '${versionName}']"
            }


            def flavor = variant.flavorName
            def buildType = variant.buildType.name.capitalize()

            def versionNameSuffix = "$flavor$buildType"
            if (versionNameSuffix.endsWithAny("ProdRelease", "prodRelease")) {
                def end = versionNameSuffix.length() - "ProdRelease".length()
                versionNameSuffix = versionNameSuffix.substring(0, end)
            } else if (versionNameSuffix.endsWithAny("Release", "release")) {
                def end = versionNameSuffix.length() - "Release".length()
                versionNameSuffix = versionNameSuffix.substring(0, end)
            }

            def fullVersionName
            if (versionNameSuffix.isEmpty()) {
                fullVersionName = versionName
            } else {
                fullVersionName = "$versionName-$versionNameSuffix"
            }

            println "fullVersionName $fullVersionName"

            variant.buildConfigField('String', 'FULL_VERSION_NAME', "\"$fullVersionName\"")

            def fileSuffix = "${versionName}_$flavor$buildType"
            variant.outputs.all { output ->
                versionNameOverride = fullVersionName
                outputFileName = "${versioning.appName}_${fileSuffix}.apk"
            }
        }
    }

    String getBuildNumber(Project project) {
        if (project.hasProperty("build.shared_number")) {
            return project.ext["build.shared_number"]
        }
        if (project.hasProperty("build.number")) {
            return project.ext["build.number"]
        }
        return "0"
    }
}
