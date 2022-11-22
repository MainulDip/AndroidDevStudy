## Overview:
Personalized Jump Start Docs regarding android app's API, Fundamental Concepts And Communication between components.......

<br>

> ### There are 4 types of app components:
___
| Component | Class | AndroidManifest.xml | Methods |
| --- | --- | --- | --- |
| Activity | Intent | <activity> elements for activities | startActivity(), startActivityForResult()
| Service |  Intent, JobScheduler | <service> elements for services. | bindService()
| BroadcastRecever | Intent | <receiver> elements for broadcast receivers. | sendBroadcast(), sendOrderedBroadcast(), sendStickyBroadcast()
| ContentProvider | ContentResolver | query()): <provider> elements for content providers
---

### Test (Unit-test) and Android-Test (instrumentation-test/UI):
There are 2 directory inside app's src directory for automated testing. The "test" directory is for Unit testing and "androidTest" is for instrumentation-test or UI test. These are set inside app/build.gradle

> Unit Testing inside test directory:

- Test functions must first be annotated with the @ Test annotation imported from the org.junit.test
```kt
class ExampleUnitTest {
   @Test
   fun addition_isCorrect() {
       assertEquals(4, 2 + 2) // Assertion methods are the end goal of a unit test.


    //val rollResult: Int = 4
    // assertTrue("The value of rollResult was not between 1 and 6", rollResult in 1..6) // this will return true as 4 is in between 1 to 6 range. So this test will return true
   }
   
   // assertEquals()
   // assertNotEquals()
   // assertThat()
   // assertTrue()
   // assertFalse()
   // assertNull()
   // assertNotNull()
}
```


### UI, View and ViewGroup:
The user interface (UI) of an app like text, images, buttons, etc are called Views. Each view has relationship with other views. A ViewGroup (ConstraintLayout, RelativeLayout, etc) is a container that View objects can sit in, and is responsible for arranging the Views inside it. LayoutEditor helps to customize layout elements (Views).

Margin can be used to position a view from an edge of its container.

- constraint layout: in the context of the Layout Editor represents a connection or alignment of a view to another view, the parent layout, or an invisible guideline. For constraint position mistake, use "Clear Constraints of the Selection" by right clicking the view/viewGroup.

### Views behind/top on other views (overlap):
In the constraint layout, the views (xml) will be drawn from first to last. The first sibling will drawn first and last will be drawn last. Same way the last sibling is the top most element, Other views element will be behind of it as positioned.

### Adding Images and icons:
From AndroidStudio go to View > Tool Windows > Resource Manager and then "Import Drawables" and select. Imported images and icons will be on the app > res > drawable.

To add a image: LayoutEditor >Palette then drag/add an ImageView, then use "Pick a Resource" dialog. use the image listed under the Drawable tab.

Image can be referenced using R.drawable.image_name from java/kotlin. To set an Image use 
```kt
val diceImage: ImageView = findViewById(R.id.imageView)
diceImage.setImageResource(R.drawable.di)
```

ImageView attributes:
- scaleType: to scale the image. centerCrop value for filling the screen without distortion.
- srcCompat: hold the image reference for developer only

### Folder and File Structure:
- app: contains most of the files for the app to change.
- res: contains all the resources.
- layout: is for screen layouts.
- activity_main.xml: it contains a description of your screen layout (res > layout > activity_main.xml)
- drawable (app > res > drawable): This Directory includes all images and icons for the app
- 

### Measurement Units (dp,sp):
- dp (Density-independent Pixels): An abstract unit that is based on the physical density of the screen. These units are relative to a 160 dpi screen, so one dp is one pixel on a 160 dpi screen. The ratio of dp-to-pixel will change with the screen density, but not necessarily in direct proportion. "dip" and "dp" are same.

- sp (Scale-independent Pixels, for Font-Size): Similar to dp unit, but also scaled by the user's font size preference. 

- px (Pixels) - Actual pixels or dots on the screen.
- in (Inches) - Physical size of the screen in inches.
- mm (Millimeters) - Physical size of the screen in millimeters.
- pt (Points) - 1/72 of an inch.

> Always use sp for font sizes and dip for everything else.

### Language Translation:
Extract strings into a resource file (app > res > values > strings.xml).

### Accessibility:
- contentDescription: For image description
- importantForAccessibility: Set "no" for decorative elements only.

### Activity:
An Activity provides the window in which your app draws its UI. In Android, each screen is an Activity. The top-level or first activity is often called the MainActivity and is provided by the project template. App can have single or multiple Activities. Each Activity has a specific purpose.

I.E: in a photo gallery app, you could have an Activity for displaying a grid of photos, a second Activity for viewing an individual photo, and a third Activity for editing an individual photo

```kt
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_main)
   }
}

// open class AppCompatActivity : FragmentActivity, AppCompatCallback, TaskStackBuilder.SupportParentable, ActionBarDrawerToggle.DelegateProvider

// class FragmentActivity : ComponentActivity, ActivityCompat.OnRequestPermissionsResultCallback, ActivityCompat.RequestPermissionsRequestCodeValidator

// interface AppCompatCallback
```

```java
// public class AppCompatActivity extends FragmentActivity implements AppCompatCallback, TaskStackBuilder.SupportParentable, ActionBarDrawerToggle.DelegateProvider

// public class FragmentActivity extends ComponentActivity implements ActivityCompat.OnRequestPermissionsResultCallback, ActivityCompat.RequestPermissionsRequestCodeValidator

// public interface AppCompatCallback
```
Note: setContentView() must be called before findViewById()

Note: Android apps operate differently unlike raw Kotlin. Instead of calling a main() function, the Android system calls the onCreate() method of the MainActivity when your app is opened for the first time.

Auto-Import: File > Settings > Editor > General > Auto Import."Unambiguous imports on the fly" and "Optimize imports on the fly" (for current project) should be checked. Optimize imports settings for removing un-used imports.

### Listeners (event):
```kt
val rollButton: Button = findViewById(R.id.button)
rollButton.setOnClickListener {
    val resultTextView: TextView = findViewById(R.id.textView)
    resultTextView.text = "6"

    val toast = Toast.makeText(this, "Dice Rolled!", Toast.LENGTH_SHORT)
    toast.show()
}
```

### Debug and Logging:

Logging: There are several functions for logging output, taking the form Log.v(//Verbose), Log.d(//Debug), Log.i(//Info), Log.w(//Warning), or Log.e(//Error), Log.wtf() (What a Terrible Failure). These methods take two parameters: the first, called the "tag", is a string identifying the source of the log message (such as the name of the class that logged the text). The second is the actual log message. These logs are ASSERT level, a log level above ERROR

```kt
private const val TAG = "MainActivity"

fun logging() {
    Log.v(TAG, "Hello, world!")
}

fun division() {
    val numerator = 60
    var denominator = 4
    repeat(5) {
        Log.v(TAG, "${numerator / denominator}")
        denominator--
    }
}

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    logging()
    division() // app will crash
    // java.lang.RuntimeException: Unable to start activity ComponentInfo{com.example.debugging/com.example.debugging.MainActivity}: java.lang.ArithmeticException: divide by zero
}
```
> Note/Warning: Because Logs are removed from release builds, always avoid introducing side effects (modifying variables, etc values) from log statements.

### Stack trace:
When one function calls another function, the device won't run any code from the first function until the second function finishes. Once the second function finishes executing, the first function resumes where it left off. This is similar to a stack in the physical world, such as a stack of plates or a stack of cards. If you want to take a plate, you're going to take the top most one. It's impossible to take a plate lower in the stack without first removing all the plates above it.

```kt
val TAG = ...

fun first() {
    second()
    Log.v(TAG, "1")
}

fun second() {
    third()
    Log.v(TAG, "2")
    fourth()
}

fun third() {
    Log.v(TAG, "3")
}

fun fourth() {
    Log.v(TAG, "4")
}

// If you call first(), then the numbers will be logged in the following order.
// 3
// 2
// 4
// 1
```
### Coding Best Practice:
- Pseudocode: It is is an informal description of how some code might work. It uses some elements of computer language like if / else, but describes things in a human understandable way. It can be useful for planning the correct approach to take before all the details have been decided.