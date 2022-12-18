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
Like activity, each fragment will consist of two files, an XML file + a Kotlin class to display data and handle user interactions.

With app selected in the Project Navigator, add the following fragments (File > New > Fragment > Fragment (Blank)), select name for the fragment (layout name will be generated automatically) and both a class and layout file should be generated for each.

* context in Fragment: Unlike an activity, a fragment is not a Context. You can't pass in this (referring to the fragment object) as the layout manager's context. However, fragments provide a context property you can use instead.

* intent in Fragment: fragments don't have direct access to the intent, so can be referenced it with activity.intent
### Navigational Components:
