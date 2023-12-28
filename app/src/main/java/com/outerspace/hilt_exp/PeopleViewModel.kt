package com.outerspace.hilt_exp

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PeopleViewModel
    @Inject constructor(private val peopleRepository: PeopleRepository)
    :ViewModel() {
    
    suspend fun addPerson(person: PersonEntity) = peopleRepository.addPerson(person)
    suspend fun getPerson(id: Int): PersonEntity = peopleRepository.getPerson(id)
    suspend fun countPeople(): Int = peopleRepository.countPeople()
    suspend fun getAll(): List<PersonEntity> = peopleRepository.getAll()
    suspend fun deletePerson(id: Int) = peopleRepository.deletePerson(id)
    suspend fun deleteAll() = peopleRepository.deleteAll()

    fun testPeopleList(): List<PersonEntity> {
        return listOf(
            PersonEntity(null, "Luis", "Virueña", GenderEnum.MALE),
            PersonEntity(null, "Elvi", "Chávez", GenderEnum.FEMALE),
            PersonEntity(null, "Aldo", "Virueña Chávez", GenderEnum.MALE),
            PersonEntity(null, "Sofi", "Virueña Chávez", GenderEnum.LGTB),
        )
    }
}
