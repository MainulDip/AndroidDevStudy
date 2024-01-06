## Overview:
This Docs is aming towards the app's navigation workflow of the android framework.

### Intent
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
* Common Types: https://developer.android.com/guide/components/intents-common

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
### Activity Lifecycle Methods:
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


### Setting Up Jetpack Navigation Component:
* Navigation Dependency Adding:
    - in project build.gradle (also top level) add nav_version equal to 2.5.2 in buildscript ext object.
        - or constant can also be defiend inside dsl object by > def constantName = "constantValue"
    - then in app build.gradle add
        - implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
        - implementation "androidx.navigation:navigation-ui-ktx:$nav_version"

* Safe Args—a Gradle plugin that will assist you with type safety when passing data between fragments.

* SafeArgs Plugin Adding: 
    - top/project level build.gradle inside buildscript > dependencies add
        - classpath "androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version"
    - app-level build.gradle file, within plugins at the top, add
        - id 'androidx.navigation.safeargs.kotlin'

### Jetpack Navigation Component Brief:
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

2. NavHost: used to display destinations from a navigation graph within an activity. When you navigate between fragments, the destination shown in the NavHost is updated. You'll use a built-in implementation, called NavHostFragment, in your MainActivity.

3. NavController: this object control the navigation between destinations displayed in the NavHost. When working with intents, you had to call startActivity to navigate to a new screen. With the Navigation component, you can call the NavController's navigate() method to swap the fragment that's displayed.

### Brief Start up of the Jetpack Navigation Component:
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
                app:argType="string"
                android:defaultValue="Dummy Text" />
        </action>
    </fragment>
```

- arguments vs intent.extra: in fragment there are no intent.extra available because when navigation between fragments, we don't fire new intents. Data is transferred between fragments using FragmentDirection/action and can be get using arguments?.getString(Key).toString()

- Fragment Labeling: Set title/label name in app/action bar from main activity using
```kotlin
/**
         * setting action/top bar's title as Fragment's title
         * supportFragmentManager.findFragmentById(R.id.nav_host_fragment) returns a Fragment? (nullable), but we need NavHost and Fragment together
         * NavHostFragment implements Fragment and NavHost
         * so we can cast Fragment as NavHostFragment to use it's controller (navController) */
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
```
### Activity to Fragment to Intent Summary:
- Setup a container for Fragment inside an activity. 
### Navigational Components:
* NavHostFragment: Container for home/destinations fragments, provide destination direction/action's info. But the actual navigation is done using code by NavController.

* NavController: Conducts Actual Navigation Using the NavHostFragment's NavDirections info
```kotlin
val action: NavDirections = LetterListFragmentDirections.actionLetterListFragmentToWordListFragment(letter = holder.button.text.toString())
holder.view.findNavController().navigate(action)
```

* NavigationView: Menu for DrawerLayout or BottomNavBar
* NavigationUI: Updates content outside NavHostFragment

### Fragment to Framment Navigation:
```kotlin
findNavController().navigate(R.id.action_startFragment_to_flavorFragment)
```


### ViewModel (Android Jetpack Architecture Components):
The ViewModel is a model of the app data that is displayed in the views. Models are components that are responsible for handling the data for an app. ViewModels are Models Specific to Views. ViewModel allow your app to follow the architecture principle, driving the UI from the model.
### UI Controllers(activity/fragment) vs ViewModel:
 The Android system can destroy UI controllers at any time based on certain user interactions or because of system conditions like low memory. Thats why it's not a good place to store app's state. Instead, the decision-making logic about the data should be added in your ViewModel. The ViewModel stores the app related data that isn't destroyed when activity or fragment is destroyed and recreated by the Android framework.

 * Activities and fragments are responsible for drawing views and data to the screen and responding to the user events. As licycle of this is not on developers hand, application state should never live here.

 * ViewModel is responsible for holding and processing all the data needed for the UI. It should never access the view hierarchy (like view binding object) or hold a reference to the activity or the fragment. It will only process and deliver the data to the UI controllers.

 ### ViewModel Implementation:
 1. add the ViewModel Dependencies inside module's build.gradle
 2. create Model class Inheriting form ViewModel()
 3. Inside UI-Controller (Fragment/Activity) delegate property of the Model class by viewModels()

 * viewModels() return Lazily, so when the reference is first access, it will be created.

 NB: by delegating from viewModels(), the android system handles the data persistance of the Model class. The delegate class creates the viewModel object for you on the first access, and retains its value through configuration changes and returns the value when requested.

 ### ViewModel Lifecycle:
 The framework keeps the ViewModel alive as long as the scope of the activity or fragment is alive. A ViewModel is not destroyed if its owner is destroyed for a configuration change, such as screen rotation. The new instance of the owner reconnects to the existing ViewModel instance.

 ### Single Fragment's ViewModel:
 To persist a ViewModel object within a Fragment (Not entire Activity), viewModels() is used. The delegated object is only destroyed if the fragment is destroyed.
```kotlin
private val myViewModel: MyViewModel by viewModels()

// or

private val myViewModel by viewModels<MyViewModel>()
```

 ### Multiple Fragments/Shared ViewModel:
 Multiple fragments in an activity can share same ViewModel Object using the Activity scope. activityViewModel() is used to delegate ViewModel which will persist throughout a Activity and it's fragment.

 When its activity get destroy, activityViewModels() will be destroyed as well.

 ```kotlin
 private val sharedViewModel: OrderViewModel by activityViewModels()
 ```

 ### LiveData:
 LiveData is an observable data holder class that is lifecycle-aware.
 - LiveData holds data; LiveData is a wrapper that can be used with any type of data.
 - LiveData is observable, which means that an observer is notified when the data held by the LiveData object changes.
 - LiveData is lifecycle-aware, meaning it only updates observers that are in an active lifecycle state. When you attach an observer to the LiveData, the observer is associated with a LifecycleOwner (usually an activity or fragment). The LiveData only updates observers that are in an active lifecycle state such as STARTED or RESUMED. You can read more about LiveData and observation : https://developer.android.com/topic/libraries/architecture/livedata.html#work_livedata .

 ### Creating LiveData and hooking observer:
 When data is changed hooked observer will be notified and it will update the UI according to the observer callback in fragment/activity.
 1. MutableLiveData : private val _something = MutableLiveData<String>() [Or initial value using MutableLiveData(InitialValue)]
 2. LiveData<T>: For getter only (for the newly created val above)
 3. Set Livedata Value: by calling _something.value = "something" 
 4. Set Observer : Inside Fragment's onViewCreated, attatch the observer for the targated by
 ```kotlin
viewModel.currentScrambledWord.observe(viewLifecycleOwner, Observer { newWord ->
    Log.d("GameFrammentObserve", "viewModel.currentScrambledWord.observe calling, it will be called if the data changes")
    binding.textViewUnscrambledWord.text = newWord
})
 ```
 Note : if observed data is changed, it will call the lambda.
 ### LiveData With DataBindings:
Data binding binds the UI components in layouts to data sources using a declarative format. It's a part of the Android JetPack library
* In simpler terms Data binding is binding data (from code) to views + view binding (binding views to code)
```kotlin
// Here viewModel is the delegated GameViewModel : ViewModel() instance.
// ViewBinding in UI Controllers view created lificycle method
binding.textViewUnscrambledWord.text = viewModel.currentScrambledWord

// Databindings in layout xml file. 
// Here gameViewModel is the delegated GameViewModel : ViewModel() instance.
android:text="@{gameViewModel.currentScrambledWord}"
```

* Steps For DataBindings:
 - build.gradle add buildFeatures => dataBinding = true and add => id 'kotlin-kapt as plugins
    - this generates a binary file for every layout xml file. For activity_main.xml, the auto generated class will be ActivityMainBinding (Like View Binding)
 
 - To use DataBinding, the layout file will start with <layout> followed by an optional <data> element and a view (ScrollView or other type of view) root element.
    - use IDE feature to auto convert into databinding layout by alt+enter while keeping cursor on the parent view and select "convert to databinding......."
 
 ```xml
 <layout xmlns:android="http://schemas.android.com/apk/res/android"
   xmlns:app="http://schemas.android.com/apk/res-auto"
   xmlns:tools="http://schemas.android.com/tools">

   <data>

   </data>

   <ScrollView
       android:layout_width="match_parent"
       android:layout_height="match_parent">

       <androidx.constraintlayout.widget.ConstraintLayout
         ... >
       </androidx.constraintlayout.widget.ConstraintLayout>
   </ScrollView>
</layout>
 ```
 
 - instantiate binding as DataBindingUtil on "onCreateView" (for fragment) lifecycle method
    - like => binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
    - instade of viewBinding => binding = GameFragmentBinding.inflate(inflater, container, false)
    - Note: the lateinit declaration of the ViewModel will remain same like ViewBinding => private val viewModel: GameViewModel by viewModels()

- bind the layout variables and lifecycleOwner with the binding object in onCreateView (for fragment) lifecycle method. Like

```kotlin
 binding.gameViewModel = viewModel
 binding.maxNoOfWords = MAX_NO_OF_WORDS
 binding.lifecycleOwner = viewLifecycleOwner
```
- connect layout view using binding expression "@{}" with the layout's declared variables directly, like

```xml
<!-- databinding layout attaching variables with view -->
<data>
    <variable
        name="gameViewModel"
        type="com.example.android.unscramble.ui.game.GameViewModel" />

    <variable
        name="maxNoOfWords"
        type="int" />
</data>
...
<TextView
   android:id="@+id/textView_unscrambled_word"
   ...
   android:text="@{gameViewModel.currentScrambledWord}"
   .../>
``` 
- No LiveData Observer Required: The layout (xml) will receive the updates of the changes to the LiveData defined in the custom viewModel (ViewModel() inherited classes) through data binding in variables.

### Resources in data binding expressions:
A data binding expression can reference app resources using
```xml
<!-- layout.xml || pass the value as function params -->
android:text="@{@string/example_resource(user.lastName)}"

<!-- strings.xml, here %s will be replaced with "user.lastname" defined in layout file above -->
<string name="example_resource">Last Name: %s</string>
```

### LiveData to Talkback:
Convert LiveData to LiveData<Spanable> and transform
```kotlin
private val _currentScrambledWord = MutableLiveData<String>()
val currentScrambledWord: LiveData<Spannable> get() = Transformations.map(_currentScrambledWord) {
    if (it == null) {
        SpannableString("")
    } else {
        val scrambledWord = it.toString()
        val spannable: Spannable = SpannableString(scrambledWord)
        spannable.setSpan(
            TtsSpan.VerbatimBuilder(scrambledWord).build(),
            0,
            scrambledWord.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        spannable
    }
}
```
### Transformations.map:
Transformation methods for LiveData. These methods permit functional composition and delegation of LiveData instances. The transformations are calculated lazily, and will run only when the returned LiveData is observed. Lifecycle behavior is propagated from the input sourceLiveData to the returned one.

### NavigaitonAdvanced App | Deep on Navigation:

### AppCompatActivity inheritance with constructor:
MainActivity can inherit from parameterized constructor of the AppCompatActivity

* This code uses a parameterized constructor AppCompatActivity(@LayoutRes int contentLayoutId) 
* which takes in a layout that will be inflated as part of super.onCreate(savedInstanceState)
```kotlin

class MainActivity : AppCompatActivity(R.layout.activity_main)
/**
 * Same as below.
    class MainActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        }
    }
 * This code uses a parameterized constructor AppCompatActivity(@LayoutRes int contentLayoutId) 
 * which takes in a layout that will be inflated as part of super.onCreate(savedInstanceState)
 */
```

### Fragment to Fragment Navigation or Navigation to a destination:
To retrieve the NavController, Use
* Form Fragment: Fragment.findNavController()
* From View: View.findNavController()
* From Activity: Activity.findNavController(viewId: Int)

```kotlin
// Navigate to a destination from a fragment
import androidx.navigation.fragment.findNavController

findNavController().navigate(R.id.action_startFragment_to_flavorFragment)
```
Note: as the navController is tied with the activity/fragment 's layout by NavHostFragment's attribute, this object is generated behind the scene and can be accessed by those methods.
Docs: https://developer.android.com/guide/navigation/navigation-navigate

### Shared ViewModel:
 Multiple fragments in an activity can share same ViewModel Object using the Activity scope. activityViewModel() is used to delegate ViewModel which will persist throughout a Activity and it's fragment.

 When its activity get destroy, activityViewModels() will be destroyed as well.

 ```kotlin
 private val sharedViewModel: OrderViewModel by activityViewModels()
 // or
 private val sharedViewModel by activityViewModels<OrderViewModel>()
 ```

 ### viewModels() vs activityViewModels vs navGraphViewModels() :
 * viewModels() is used to scoped to the fragment where it initialized. When that attached fragment get destroy, viewModels() will also get destroyed.
    => private val sharedViewModel: OrderViewModel by viewModels()

 * activityViewModels() is used to scoped to its activity and when its activity get destroy, activityViewModels() will be destroyed as well.
    => private val sharedViewModel: OrderViewModel by activityViewModels()

 * navGraphViewModels() is ViewModel that bind to navigation graph and will persist throughout the entire back-stack of that navigation graph. This is how we persist our data throughout certain Fragment and dispose these when exit this navigation graph.
    => private val viewModel: TheViewModel by navGraphViewModels(R.navigation.nav_graph)


### Listener bindings with layout/fragment.xml:

With Data Binding, listeners (click/event listeners) can be bind directly to call methods using lambda expression.

When a callback is used in an expression, data binding automatically creates the necessary listener and registers it for the event. When the view fires the event, data binding evaluates the given expression.
```txt
android:onClick="@{() -> domething()}"
// Passing String As Param, use single quote for the xml attribute's value and double quote for strings
android:text='@{map["firstName"]}'
```

Docs: Binding Expression : https://developer.android.com/topic/libraries/data-binding/expressions


### Date Formatter:
The Android framework provides a class called SimpleDateFormat, which is a class for formatting and parsing dates in a locale-sensitive manner. It allows for formatting (date → text) and parsing (text → date) of dates.
* SimpleDateFormat("E MMM d", Locale.getDefault())
* Locale.getDefault() will retrive the local info set on the user's devices
Docs: https://developer.android.com/reference/java/text/SimpleDateFormat#date-and-time-patterns


### LifecycleOwner and LiveData observer:
Fragments/UI controllers are the lifecycle owner. LifecycleOwner is a class that has an Android lifecycle, such as an activity or a fragment. 

A LiveData observer observes the changes to the app's liveData only if the lifecycle owner is in active states.

In dataBinding, UI controllers read the data when first read. To update UI instantly when a livedata changes, lifecycle owner is also need to be bound. It's the way of setting observer on LiveData.

* viewLifecycleOwner is tied to when the fragment has (and loses) its UI (onCreateView(), onDestroyView())

```kotlin
```kotlin
private var binding: FragmentSomethingBinding? = null

binding = FragmentSomethingBinding.inflate(inflater, container, false)

binding?.apply {
    ...
    lifecycleOwner = viewLifecycleOwner
    // lifecycleOwner = this@PickupFragment // is also possible, but can make mamory leack
    // this is tied to the fragment's overall lifecycle (onCreate(), onDestroy()), which may be substantially longer than onCreateView to onDestroyView
}
```

<details>

<summary>More Detail Example</summary>

```kotlin
private var binding: FragmentPickupBinding? = null
private val sharedViewModel by activityViewModels<OrderViewModel>()

override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
    val fragmentBinding = FragmentPickupBinding.inflate(inflater, container, false)
    binding = fragmentBinding
    return fragmentBinding.root
}

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding?.apply {
        viewModel = sharedViewModel
        lifecycleOwner = viewLifecycleOwner
        nextButton.setOnClickListener { goToNextScreen() }
    }
}
```

</details>

### LiveData transformation (Convert LiveData While Being Observable):
The LiveData transformation method(s) provides a way to perform data manipulations on the source LiveData and return a resulting LiveData object. In simple terms, it transforms the value of LiveData into another value. These transformations aren't calculated unless an observer is observing the LiveData object.# LiveData transformation:

The Transformations.map() is one of the transformation functions, this method takes the source LiveData and a function as parameters. The function manipulates the source LiveData and returns an updated value which is also observable.

```kotlin
private val _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price) {
        NumberFormat.getCurrencyInstance().format(it)
    }
```

### Listener Binding With Click Listener (2nd way):
Listener can be bind from layout.xml using data variable binding with the controller (UI) class itself in data binding.

Instade of binding listener from the ui controller, this can be more clean option

```xml
<data>
    <variable
        name="startFragment"
        type="com.example.cupcake.StartFragment" />
</data>

<Button
    android:id="@+id/order_one_cupcake"
    android:onClick="@{() -> startFragment.orderCupcake(1)}"  />
```

```kotlin
binding?.startFragment = this // bind onViewCreated

public fun orderCupcake(quantity: Int) { } // after binding this method is available to call from layout.xml
```

### Tasks, Activity and BackStack:
Activities in Android exist within tasks. A task is a collection of activities that the user interacts with when performing a certain job (i.e. checking email, creating a cupcake order, taking a photo).

Activities are arranged in a stack, known as a back stack, where each new activity the user visits gets pushed onto the back stack for the task. The activity on the top of the stack is the current activity the user is interacting with. The activities below it on the stack have been put in the background and have been stopped.

The back stack is useful for when the user wants to navigate backwards. Android can remove the current activity from the top of the stack, destroy it, and start the activity underneath it again. It's known as popping an activity off the stack, and bringing the previous activity to the foreground for the user to interact with. If the user wants to go back multiple times, Android will keep popping the activities off the top of the stack until you get closer to the bottom of the stack. When there are no more activities in the backstack, the user is brought back to the launcher screen of the device (or to the app that launched this one).

Note: After opening the app, if you tap Home on the device, the whole task for the app is put into the background. If you tap the launcher icon for the app again, Android will see if there's an existing task for your app and bring that to the foreground (with the back stack intact). If no existing task exists, then Android will create a new task for you and launch the main activity, pushing that onto the back stack.

* the back stack can also track the fragment destinations the user has visited with the help of the Jetpack Navigation component.

* The Navigation library allows you to pop a fragment destination off the back stack each time the user hits the Back button. This default behavior comes for free, without you needing to implement any of it. You would only need to write code if you need custom back stack behavior.

### Custom BackStack Implementation With Navigation Graphs:
* Add Navigation Actions (like last fragment to home fragment) on navigation graph.
* Add popUPTo (up to the destination) and popUpToInclusive (true) in navigation graph's newly created action to remove/pop-off the activit/fragment and in-between of those from the backstack.
* Then from UI controller, on click listener, navigate using : findNavController().navigate(R.id.action_fromFragment_to_destinationFragment)
* Using data binding, click listener can be called from layout.xml file using listener binding (@{()->passedClass.method()})

### Testing ViewModel and LiveData:
* unit test directory: src/test/java/com.example.appname/test.kt
* instumentation test: inside src/AndroidTest/java/com.example.appname/test.kt

Testing LiveData objects requires an extra step. because LiveData objects cannot access the main thread we have to explicitly state that LiveData objects should not call the main thread.

Note: a unit test assumes that everything runs on the main thread.

* LiveData objects need to be observed in order for changes to be emitted. A simple way of doing this is by using the observeForever method

```kotlin
class ViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun quantity_twelve_cupcakes() {
        /**
         * LiveData objects need to be observed in order for changes to be emitted. A simple way of doing this is by using the observeForever method.
         * */
        val viewModel = OrderViewModel()
        viewModel.quantity.observeForever {}
        viewModel.setQuantity(12)

        assertEquals(12, viewModel.quantity.value)
    }
}
```
### Adaptive Layout (SlidingPaneLayout):
* list UI pattern : just one row of list items. Good for mobile devices (narrow screen devices)
* list-detail UI (master-detail pattern): a list of item on the left side and details on the selected Item on the right side. It's better for large screen like tablet or desktop mode
* SlidingPaneLayout : it'a a way to implement list-detail/master-detail layout pattern. Here the details pane overlaps with the list pane when an item from the list is selected. SlidingPaneLayout supports showing two panes side by side on larger devices, while automatically adapting to show only one pane at a time on smaller devices such as phones.

```kotlin
// adding sliding panel layout in build.gradle (Module Aoo)
dependencies {
...
    implementation "androidx.slidingpanelayout:slidingpanelayout:1.2.0-beta01"
}
```
SlidingPaneLayout provides a horizontal, two pane layout for use at the top level of a UI. This layout uses the first pane as a content list or a browser, subordinate to a primary detail view for displaying content in the other pane.

* Adding Sliding Pane Layout on Fragment instade of FrameLayout:
 - Change the FrameLayout to androidx.slidingpanelayout.widget.SlidingPaneLayout in the xml file


* Inside the slidingpanelayout we need 2 view. 1 for lists (recyclerview) and another for details. The SlidingPaneLayout uses the width of the two panes to determine whether to show the panes side by side. If the available device with is same or more than the specified width of the slidingpanel layout, it will keep the both view side by side. It uses breakpoint to determine positioning.

* Notes On Devices Width:
```txt
Compact width   < 600dp     99.96% of phones in portrait

Medium width    600dp+      93.73% of tablets in portraitLarge unfolded inner displays in portrait

Expanded width  840dp+      97.22% of tablets in landscapeLarge unfolded inner displays in landscape
```
* android:layout_weight="1" : this will make the second view go full remaining spaces

* sliding pane layout navigation: Instade of navigating using findNavController().navigate(action), use slidingPaneLayout.openPane() to navigate responsively on both smaller and larger screen at the same time
```kotlin
// Here Sports List Fragment is using a recycler view for list item
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSportsListBinding.bind(view)

        // Initialize the adapter and set it to the RecyclerView.
        val adapter = SportsAdapter {
            // Update the user selected sport as the current sport in the shared viewmodel
            // This will automatically update the dual pane content
            sportsViewModel.updateCurrentSport(it)
            // Navigate to the details screen
            // val action = SportsListFragmentDirections.actionSportsListFragmentToNewsFragment()
            // this.findNavController().navigate(action)

            // Navigate using SlidingPanelLayout
            binding.slidingPaneLayout.openPane()
        }
        binding.recyclerView.adapter = adapter
        adapter.submitList(sportsViewModel.sportsData)
    }
```

* Project Configuration on Android Studio: ctrl+alt+shift+s to edit
### Back Navigation Customization:
For some case, like webview and slidingPaneLayout, we need custom navigation instade of the default to create better ux for the user, so user can navigate inside webview or can navigate slidingPaneLayout differently on both smaller and larger screen. If custom behaviout is not implemented, pressing the back button will close the current stack/task

### SlidingPaneLayout Custom Back Button Implementation:
SlidingPaneLayout provide the state of the layout through isEnable and isOpen as boolean. The boolean isSlideable will only be true if the second pane is slidable, which would be on a smaller screen and a single pane is being displayed. The value of isOpen will be true if the second pane - the contents pane is completely open.

So, we can implement a new class to override the default behavour of the back button by implementing OnBackPressedCallback class and passing these slidingPaneLayout state as constructor's enable parameter.

This will ensure that the callback is only enabled on the smaller screen devices and when the content pane is open.

```kotlin
class SportsListOnBackPressedCallback (private val slidingPaneLayout: SlidingPaneLayout ): OnBackPressedCallback(slidingPaneLayout.isSlideable && slidingPaneLayout.isOpen) {

    override fun handleOnBackPressed() {
        Log.d("Backpressure Callback", "handleOnBackPressed: override the default ")

        slidingPaneLayout.closePane()
    }

}
```
* Note: The SlidingPaneLayout always allows you to manually call open() and close() to transition between the list and detail panes on phones. These methods have no effect if both panes are visible and do not overlap

### SlidingPaneLayout's events:
The interface SlidingPaneLayout.PanelSlideListener contains three abstract methods onPanelSlide(), onPanelOpened(), and onPanelClosed(). These methods are called when the details pane slides, opens, and closes.
```kotlin
// implement the SlidingPaneLayout.PanelSlideListener Interface along with OnBackPressedCallback and implement the abstract methods of the newly implemented interface
class SportsListOnBackPressedCallback (private val slidingPaneLayout: SlidingPaneLayout ): OnBackPressedCallback(slidingPaneLayout.isSlideable && slidingPaneLayout.isOpen), SlidingPaneLayout.PanelSlideListener {...}
```

The base class for FragmentActivity, allows you to control the behavior of the Back button by using its OnBackPressedDispatcher. The OnBackPressedDispatcher controls how Back button events are dispatched to one or more OnBackPressedCallback objects.

```kotlin
// register the back-pressed call back custom class
requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, SportsListOnBackPressedCallback(slidingPaneLayout))
```

### Customize Sliding Panel Gesture Navigtion by Dragging:
On Sliding Pane Layout users can swipe in both directions by default because of gesture navigation. To change this 
```kotlin
// inside fragment's onViewCreated method
val slidingPaneLayout = binding.slidingPaneLayout
        // lock the gesture navigation on Sliding Pane Layout to Prevent Left-Right Drag option
        slidingPaneLayout.lockMode = SlidingPaneLayout.LOCK_MODE_LOCKED
```

### Important Topics:
* FragmentManager / supportFragmentManager: this class responsible for performing actions on app's fragments, such as adding, removing, or replacing them, and adding them to the back stack.

Every FragmentActivity and subclasses thereof, such as AppCompatActivity, have access to the FragmentManager through the getSupportFragmentManager() method. Inside fragment it can be retrieved by getParentFragmentManager() or getChildFragmentManager().

Docs: https://developer.android.com/guide/fragments/fragmentmanager

* NavController : Navigating to a destination is done using a NavController, an object that manages app navigation within a NavHost. Each NavHost has its own corresponding NavController. Can be retrieved by findNavController() method.
```kotlin
findNavController().navigate(R.id.action_startOrderFragment_to_entreeMenuFragment)
```
* Deep Link: In Android, a deep link is a link that takes you directly to a specific destination within an app.The Navigation component lets you create two different types of deep links: explicit implicit.

* FragmentTransaction : At runtime, a FragmentManager can add, remove, replace, and perform other actions with fragments in response to user interaction. Each set of fragment changes that you commit is called a transaction.
 - Docs: https://developer.android.com/guide/fragments/transactions

 * NavigationUI: The Navigation component includes a NavigationUI class. This class contains static methods that manage navigation with the top app bar, the navigation drawer, and bottom navigation.
  - Docs: https://developer.android.com/guide/navigation/navigation-ui