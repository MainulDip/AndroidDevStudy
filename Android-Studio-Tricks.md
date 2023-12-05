### General Keyboard Helpers:
- `ctrl + p` inside fn will suggest all the params
- `ctrl + space` will suggest
- `ctrl + o` override/implement fn suggestion in side class
- typing `this.` + `ctrl + space` inside a lambda block (scope) will suggest more precisely
- `alt + enter` to import
- `alt + enter` inside a typo (word with curly underling) will fix
- `r` will refresh xml layout design
### Compose
- typing `wc` in editor for column template
- Tooling : https://developer.android.com/jetpack/compose/tooling#editor-actions
### Intellij Plugins:
`Key Promoter x` suggest short-cut of possible task done through GUI in IDE 

### Digging Tricks:
1. Ctrl + click function and inspect usages in current project. There are options for include/exclude Test classes, preview sources, etc. 

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

### Changing Project Name:
=> From Studio's project tab as `Android`, refactor all occurrence by selecting `com.something.<packagename>` to new name. 
=> Also change app level build.gradle's `applicationID` and `namespace` with valid format (no dash `-`, use underscore `_`)
=> Also change settings.gradle's rootprojectname. 
=> find the package (`java package`) in each kotlin/java file inside `src/main` and test directories.

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

### Working With xml Layout file:
- `r` for refresh the xml design layout
- placeholder text `tools:text` and image `tools:src`
- split code and xml side by side (not horizontally): right click the tab and select `split right`.
- layout rendering issue: change `api version for preview` also try to make android studio full screen mode


### Open Multiple Projects:
To open multiple projects simultaneously in Android Studio, go to `Settings` > `Appearance & Behavior` > `System Settings`
in the Project Opening section, choose `Open project in new window` & save.
* After this, while on a project, `file` > `open` and select will open the project in new window without closing the previous one.