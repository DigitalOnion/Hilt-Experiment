package com.outerspace.hilt_exp

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PeopleViewModelTest {

    private lateinit var peopleVM: PeopleViewModel

    @Before
    fun initializeTests() {
        val dao = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            PeopleDatabase::class.java)
            .build()
            .peopleDao()

        peopleVM = PeopleViewModel(PeopleRepository(dao))
    }

    fun testList(): List<PersonEntity> {
        return listOf(
            PersonEntity(null, "Bimbo", "Wirwueña", GenderEnum.FURRY),
            PersonEntity(null, "Hela", "Wirwueña", GenderEnum.FURRY),
            PersonEntity(null, "Toy", "Wirwueña", GenderEnum.FURRY),
        )
    }

    @Test
    fun addPeopleTest() = runTest {
        val persons = testList()
        for (person in persons) {
            peopleVM.addPerson(person)
        }
        val n = peopleVM.countPeople()
        assertEquals("Not all people were added", n, persons.size)
    }

    @Test
    fun getPersonTest() = runTest {
        val persons = testList()
        for (person in persons) {
            peopleVM.addPerson(person)
        }
        val person2 = peopleVM.getPerson(2)
        assertTrue("getPerson returned the wrong person", persons[1]==person2)
    }

    // the rest... I will do later, this is only an exercise
//    @Test
//    fun getAllTest() = runTest {
//
//    }
//
//    @Test
//    fun deletePersonTest() = runTest {
//
//    }
//
//    @Test
//    fun deleteAllTest() = runTest {
//
//    }

}