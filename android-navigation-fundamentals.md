## Overview:
This Docs is aming towards the app's navigation workflow of the android framework.

### Intent:
An intent is an object representing some action to be performed. Like lanuching an activity. Intent describes the request, not the actual result.

* explicit intent: it is highly specific, where you know the exact activity to be launched, often a screen in your own app

* implicit intent: it is a bit more abstract, where you tell the system the type of action, such as opening a link, composing an email, or making a phone call, and the system is responsible for figuring out how to fulfill the request. You commonly use implicit intents for performing actions involving other apps and rely on the system to determine the end result. You'll use both types of intents in the Words app.

### Setting Up Explicit Intent Steps:
1: get the context
2: create intent passing the context and the destination activity. On Recycler view, 
3: Inject data to the intent with putExtra
4: Call the startActivity() method on the context object, passing in the intent object.
5: Get data to the targated 


### Setting Implicit Intent:
* types: Action_VIEW (Open in browser), CATEGORY_APP_MAPS (Launhes Maps), CATEGORY_APP_EMAIL (launching the email app), CATEGORY_APP_GALLERY (launching the gallery (photos), ACTION_SET_ALARM, ACTION_DIAL (phone call)
* Commont Types: https://developer.android.com/guide/components/intents-common

* Steps:
    - Define intent like val intent = Intent(Intent.ACTION_VIEW, query_url)
    - Fire context.startActivity(intent)

### Menu:
* Add Menu Resource File: by new > android resource file. The Resource type should be menu and file name can be layout_menu. A new directory should be created in res with layout_menu.xml file. And define menu item as
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <item android:id="@+id/action_switch_layout"
        android:title="@string/action_switch_layout"
        android:icon="@drawable/ic_linear_layout"
        app:showAsAction="always"
        android:iconTint="@color/white"/>
</menu>
```
* also reference the icons for the menu.

* onCreateOptionsMenu and onOptionsItemSelected:
 - onCreateOptionsMenu : override this function and inflate the menu xmml file with menuInflater to actually draw the menu inside app bar when first created. The android system will provede the icon positioning. Icon can also be set by initial state here.
  - onOptionsItemSelected: this override is required to add the functionality when the menu button is tapped/clicked.

```kotlin
    /**
     * this will actually draw the menu icon on the app bar
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.layout_menu, menu)
        return true
    }

    /**
     * this will be called upon menu item interaction by menu/item's id or itemId
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // check if the tapped menu item is same as targeted menu item
            R.id.action_switch_layout -> {
                isLinearLayoutManager = !isLinearLayoutManager
                chooseLayout()
                return true
            }
            // otherwise do nothing by returning default option by ide/system
            else -> super.onOptionsItemSelected(item)
        }
    }
```
### Activity Lyfecycle Methods:
* onCreate() : called once, just after the activity is initialized (when the new Activity object is created in memory). Tasks here are inflate the layout, initialized variables, define click listeners, or set up view binding.

* onStart(): is called just after onCreate(). After onStart() runs, the activity is visible on the screen. Unlike onCreate(), which is called only once to initialize the activity, onStart() can be called many times in the lifecycle of your activity. onStart() is paired with a corresponding onStop().

* onPause & onResume: OnPause is called when the app's focus is lost but still visible. onResume is called when the app get focus.
* onStop(): is called when the app is no longer visible on screen.

* onDestroy: called once when activity is completely shut down and ready for garbage collection.




```kotlin
override fun onResume() {
   super.onResume()
   Log.d(TAG, "onResume Called")
}

override fun onPause() {
   super.onPause()
   Log.d(TAG, "onPause Called")
}

override fun onStop() {
   super.onStop()
   Log.d(TAG, "onStop Called")
}

override fun onDestroy() {
   super.onDestroy()
   Log.d(TAG, "onDestroy Called")
}

override fun onRestart() {
   super.onRestart()
   Log.d(TAG, "onRestart Called")
}
``` 

* When an activity start for the first time: onCreate(), onStart(), and onResume() callbacks are called
* When app is closed: onPause(), onStop(), and onDestroy() are called, in that order. After onDestroy(), Garbage Collection will happen.
* when home is pressed: onPause(), onStop() are called. The app is now on background. onRestart(), onStart(), and onResume() are called.
* app from background to foreground: 
* finish(): this activity method will completely shut down the activity. 

* when configuration change happens: like screen rotate, language change, desktop mode etc. The activity is completely shut down and rebuild, the activity starts up with default values.

* onSaveInstanceState(outState: Bundle): this callback is called each time after the activity has been stopped and goes into background. It's a way to save application data/state and passed back to onCreate(savedInstanceState: Bundle?). A Bundle is a collection of key-value pairs, where the keys are always strings and values can be int or boolean. If bundle data is too large it will throw error. So keep the bundle data small. (Note: onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) is not a replacement of this single bundle param method)
```kotlin
const val KEY_REVENUE = "revenue_key"
const val KEY_DESSERT_SOLD = "dessert_sold_key"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
        * read the state and update variables.
        */

        if (savedInstanceState != null) {
            revenue = savedInstanceState.getInt(KEY_REVENUE)
            dessertsSold = savedInstanceState.getInt(KEY_DESSERT_SOLD)
            /**
            * then update the UI according to state
            */
            showCurrentDessert()
        }
    }

    override fun onSaveInstanceState(outState: Bundle ) {
        super.onSaveInstanceState(outState)
        /**
        * update the current state in outState, then read back from onCreate override
        */
        outState.putInt(KEY_REVENUE, revenue)
        outState.putInt(KEY_DESSERT_SOLD, dessertsSold)
    }
```

### Fragments:
it's simply a reusable piece of app's ui. Like activities, fragments have a lifecycle and can respond to user input. A fragment is always contained within the view hierarchy of an activity when it is shown onscreen. Each fragment manages its own separate lifecycle.

* Fragment lifecycle: The fragment lifecycle has five states, represented by the Lifecycle.State enum.
 - INITIALIZED: A new instance of the fragment has been instantiated.
 - CREATED: The first fragment lifecycle methods are called. During this state, the view associated with the fragment is also created.
 - STARTED: The fragment is visible onscreen but does not have "focus", meaning it can't respond to user input.
 - RESUMED: The fragment is visible and has focus.
 - DESTROYED: The fragment object has been de-instantiated.

 * Fragment Methods Override: Like activities, the Fragment class provides many methods that you can override to respond to lifecycle events.
 
 - onCreate(): The fragment has been instantiated and is in the CREATED state. However, its corresponding view has not been created yet.
 - onCreateView(): This method is where you inflate the layout. The fragment has entered the CREATED state.
 - onViewCreated(): This is called after the view is created. In this method, you would typically bind specific views to properties by calling findViewById().
 - onStart(): The fragment has entered the STARTED state.
 - onResume(): The fragment has entered the RESUMED state and now has focus (can respond to user input).
 - onPause(): The fragment has re-entered the STARTED state. The UI is visible to the user
 - onStop(): The fragment has re-entered the CREATED state. The object is instantiated but is no longer presented on screen.
 - onDestroyView(): Called right before the fragment enters the DESTROYED state. The view has already been removed from memory, but the fragment object still exists.
 - onDestroy(): The fragment enters the DESTROYED state.

### Activity vs Fragment (Layout Inflate, View Bindings):
With activities, onCreate() method is used to inflate the layout and bind views. But in the fragment lifecycle, onCreate() is called before the view is created, so layout inflate is not possible here. In fragment onCreateView() is used to inflete layout. Then, after the view has been created, the onViewCreated() method is called, where view binding can be done to bind properties to specific views.

### Fragment Creating:
Like activity, each fragment will consist of two files, an XML file + a Kotlin class to display data and handle user interactions

With app selected in the Project Navigator, add the following fragments (File > New > Fragment > Fragment (Blank)), select name for the fragment (layout name will be generated automatically) and both a class and layout file should be generated for each.

* context in Fragment: Unlike an activity, a fragment is not a Context. You can't pass in this (referring to the fragment object) as the layout manager's context. However, fragments provide a context property you can use instead.

* intent in Fragment: fragments don't have direct access to the intent, so can be referenced it with activity.intent


### Setting Up Jetpck Navigaiton Component:
* Navigation Dependency Adding:
    - in project build.gradle (also top level) add nav_version equal to 2.5.2 in buildscript ext object.
    - then in app build.gradle add
        - implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
        - implementation "androidx.navigation:navigation-ui-ktx:$nav_version"

* Safe Args—a Gradle plugin that will assist you with type safety when passing data between fragments.

* SafeArgs Plugin Adding: 
    - top/project level build.gradle inside buildscript > dependencies add
        - classpath "androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version"
    - app-level build.gradle file, within plugins at the top, add
        - id 'androidx.navigation.safeargs.kotlin'

### Jetpack Navigation Component Brif:
 The navigation component simply refers to the collection of tools for implementing navigation, particularly between fragments.

The Navigation component has three key parts
1. Navigation Graph: its is a virtual mapping of app's navigation described in xml file like layout, it consists of destinations which correspond to individual activities and fragments, also actions between them to navigate from one destination to another. 

Note: (Navigation Graph -> NavGraph, accessed by FragmentContainerView) Behind the scenes, this actually creates a new instance of the NavGraph class. However, destinations from the navigation graph are displayed to the user by the FragmentContainerView.
```xml
<androidx.fragment.app.FragmentContainerView
android:id="@+id/nav_host_fragment"
android:name="androidx.navigation.fragment.NavHostFragment"
android:layout_width="match_parent"
android:layout_height="match_parent"
app:defaultNavHost="true" <!-- allows the fragment container to interact with the system navigation hierarchy, like if the system back button is pressed, then the container will navigate back to the previously shown fragment -->
app:navGraph="@navigation/nav_graph" />
```

* on name attribute of the FragmentContainerView, specifying androidx.navigation.fragment.NavHostFragment other than a specific fragment, allows FragmentContainerView to navigate between fragments in editor panel.

2. NavHost: used to display destinations from a navigation graph within an activity. When you navigate between fragments, the destination shown in the NavHost is updated. You'll use a built-in implementation, called NavHostFragment, in your MainActivity

3. NavController: this object control the navigation between destinations displayed in the NavHost. When working with intents, you had to call startActivity to navigate to a new screen. With the Navigation component, you can call the NavController's navigate() method to swap the fragment that's displayed.

### Brif Start up of the Jetpack Navigation Component:
- Creating Navigation Graph: File > New > Android Resource File and create Resource Type "Navigation", give it a name (which is referenced from FragmentContainerView's' app:navGraph attribute inside of an activity).

- Destination: Inside the newly created file, add destinations. 
- Create Navigation Action: Destinations Handle Can be dragged like Node Editor to link the action/navigation.
- extra data as argument: inside attribute panel's argument section, a new argument can be created. The safe args plugin can be helpful for the type safety here.
- Rebuild: Do Build > Rebuild Project To generate the code based on the navigation graph the project needs to rebuild
- Destination Arguments vs destination level arguments: the 1st type is where the argument is not nested inside action and the 2nd type is where it is. Destination-level arguments and default values are used by all actions that navigate to the destination with some data
```xml
<!-- Showing destinatin level argument -->
<fragment
        android:id="@+id/letterListFragment"
        android:name="com.example.wordsapp.LetterListFragment"
        android:label="fragment_letter_list"
        tools:layout="@layout/fragment_letter_list" >
        <action
            android:id="@+id/action_letterListFragment_to_wordListFragment"
            app:destination="@id/wordListFragment" >
            <argument
                android:name="letter"
                app:argType="string" />
        </action>
    </fragment>
```

- arguments vs intent.extra: in fragment there are no intent.extra available because when navigation between fragments, we don't fire new intents. Data is transfered between fragments using FragmentDirection/action and can be get using arguments?.getString(Key).toString()

- Fragment Labeling:
### Activiti to Fragment to Intent Summary
- Setup a container for Fragment inside an activity. 
### Navigational Components:
