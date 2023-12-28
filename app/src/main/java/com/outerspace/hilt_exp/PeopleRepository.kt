package com.outerspace.hilt_exp

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase

const val PEOPLE_DATABASE_VERSION = 1

@Dao
interface PeopleRepositoryDao {
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
}

@Database(entities = [PersonEntity::class], version = PEOPLE_DATABASE_VERSION, exportSchema = false)
abstract class PeopleDatabase: RoomDatabase() {
    abstract fun peopleDao(): PeopleRepositoryDao
}
