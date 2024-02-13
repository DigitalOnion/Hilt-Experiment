package com.outerspace.hilt_exp

import android.util.Log
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToString
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import javax.inject.Inject


@HiltAndroidTest
class MainActivityTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var mainActivityTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun initialize() {
        hiltRule.inject()
    }

    // NOTE: the FakePeopleRepoModule replaces the PeopleRepoModule
    //      which is injected for the tests. The fake creates a new
    //      in-memory database and fills it up with one person: Zorg
    //      this does not interfere with the app's database which
    //      keeps its data untouched by the test.
    //
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addPetsButton_Test() {
        val activity = mainActivityTestRule.activity

        runTest(UnconfinedTestDispatcher()) {
            var list: List<PersonEntity> = listOf()
            list = activity.getPeople()

            mainActivityTestRule
                .onNodeWithText("Add Pets to the Family")
                .assertExists()
            mainActivityTestRule
                .onNodeWithTag(LIST_OF_PERSONS)
                .onChildren()[list.lastIndex]
                .assertTextContains(list.last().firstName, substring = true)

            Log.d("LUIS", list.last().firstName)

            mainActivityTestRule
                .onNodeWithText("Add Pets to the Family")
                .performClick()

            advanceUntilIdle()

            mainActivityTestRule
                .onNodeWithTag(LIST_OF_PERSONS)
                .onChildren()[list.lastIndex + 1]
                .assertTextContains("Bimbo", substring = true)
        }
    }
}


