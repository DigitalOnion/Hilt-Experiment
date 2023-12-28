package com.outerspace.hilt_exp.quarentine

import com.outerspace.hilt_exp.PersonEntity

class PeopleRepository: PeopleRepoInterface {
    private val map: MutableMap<Int, PersonEntity> = mutableMapOf()

    override suspend fun addPerson(person: PersonEntity) {
        if (person.id == null) {
            person.id = if (map.isNotEmpty()) map.keys.max() + 1 else 1
        }
        map[person.id!!] = person
    }

    override suspend fun getPerson(id: Int): PersonEntity = map[id]!!

    override suspend fun countPeople(): Int = map.size

    override suspend fun getAll(): List<PersonEntity> = map.values.toList()

    override suspend fun deletePerson(id: Int) { map.remove(id) }

    override suspend fun deleteAll() = map.clear()
}

