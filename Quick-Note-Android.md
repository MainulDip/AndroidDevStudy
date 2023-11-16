### findViewByID vs ViewBinding:


If `binding` is enabled in a module, it generates a binding class for each XML layout file present in that module. An instance of a binding class contains direct references to all views that have an ID in the corresponding layout.

* In most cases, view binding replaces findViewById.

```kotlin
// attaching xml view as active to current activity
setContentView(R.layout.activity_main)
// fetching xml view by it's id
findViewById<TextView>(R.id.notifications).propName
```

```kotlin
// Inside Activity
private lateinit var binding: ResultProfileBinding

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ResultProfileBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)
}

// then access
binding.name.text = viewModel.name
binding.button.setOnClickListener { viewModel.userClicked() }
```

```kotlin
// inside Fragment
private var _binding: ResultProfileBinding? = null
// This property is only valid between onCreateView and
// onDestroyView.
private val binding get() = _binding!!

override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
    _binding = ResultProfileBinding.inflate(inflater, container, false)
    val view = binding.root
    return view
}

override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}
```

```kotlin
// adding support for viewBinding
android {
    ...
    buildFeatures {
        viewBinding = true
    }
}
```

ViewBinding Guide: https://developer.android.com/topic/libraries/view-binding#activities

### DataBinding vs ViewBinding:
View binding and data binding both generate binding classes that can be used to reference views directly. 

However, view binding is intended to handle simpler use cases and provides the following benefits over data binding:

- Faster compilation: view binding requires no annotation processing, so compile times are faster.
- Ease of use: view binding doesn't require specially tagged XML layout files, so it's faster to adopt in your apps. Once you enable view binding in a module, it applies to all of that module's layouts automatically.

On the other hand, view binding has the following limitations compared to data binding:

- View binding doesn't support layout variables or layout expressions, so it can't be used to declare dynamic UI content straight from XML layout files.
- View binding doesn't support two-way data binding


Because of these considerations, in some cases it's best to use both view binding and data binding in a project. You can use data binding in layouts that require advanced features and use view binding in layouts that don't.

DataBinding Guide: https://developer.android.com/topic/libraries/data-binding#using_the_data_binding_library

### Activity to Activity, Fragment to Activity and Fragment to Fragment Navigation:
Navigation can be done without using Navigation Graph.

* Note: Fragment can assess it's Activity's fn using `(requireActivity() as MainActivity).<activityFn>`
```kotlin
// Activity to Activity
startActivity(Intent(this, RegistrationActivity::class.java))
// closing current activity. The ActivityResult is propagated back to whoever launched you via onActivityResult()
finish()

// Fragment to Activity
startActivity(Intent(getActivity(), RegistrationActivity::class.java))


// Fragment to Fragment From Activity Within a Host View (ie, FrameLayout)
supportFragmentManager.beginTransaction()
                .addToBackStack(SomeNamedFragment::class.java.simpleName())
                .replace(R.id.fragment_container, DestinationFragment())
                .commit()

// Fragment to Fragment From Fragment Within a Host View (ie, FrameLayout)
// use (requireActivity() as MainActivity) to grab the Fragment's Activity
// then call Activity's method where supportFragmentManager.beginTransaction Can be called

```

### Activity/Fragment to Fragment Navigation (Without NavController/NavGraph):
With Fragment, Android Navigation Component is used. Navigation Component can be used with and without navigation graph. But navigation graph is more powerful.

- `FragmentManager` classes (`supportFragmentManager`) are used to navigation between Activity/Fragment to Fragment when `Navigation Graph` is not begin used


Guide `FragmentManager`: https://developer.android.com/guide/fragments/fragmentmanager

### Activity/Fragment to Fragment Navigation (With NavController/NavGraph):
Navigation Graph (Fragment Focused Navigation) represents app's navigation configuration in a recourse file (commonly NavGraph.xml). A host view is used (As Activity's ContentView) where individual `Fragments` are swapped to make the navigation happen. This host view is used to hold the `Navigation Graph` inside FragmentContainerView. FragmentContainerView has `androidx.navigation.fragment.NavHostFragment` Navigation Component Implementation, which by default comes with Navigation Component Package.

Docs: See `Create a navigation graph` section from https://developer.android.com/guide/navigation/migrate

```xml
<!-- navigation_host.xml -->
<FrameLayout
   xmlns:app="http://schemas.android.com/apk/res-auto"
   xmlns:android="http://schemas.android.com/apk/res/android"
   android:layout_height="match_parent"
   android:layout_width="match_parent">

    <androidx.fragment.app.FragmentContainerView
    android:id="@+id/main_content"
    android:name="androidx.navigation.fragment.NavHostFragment"
    app:navGraph="@navigation/navgraph"
    app:defaultNavHost="true"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />

</FrameLayout>
```

* Note: Each activity Needs separate Navigation Graph.
- `NavController` classes (``)

### Brief about JetPack Navigation (NavController):
The Navigation Component consists of three key parts, working together in harmony. They are:

- `Navigation Graph` (New XML resource) - contains all navigation-related information (Destination, Args) in one centralized location per activity.
- `NavHostFragment` (Layout XML view)=> a special widget inside layout. It displays different destinations from Navigation Graph.
- `NavController` (Kotlin/Java object) - keeps track of the current position within the navigation graph. It orchestrates swapping destination content in the NavHostFragment as user moves through a navigation graph.
NavController object is used to navigate into a destination abd show the appropriate destination in the NavHostFragment.

### Brief about Navigation Graph:
Inside Navigation Graph, the Navigation component refers to individual screens as destinations. Destinations can be fragments, activities, or custom destinations. activity destinations are considered terminal destinations, because once you navigate to an activity destination, you are operating within a separate navigation host and graph.

The Navigation component refers to the way in which users get from one destination to another as actions. Actions can also describe transition animations and pop behavior.

navigation XML file anatomy =>
- `<navigation>` is the root node of every navigation graph.
- `<navigation>` contains one or more destinations, represented by `<activity>` or `<fragment>` elements.
- `app:startDestination` is an attribute that specifies the destination that is launched by default when the user first opens the app.


- `android:id` defines an ID for the fragment that you can use to reference the destination elsewhere in this XML and your code
- `android:name` declares the fully qualified class name of the fragment to instantiate when you navigate to that destination.
- `tools:layout` specifies what layout should be shown in the graphical editor.

Note: Its easier to make navigation graph than describe in words

### Attaching NavGraph in NavHostFragment:
Inside a layout file of an Activity, NavHostFragment host the NavGraph and Swapping take place inside of it. 
```xml
<LinearLayout>
    <fragment
        android:id="@+id/my_nav_host_fragment"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:defaultNavHost="true"
        app:navGraph="@navigation/mobile_navigation" />
</LinearLayout>
```

### Using NavController With Navigation Graph:
`NavController` triggers wapping of fragments in the `NavHostFragment`.

NavController can be referenced from fragment, activity or view using

- Fragment.findNavController()
- View.findNavController()
- Activity.findNavController(viewId: Int)

with the NavController, a destination or action ID can be used to navigate.
```kotlin
findNavController().navigate(R.id.flow_step_one_dest, null)

Navigation.createNavigateOnClickListener(R.id.flow_step_one_dest, bundle = null)
```

### NavController With Destination-ID/Action-ID/Destination:
NavigationController can call different method to navigate to the destination

- Using Fragment/View's id (the id of the next navigating Fragment)
```kotlin
findNavController().navigate(R.id.id_of_next_fragment, null)
```

- Using Action id (this provide some extra benefit, like, automatic animation inclusion and safe args auto generation of action class)
```kotlin
findNavController().navigate(resId = R.id.next_destination, args = null, navOptions = options)

// or 
Navigation.createNavigateOnClickListener(R.id.next_destination, null)
```

- Using NavDirections
```kotlin
val action: NavDirections = HomeFragmentDirections.nextAction() // arguments can be supplied here
findNavController().navigate(action) // NavOptions can also fit here
```
### Argument Passing/Receiving With NavController:
Arguments can be Passed using NavController's `navigate()` method's parameter.

Receiving/Reading arguments can be done on the navigated fragment using
- using `argument` bundle of the fragment, like `val argumentName = arguments?.getInt("argumentName")`
- using safe args plugin (which is auto generated). It's type safe (no need like getInt()), so considered best practice for receiving arguments. It's done like this from the receiving/navigated fragment
```kotlin
val safeArgs: FragmentNameArgs by navArgs()
val argumentName = safeArgs.argumentName
```

### Safe Args:
It Generates classes from the Navigation Graph and provide a type safe way to call graph's definition. It provide
- Fragment's argument accessor as `FragmentNameArgs`
- Fragment's Direction class as `FragmentNameDirections` and it's actions as method of it

```kotlin
// Argument class hookup
val safeArgs: FlowStepFragmentArgs by navArgs()
val argumentName = safeArgs.argumentName

// Direction class hookup
val action: NavDirections = HomeFragmentDirections.nextAction() // arguments can be supplied here
findNavController().navigate(action)
```

### Drawer Layout and Navigation

### ProGuard and R8:
R8: Tool to optimize app for release
- Remove unused classes,
- Remove unused resources (images, etc)
- Optimize the code itself
- Obfuscate the code: rename fn and variable's into short unreadable names for release app (make reverse-engineering difficult)

* in build.gradle set buildTypes { release {...}} include `minifyEnable true`, `shrinkEnable true` and add `proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'`

### Ongoing:
- https://developer.android.com/guide/navigation/migrate
- Menu systems

### From:
- Dagger-Codelab-1
- JetPack Compose CodeLab | View to JetPack