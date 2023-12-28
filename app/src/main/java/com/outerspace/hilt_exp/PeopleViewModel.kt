package com.outerspace.hilt_exp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

const val PEOPLE_DATABASE_NAME = "people-database"

class PeopleViewModel(application: Application): AndroidViewModel(application) {
    val dao: PeopleRepositoryDao
    init {
        dao = Room.databaseBuilder(
            application.applicationContext,
            PeopleDatabase::class.java,
            PEOPLE_DATABASE_NAME
        ).build().peopleDao()

        viewModelScope.launch(Dispatchers.IO) {
            val n = dao.countPeople()
            if (n == 0) {
                for(person in testPeopleList()) {
                    dao.addPerson(person)
                }
            }
        }
    }

    private fun testPeopleList(): List<PersonEntity> {
        return listOf(
            PersonEntity(null, "Luis", "Virueña", GenderEnum.MALE),
            PersonEntity(null, "Elvi", "Chávez", GenderEnum.FEMALE),
            PersonEntity(null, "Aldo", "Virueña Chávez", GenderEnum.MALE),
            PersonEntity(null, "Sofi", "Virueña Chávez", GenderEnum.LGTB),
        )
    }

    class Factory(private val application: Application): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PeopleViewModel(application) as T
        }
    }
}