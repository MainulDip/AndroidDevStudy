package com.example.wordsapp

import android.util.Log
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NavigationTests {
    private lateinit var navController : TestNavHostController

    /**
     * avoid repetitive code with annotations in JUnit Test
     * @BeforeClass runs before the class is initialized
     * @Before runs before every test functions
     * @After runs after every test function
     * @AfterClass runs after all the test in the test class
     */
    @Before
    fun setup() {
        Log.d("TAG: @Before", "Setting Up Test's Before actions ")
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val letterListScenario = launchFragmentInContainer <LetterListFragment> (themeResId = R.style.Theme_Words)

        letterListScenario.onFragment { fragment ->
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
    }

    @Test
    fun navigate_to_words_nav_component() {
        Log.d("TAG: Actual Test Function", "Testing Here With Espresso ")
        onView(withId(R.id.recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(2, click())
            )

        assertEquals(navController.currentDestination?.id, R.id.wordListFragment)
    }

    @After
    fun tearDowning() {
        Log.d("TAG: @After", "This code will run after the test")
    }

}