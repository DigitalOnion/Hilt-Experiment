package com.outerspace.hilt_exp

import android.util.Log
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.printToLog
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch


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

    @Test
    fun AddPetsButton_Test() {
        val activity = mainActivityTestRule.activity

        val doneSignal = CountDownLatch(1)

        runTest {
            var list: List<PersonEntity> = listOf()

            withContext(Dispatchers.Main) {
                list = activity.getPeopleList()
            }
            val lastIndex = list.size - 1

            mainActivityTestRule
                .onNodeWithText("Add Pets to the Family")
                .assertExists()
            mainActivityTestRule
                .onNodeWithTag(LIST_OF_PERSONS)
                .onChildren()[lastIndex]
                .assertTextContains(list.last().firstName, substring = true)

            Log.d("LUIS", list.last().firstName)

            withContext(Dispatchers.Main) {
                activity.observePersonAdded { person ->
                    Log.d("LUIS", "Person added: ${person.firstName}" )
                    doneSignal.countDown()
                }
            }

            mainActivityTestRule
                .onNodeWithText("Add Pets to the Family")
                .performClick()

            doneSignal.await()

            var list2: List<PersonEntity> = listOf()
            withContext(Dispatchers.Main) {
                list2 = activity.getPeopleList()
            }
            val lastIndex2 = list.size - 1

            assert(lastIndex < lastIndex2)

            mainActivityTestRule
                .onNodeWithTag(LIST_OF_PERSONS)
                .onChildren()[lastIndex2]
                .assertTextContains(list.last().firstName, substring = true)
        }
    }

}


