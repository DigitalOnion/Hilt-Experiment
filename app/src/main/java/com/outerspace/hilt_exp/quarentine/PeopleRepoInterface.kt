package com.outerspace.hilt_exp.quarentine

import com.outerspace.hilt_exp.PersonEntity

interface PeopleRepoInterface {
    suspend fun addPerson(person: PersonEntity)
    suspend fun getPerson(id: Int): PersonEntity
    suspend fun countPeople(): Int
    suspend fun getAll(): List<PersonEntity>
    suspend fun deletePerson(id: Int)
    suspend fun deleteAll()
}