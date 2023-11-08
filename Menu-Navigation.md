### Overview:
Menu navigation can be done with `FragmentManager` or `NavController` using NavigationUI. NavigationUI is NavGraph based.

### Drawer Layout || Navigation Drawer:
https://developer.android.com/guide/navigation/integrations/ui#add_a_navigation_drawer

### NavigationUI (NavController) || Top app bar:
The top app bar provides a consistent place along the top of your app for displaying information and actions from the current screen.

NavigationUI provides support for the following top app bar types
- Toolbar
- CollapsingToolbarLayout
- ActionBar

Top App Bar Guide: https://developer.android.com/guide/navigation/integrations/ui

### Option Menu:
The options menu is the primary collection of menu items for an activity. It's where you place actions that have a global impact on the app, such as "Search," "Compose email," and "Settings."

Option Menu Guide : https://developer.android.com/develop/ui/views/components/menus#options-menu

### CollapsingToolbarLayout:
CollapsingToolbarLayout is a wrapper for `Toolbar` which implements a collapsing app bar. It is designed to be used as a direct child of a `AppBarLayout`. CollapsingToolbarLayout contains the following features

Docs: https://developer.android.com/reference/com/google/android/material/appbar/CollapsingToolbarLayout
Guide: https://developer.android.com/guide/navigation/integrations/ui#include_collapsingtoolbarlayout

### ActionBar:
A primary toolbar within the activity that may display the activity title, application-level navigation affordances, and other interactive items.

From your activity, you can retrieve an instance of ActionBar by calling getActionBar().

Docs: https://developer.android.com/reference/android/app/ActionBar
Docs Guide: https://developer.android.com/develop/ui/views/components/appbar

### ActionMode || Contextual Menu:
Represents a contextual mode of the user interface. Action modes can be used to provide alternative interaction modes and replace parts of the normal UI until finished. Examples of good action modes include text selection and contextual actions.
Guide: https://developer.android.com/develop/ui/views/components/menus#context-menu

There are two ways to provide contextual actions:

- In a `floating context menu`. A menu appears as a floating list of menu items, similar to a dialog, when the user performs a touch & hold on a view that declares support for a context menu. Users can perform a contextual action on one item at a time.
- In the `contextual action mode`. This mode is a system implementation of ActionMode that displays a contextual action bar, or CAB, at the top of the screen with action items that affect the selected item(s). When this mode is active, users can perform an action on multiple items at once, if your app supports that.

### Popup Menu:
Guide: https://developer.android.com/develop/ui/views/components/menus#PopupMenu
### Bottom Navigation:
Guide: https://www.geeksforgeeks.org/bottom-navigation-bar-in-android/.
Docs: https://developer.android.com/reference/com/google/android/material/bottomnavigation/BottomNavigationView
