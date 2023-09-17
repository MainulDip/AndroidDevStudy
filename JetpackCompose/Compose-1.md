### Overview:
Modern toolkit for building native Android UI with declarative functions using Kotlin instead of the XML based layout.

### Main Components:
* MainActivity: Starting point, which extends the ComponentActivity
* setContent | ComponentActivity.setContent : Creates the root view of the UI. 
* Composable function:
@Composable annotation makes it into a composable function, it's the building block of Jetpack Compose. It describe the UI/Views and provide data dependencies. Any function including a @Composable function needs to be itself @Composable