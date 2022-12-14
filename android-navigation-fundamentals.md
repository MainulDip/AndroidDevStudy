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

* onSaveInstanceState(outState: Bundle): this callback is called each time after the activity has been stopped and goes into background. It's a way to save application data/state and passed back to onCreate(savedInstanceState: Bundle?). A Bundle is a collection of key-value pairs, where the keys are always strings and values can be int or boolean. If bundle data is too.