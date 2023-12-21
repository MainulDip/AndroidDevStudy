### Migrating From View/XML to Compose:
The recommendation is - an `incremental migration` where Compose and View co-exist in same codebase until the app is fully in Compose.

### Migration Strategy:
1. Build New Screen in Compose: 
View and Compose can co-exists on the same screen. Ex. If there is a existing `RecyclerView`, a Composable can be added there while keeping the other item same. Then migrate slowly.
2. Build Library of common UI components:
identify reusable components to promote reuse across the app so that shared components have a single source of truth. 
3. Replace existing features one screen at a time:
- Simple Screen: This is the easiest part.
- Mixed View and Compose: Start migrating elements mixed screen piece-by-piece. If there is a screen with only a `subtree` in Compose, continue migrating other parts of the `tree` until the entire UI is in Compose. This is called the `bottom-up` approach of migration.

### Working App and Structure:
We're migrating the `Sunflower` https://github.com/android/codelab-android-compose app from view to JetPack Compose.

Sunflower App Structure:
- Implement Material's ViewPager2 for `Swipe View` with `tab` (https://developer.android.com/guide/navigation/navigation-swipe-view-2)
- Navigation Component and Navigation Graph
- Implemented Room Database and a `SeedDatabaseWorker` as `CoroutineWorker` to fetch data from a local json file form `./assets/plants.json`
- The ViewPager's adapter implements the 
### map[key]?.invoke():

### ComposeView:
`ComposeView`: an Android View that can host Compose UI content using its setContent method. This used to integrate Compose View inside of xml view.

1. First add the ComposeView in the targeted xml file.
2. Then access the ComposeView from kotlin file using `binding` or `findViewById` whatever, and call the `setContent` callback.
3. If Compose view needs ViewModel access, pass it when instantiating the ComposeView as contractor param.
```xml
<androidx.compose.ui.platform.ComposeView
        android:id="@+id/compose_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

Now the `binding` has access to this `compose_view` is as `composeView` in Fragment/Activity class. Using this, a composable view can be injected into the layout.
```kotlin
class PlantDetailFragment : Fragment() {
    // ...
    override fun onCreateView(...): View? {
        val binding = DataBindingUtil.inflate<FragmentPlantDetailBinding>(
            inflater, R.layout.fragment_plant_detail, container, false
        ).apply {
            // ...
            composeView.setContent {
                // You're in Compose world!
                MaterialTheme {
                    PlantDetailDescription()
                }
            }
        }
        // ...
    }
}
```
### Matching Of Compose Modifier with XML layout properties:
`fillMaxWidth` is similar to the `match_parent`
`padding` for `marginStart` and `marginEnd`
`wrapContentWidth` is similar to `gravity` and `center_horizontal` to align text center

Note: Compose provides convenient methods to get values from the dimens.xml and strings.xml files, namely: dimensionResource(id) and stringResource(id).



### Compose with ViewModel and LiveData + DataBinding
Composables don't have their own ViewModel instances, the same instance is need to share from Fragment/Activity to the ComposeView.
* Pass the `ViewModel` from Activity/Fragment to `Composable Fn` as parameter.

* When the `ViewModel` is injected to a Composable, LiveData is available too. Use `LiveData.observeAsState()` to observe changes. It will represent its values as a `State` object.

### Using LiveData with ComposeView:
As values emitted by the LiveData can be null (.value: T?), you'd need to wrap its usage in a null check. Because of that, and for reusability, it's best to split the LiveData consumption and listening in different composable.
```kotlin
val plant: Plant? by plantDetailViewModel.plant.observeAsState()
plant?.let {
    PlantDetailContent(it)
}

// without delegation `by` assignment, LiveData<T>.value : T? can be used
```

### Applying String/Dimension/Plural Resources:
`stringResource(R.string.string_name)` is used for strings

`pluralStringResource()` and `dimensionResource()` can be used too in compose.

```kotlin
val normalPadding = dimensionResource(R.dimen.margin_normal)

Text(
    text = stringResource(R.string.watering_needs_prefix),
    modifier = centerWithPaddingModifier.padding(top = normalPadding)
)

val wateringIntervalText = pluralStringResource(
    R.plurals.watering_needs_suffix, wateringInterval, wateringInterval
)

/* xml for plurals */ /*
<plurals name="watering_needs_suffix">
        <item quantity="one" translation_description="Detail Description">every day</item>
        <item quantity="other" translation_description="Detail Description">every %d days</item>
</plurals>
*/

```
### Creating AndroidView (xml) programmatically to feed compose:
Some thing are not available to Compose yet, like `rendering html`, etc.

Like this type of cases, we can create view programmatically or use viewBinding with xml view to feed the compose.

`AndroidView `allows to construct a View in its factory lambda. It also provides an update lambda which gets invoked when the View has been inflated and on subsequent recompositions. ViewBinding can also be used when using the `xml` view.

```kotlin
@Composable
private fun PlantDescription(description: String) {
    // Remembers the HTML formatted description. Re-executes on a new description
    val htmlDescription = remember(description) {
        HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    // Displays the TextView on the screen and updates with the HTML description when inflated
    // Updates to htmlDescription will make AndroidView recompose and update the text
    AndroidView(
        factory = { context ->
            TextView(context).apply {
                movementMethod = LinkMovementMethod.getInstance()
            }
        },
        update = {
            it.text = htmlDescription
        }
    )
}

@Preview
@Composable
private fun PlantDescriptionPreview() {
    MaterialTheme {
        PlantDescription("HTML<br><br>description")
    }
}
```