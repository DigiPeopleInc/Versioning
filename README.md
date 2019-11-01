# Versioning
Gradle versioning plugin  
Now there is only "android-default" plugin.

Plugin goes through application variants and
- Creates *BuildConfig.FULL_VERSION_NAME constant*
- Overrides variant *versionName*
- Overrides variant *outputFileName*

Full mask of created version mame is **{versionName}.{buildNumber}-{flavor}{buildType}**  
In case if full version name ens with *"Prod"* or *"ProdRelese"* those suffixes will be omitten.

Full mask of .apk file is **{versioning.appName}\_{versionName}.{buildNumber}\_{flavor}{buildType}.apk**

Build number is extracted from project properties "*build.shared_counter*" or "*build.counter*"  
Default value is *0*.

###### Example:
Assuming there are 
- two flavors *"Dev"* and *"Prod"*
- two build types *"Debug"* and *"Release"*
- versioning.appName = bestApp
- version name = 1.0.0
- project["build.number"] = 0
So there will be theese variants:
```
fileName: bestApp_1.0.0.0_DevDebug.apk
versionName: 1.0.0.0-DevDebug
```
```
fileName: bestApp_1.0.0.0_DevRelease.apk
versionName: 1.0.0.0-Dev
```
```
fileName: bestApp_1.0.0.0_ProdDebug.apk
versionName: 1.0.0.0-Debug
```
```
fileName: bestApp_1.0.0.0_ProdRelease.apk
versionName: 1.0.0.0
```
Usage
--------
```groovy
// Other plugins
apply plugin: 'ru.digipeople.versioning.androiddefault'

versioning {
    appName = "bestApp"
}

android {
    // Android config
}
```


Download
------------
```groovy
buildscript {
    repositories {
        // Other repositories
        maven { url 'https://dl.bintray.com/digipeopleinc/maven' }
    }

    dependencies {
        // Other dependencies
        classpath 'ru.digipeople.versioning:android-default:1.0.1'
    }
}

```