## Overview:
This is part two of the continued android-app-fundamentals. It's the compiled form of https://developer.android.com/courses/android-basics-kotlin/unit-2 and some more.

### Some UI-Elements/Views:
- EditText - for entering and editing text

- TextView - to display text like the service question and tip 
amount
- RadioButton - a selectable radio button for each tip option

- RadioGroup - to group the radio button options, android:checkedButton="@id/option_twenty_percent" checked initially/default

- Switch - an on/off toggle for choosing whether to round up the 
tip or not

The UI for an Android app is built as a containment hierarchy of components (widgets), and the on-screen layouts of those components (Buttons, TextViews, ImageViews). Note those layouts are UI components themselves.


### Terms:
- androidx : Jetpack based UI-Component Library
- xmlns : XML namespace
- @+id/.... : a new resource ID

### Get User Input:

### View binding:
View Binding makes it much easier and faster to call methods on the views in your UI. You'll need to enable view binding for your app in Gradle, and make some code changes.

Enable View Binding:  app's build.gradle
```gradle
buildFeatures {
    viewBinding = true
}
```