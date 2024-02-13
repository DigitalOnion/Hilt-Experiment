package com.outerspace.hilt_exp

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import javax.inject.Inject

const val PEOPLE_DATABASE_VERSION = 1

@Dao
interface PeopleDao {
    @Insert
    suspend fun addPerson(person: PersonEntity)

    @Query("Select * from persons where id = :id")
    suspend fun getPerson(id: Int): PersonEntity

    @Query("Select count(*) from persons")
    suspend fun countPeople(): Int

    @Query("Select * from persons")
    suspend fun getAll(): List<PersonEntity>

    @Query("Delete from persons where id = :id")
    suspend fun deletePerson(id: Int)

    @Query("Delete from persons")
    suspend fun deleteAll()

    @Query("Delete from persons where gender = :gender")
    suspend fun deleteGender(gender: GenderEnum)
}

@Database(entities = [PersonEntity::class], version = PEOPLE_DATABASE_VERSION, exportSchema = false)
abstract class PeopleDatabase: RoomDatabase() {
    abstract fun peopleDao(): PeopleDao
}

class PeopleRepository @Inject constructor(
    private val dao: PeopleDao,
) {
    suspend fun addPerson(person: PersonEntity) = dao.addPerson(person)
    suspend fun getPerson(id: Int): PersonEntity = dao.getPerson(id)
    suspend fun countPeople(): Int = dao.countPeople()
    suspend fun getAll(): List<PersonEntity> = dao.getAll()
    suspend fun deletePerson(id: Int) = dao.deletePerson(id)
    suspend fun deleteAll() = dao.deleteAll()
    suspend fun deleteGender(gender: GenderEnum) = dao.deleteGender(gender)
}
