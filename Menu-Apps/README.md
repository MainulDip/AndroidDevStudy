### Overview:
Menu navigation can be done with `FragmentManager` or `NavController` using NavigationUI. NavigationUI is NavGraph based.

Working Example:
See Menu-App > Menu-Drawer-Bottom-Navigation
<a href="./Menu-Apps/Menu-Drawer-Bottom-Navigation">Menu-Drawer-Bottom-Navigation</a>
See screen-shot image of the MainActivity

Ongoing > Menu, Drawer, Bottom Navigation > https://developer.android.com/codelabs/android-navigation#0

### Menu:
Menu can be created as xml file using android-studio's `new` => `create resource file` => select type as `Menu`

```xml
<menu ...>

    <item
        android:id="@+id/item_1"
        android:checkable="true"
        android:enabled="true"
        android:icon="@android:drawable/ic_lock_silent_mode_off"
        android:title="DD"
        android:titleCondensed="D"
        android:visible="true"
        app:showAsAction="always|withText" />
    <item
        android:id="@+id/item_2"
        android:icon="@android:drawable/btn_star_big_on"
        android:title="Item"
        android:visible="true"
        app:showAsAction="always" />
</menu>
```
Then use the menu
### Navigation Animation and NavigationOptions:
The Defined animation can be used with NavigationController's NavOption using NavOptionBuilder
```kotlin
val options: NavOptions = navOptions {
    anim {
        enter = R.anim.slide_in_right
        exit = R.anim.slide_out_left
        popEnter = R.anim.slide_in_left
        popExit = R.anim.slide_out_right
    }
}

findNavController().navigate(resId = R.id.destination_id, args = null, navOptions = options)
```
### NavigationUI and navigation-ui-ktx of NavController:
NavigationUI class and the navigation-ui-ktx kotlin extensions is included with Navigation-Component/NavController. NavigationUI has static methods that associate menu items with navigation destinations, and navigation-ui-ktx is a set of extension functions that do the same. 

* If NavigationUI finds a menu item with the same ID as a destination on the current graph, it configures the menu item to navigate to that destination.

* view?.setupWithNavController(navController) => bind view that contain menu with NavigationUI
* setupActionBarWithNavController(navController, appBarConfig) => add drawer/hamburger and back/up icon and set it control by NavigationUI
* AppBarConfiguration => Set's AppBar behaviors
* item.onNavDestinationSelected(...) => add the navigation behavior to the optionMenu
* findNavController(R.id.my_nav_host_fragment).navigateUp(appBarConfiguration) => set back/up button press behaviors

### Activity and Fragment ActionBar Menu Inflate:
Inside Activity, ActionBar Menu is declared/created in it's `onCreateOptionsMenu` override.

`menuInflater.inflate(R.menu.menu_res_file, menu)` is used to inflate and inject menu. If underlying Fragment has it's own actionBar menu defined, inflated menu will take the right side spot following Fragment defined menu items left of it.

Inside Fragment, in `onCreateView` we need to declare `setHasOptionsMenu(true)` then inside `onCreateOptionsMenu` the menu is created/inflated.
```kotlin
override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.main_menu, menu)
}
```

```kotlin
/**
* Injecting menu (in action menu) if there is no NavigationView on current layout (current layout can change ie, for rotation/configuration change)
*/
override fun onCreateOptionsMenu(menu: Menu): Boolean {
    val retValue = super.onCreateOptionsMenu(menu)
    // suppose we don't have nav_view for portrait orientation
    // then we want to inject inflated menu into the action-bar navigation.
    val navigationView = findViewById<NavigationView>(R.id.nav_view)

    if (navigationView == null) {
        menuInflater.inflate(R.menu.overflow_menu, menu)
        return true
    }
    return retValue
}
```

### Navigate from ActionBar Menu Item to a Fragment/Activity/Destination:
The actual navigation is done inside Activity's `onOptionsItemSelected` override.

With NavigationUI and NavGraph Destination/Action, `MenuItem.onNavDestinationSelected` extension fn is used to navigate inside host Fragment's Navigation-Graph. If inflated menu file's item has same id as fragment id, then it will be captured automatically.

```kotlin
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // Checking other menu item's click behavior
        println("pressed menu ${item.title}")

        // Navigating to a navgraph's destination with matching menu-item and destination ids.if no match, apply default behavior with super...
        return item.onNavDestinationSelected(findNavController(R.id.my_nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }
```
Without NavGraph, this can be done manually by switch/match case and findNavController().navigate(...) or supportFragmentManager's way

### Bottom Navigation:
`BottomNavigationView` has slot for menu to include. It will reflect automatically when rendered if it's in inflated/set layout. It is set-up inside activity's `onCreate` using NavigationUI's `setupWithNavController` extension fn which will connect a menu item with the same ID as a destination on the current graph
```kotlin
// Inside activity's onCreate fn
val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
//        bottomNav?.setupWithNavController(navController)
bottomNav?.setupWithNavController(findNavController(R.id.my_nav_host_fragment))
```

### Up/Back Navigation:
Using NavigationUI, Up/Back navigation can be set using `AppBarConfiguration`, `setupActionBarWithNavController(navController, appBarConfig)` and the behavior is set inside of `onSupportNavigateUp` activity's override by calling `findNavController(R.id.my_nav_host_fragment).navigateUp(appBarConfiguration)`

### Custom Back Navigation behavior (override default):
https://developer.android.com/guide/navigation/navigation-custom-back

### Drawer Layout || Navigation Drawer:
Drawer Layout are defined in xml. Then to configure and add the collapsible behavior through appBar, `AppBarConfiguration` and `setupActionBarWithNavController(navController, appBarConfig)` need to be called. Also the `topLevelDestinationIds` (drawer/hamburger icon will only show on these destination) and `DrawerLayout` need to be injected in `AppBarConfiguration`.

Then `setupActionBarWithNavController(navController, appBarConfig)` needs to be called. It will set the behavior (navigation) using the menu-id/destination-id match.
```kotlin
val drawerLayout : DrawerLayout? = findViewById(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(
                setOf(R.id.home_dest, R.id.deeplink_dest),
                drawerLayout)
```


https://developer.android.com/guide/navigation/integrations/ui#add_a_navigation_drawer

### NavigationUI (NavController) || Top AppBar/ToolBar/ActionBar:
The top app bar provides a consistent place along the top of your app for displaying information and actions from the current screen.

NavigationUI provides support for the following top app bar types
- Toolbar
- CollapsingToolbarLayout
- ActionBar

Top App Bar Guide: https://developer.android.com/guide/navigation/integrations/ui

### Option Menu (Top NavBar's Right Side):
The options menu is the primary collection of menu items for an activity. It's where you place actions that have a global impact on the app, such as "Search," "Compose email," and "Settings."

* Top app bar is combination of ActionBar (left side) and Options Menu (Right side). As it seems by my personal observation. But can change in some point.

Option Menu Guide : https://developer.android.com/develop/ui/views/components/menus#options-menu

### CollapsingToolbarLayout:
CollapsingToolbarLayout is a wrapper for `Toolbar` which implements a collapsing app bar. It is designed to be used as a direct child of a `AppBarLayout`. CollapsingToolbarLayout contains the following features

Docs: https://developer.android.com/reference/com/google/android/material/appbar/CollapsingToolbarLayout
Guide: https://developer.android.com/guide/navigation/integrations/ui#include_collapsingtoolbarlayout

### ActionBar:
A primary toolbar within the activity that may display the activity title, application-level navigation affordances, and other interactive items.

From your activity, you can retrieve an instance of ActionBar by calling getActionBar()/supportActionBar().

Docs: https://developer.android.com/reference/android/app/ActionBar
Docs Guide: https://developer.android.com/develop/ui/views/components/appbar

```kotlin
// setting an action bar without app title, where the layout file has a toolbar view
val toolbar = findViewById<Toolbar>(R.id.toolbar)
    setSupportActionBar(toolbar) // setting an action bar
    // by default the actionBar's title will be app name defined in manifest.xml
    supportActionBar?.setDisplayShowTitleEnabled(false); // removing actionBar/toolBar's title as app name
```

### ActionMode || Contextual Menu:
Represents a contextual mode of the user interface. Action modes can be used to provide alternative interaction modes and replace parts of the normal UI until finished. Examples of good action modes include text selection and contextual actions.
Guide: https://developer.android.com/develop/ui/views/components/menus#context-menu

There are two ways to provide contextual actions:

- In a `floating context menu`. A menu appears as a floating list of menu items, similar to a dialog, when the user performs a touch & hold on a view that declares support for a context menu. Users can perform a contextual action on one item at a time.
- In the `contextual action mode`. This mode is a system implementation of ActionMode that displays a contextual action bar, or CAB, at the top of the screen with action items that affect the selected item(s). When this mode is active, users can perform an action on multiple items at once, if your app supports that.

### Popup Menu:
Guide: https://developer.android.com/develop/ui/views/components/menus#PopupMenu

### Next Todo:
=> Come back after Dagger2 and Hilt
=> Also JetPack Compose