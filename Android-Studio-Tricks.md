### General Keyboard Helpers:
- `ctrl + p` inside fn will suggest all the params
- `ctrl + space` will suggest
- `ctrl + o` override/implement fn suggestion in side class
- typing `this.` + `ctrl + space` inside a lambda block (scope) will suggest more precisely
- `alt + enter` to import
### Compose:
- typing `wc` in editor for column template
- Tooling : https://developer.android.com/jetpack/compose/tooling#editor-actions

### Jump Start Unknown/Other's Project:
Visual Debugging: 
=> Use layout Inspector with live emulator's update
=> Analyze the app inside emulator and layout inspector at the same time

Debugging From Scratch:
=> Find the main activity (entry point) in manifest.xml
=> From entry Point, find the attached modules or view files
=> Find if there is a Navigation Graph

### Notes on adding Dependencies:
* First add the root/project level `build.gradle` requirements and build/sync. Then add all other module level gradle requirements.

### Chaingin Project Name:
=> From Studio's project tab as `Android`, refactor all occurrence by selecting `com.something.<packagename>` to new name. 
=> Also change app level build.gradle's `applicationID` and `namespace` with valid format (no dash `-`, use underscore `_`)
=> Also change settings.gradle's rootprojectname. 

### Easy Debug:
Add Associated Class (tools:context) in root layout element per file => `tools:context="com.domain.project.package.className"`

### Some Common Errors:
* non-Gradle Java modules + Android-Gradle modules in one project:
    - `Invalidate Cache and Restart` => after restart => close the project => then delete `.gradle` and `.idea` directory from the root of the project => open Android Studio and Open the selected project.

* Kotlin and compileDebugJavaWithJavac' version mismatch => Make all the version use same JDK
```kotlin
// build.gradle.kt :app level
compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlinOptions {
    jvmTarget = JavaVersion.VERSION_17
}
```