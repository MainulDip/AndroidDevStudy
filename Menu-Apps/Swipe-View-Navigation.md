### swipe views with tabs using ViewPager2:
https://developer.android.com/guide/navigation/navigation-swipe-view-2

### Sunflower App Example:
https://github.com/android/codelab-android-compose
```kotlin
class HomeViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        val tabLayout = binding.tabs
        val viewPager = binding.viewPager

        viewPager.adapter = SunflowerPagerAdapter(this)

        // Set the icon and text for each tab
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setIcon(getTabIcon(position))
            tab.text = getTabTitle(position)
        }.attach()


        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        return binding.root
    }

    private fun getTabIcon(position: Int): Int {
        return when (position) {
            MY_GARDEN_PAGE_INDEX -> R.drawable.garden_tab_selector
            PLANT_LIST_PAGE_INDEX -> R.drawable.plant_list_tab_selector
            else -> throw IndexOutOfBoundsException()
        }
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            MY_GARDEN_PAGE_INDEX -> getString(R.string.my_garden_title)
            PLANT_LIST_PAGE_INDEX -> getString(R.string.plant_list_title)
            else -> null
        }
    }
}

// Adapter
class SunflowerPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    /**
     * Mapping of the ViewPager page indexes to their respective Fragments
     */
    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        MY_GARDEN_PAGE_INDEX to { GardenFragment() },
        PLANT_LIST_PAGE_INDEX to { PlantListFragment() }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}
```
```xml
<?xml version="1.0" encoding="utf-8"?>

<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <androidx.coordinatorlayout.widget.CoordinatorLayout
        android:id="@+id/coordinator_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:fitsSystemWindows="true">

        <androidx.viewpager2.widget.ViewPager2
            android:id="@+id/view_pager"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layout_behavior="@string/appbar_scrolling_view_behavior"/>

        <com.google.android.material.appbar.AppBarLayout
            android:id="@+id/app_bar_layout"
            style="@style/Widget.MaterialComponents.AppBarLayout.PrimarySurface"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:fitsSystemWindows="true">

            <com.google.android.material.appbar.CollapsingToolbarLayout
                android:id="@+id/toolbar_layout"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:layout_scrollFlags="scroll|snap"
                app:toolbarId="@id/toolbar">

                <androidx.appcompat.widget.Toolbar
                    android:id="@+id/toolbar"
                    style="@style/Widget.MaterialComponents.Toolbar.PrimarySurface"
                    android:layout_width="match_parent"
                    android:layout_height="?attr/actionBarSize"
                    app:layout_collapseMode="parallax">

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="?attr/actionBarSize"
                    android:gravity="center"
                    android:text="@string/app_name"
                    android:textAppearance="?attr/textAppearanceHeadline5" />

                </androidx.appcompat.widget.Toolbar>

            </com.google.android.material.appbar.CollapsingToolbarLayout>

            <!-- Override tabIconTint attribute of style with selector -->
            <com.google.android.material.tabs.TabLayout
                android:id="@+id/tabs"
                style="@style/Widget.MaterialComponents.TabLayout.PrimarySurface"
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize" />

        </com.google.android.material.appbar.AppBarLayout>

    </androidx.coordinatorlayout.widget.CoordinatorLayout>

</layout>
```