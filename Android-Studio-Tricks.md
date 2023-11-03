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
Visual Debugging : 
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