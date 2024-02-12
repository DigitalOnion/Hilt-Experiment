package com.outerspace.hilt_exp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class PeopleViewModel
    @Inject constructor(private val peopleRepository: PeopleRepository)
    :ViewModel() {

    val livePersonAdded = MutableLiveData<PersonEntity>()

    suspend fun addPerson(person: PersonEntity) {
        peopleRepository.addPerson(person)
        if (livePersonAdded.hasObservers()) {
            viewModelScope.launch(Dispatchers.Main) {
                livePersonAdded.value = person
            }
        }
    }

    suspend fun getPerson(id: Int): PersonEntity = peopleRepository.getPerson(id)
    suspend fun countPeople(): Int = peopleRepository.countPeople()
    suspend fun getAll(): List<PersonEntity> = peopleRepository.getAll()
    suspend fun deletePerson(id: Int) = peopleRepository.deletePerson(id)
    suspend fun deleteAll() = peopleRepository.deleteAll()

    suspend fun initializePeopleTable() {
        deleteAll()
        for(person in testPeopleList()) {
            addPerson(person)
        }
    }

    fun testPeopleList(): List<PersonEntity> {
        return listOf(
            PersonEntity(null, "Luis", "Virueña", GenderEnum.MALE),
            PersonEntity(null, "Elvi", "Chávez", GenderEnum.FEMALE),
            PersonEntity(null, "Aldo", "Virueña Chávez", GenderEnum.MALE),
            PersonEntity(null, "Sofi", "Virueña Chávez", GenderEnum.LGTB),
        )
    }

    fun testPetList(): List<PersonEntity> {
        return listOf(
            PersonEntity(null, "Bimbo", "Virueña Chávez", GenderEnum.FURRY),
            PersonEntity(null, "Hela", "Virueña Chávez", GenderEnum.FURRY),
            PersonEntity(null, "Toy", "Virueña Silva", GenderEnum.FURRY)
        )
    }
}
