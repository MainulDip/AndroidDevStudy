## Android-View Mini Docs :
Android View elements are mostly self explanatory. So just some minimal quick start and overview will be good

### Layout Type :
> Relative | Constraint | Linear | 

> Linear: Elements placed one after one. Can set orientation vertical/horizontal. Attributes: layout_weight, background etc. Inside LinearLayout, Relative and Constraint Layout are also placeable.

> Constraint: Better to work on Design view rather than xml editor. Options: baseline alignment, precise positioning using layout slider (horizontal/vertical), constraint to guideline (help->guideline), image ratio placeholder with constraint element.

### Vectors & Images:
Add vectors through the res/drawable and images through res/minimap.

### Adapters: ArrayAdapter | 
used to fetch and inject data to listview or recycler view. 

### XML Files:

#### Images and Icons:
> From Drawable -> new Image asset or Vector asset

#### Localization and Strings: 

#### Menu: 
> Tips: For overriding menu realted methods do ctrl+o and search for menu and menuitem 

```java
/**
* Menu will be created using the MenuInflater from the activity
* Menu XML: create a directory named "menu" and generate the menu resource file.
*/

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_menu, menu);
    return true;
}

@Override
public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
        case R.id.main_menu:
        case R.id.menu_alerm:
            Toast.makeText(this, item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
}
```

Menu xml:
```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <item
        android:title="Settings"
        android:id="@+id/main_menu"
        android:icon="@drawable/icn_main_menu"
        app:showAsAction="always"/>

    <item
        android:title="Alerm"
        android:id="@+id/menu_alerm"
        android:icon="@drawable/icn_alerm"
        app:showAsAction="always"/>
</menu>
```

#### Multiple Layout File | Variation
We can define multiple xml file for different orientaion (proatrait/landscape), and for different versions.

> Variation are created by using "create landscape/tablet/other variation"

