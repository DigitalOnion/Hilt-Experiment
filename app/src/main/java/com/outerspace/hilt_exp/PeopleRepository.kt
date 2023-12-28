package com.outerspace.hilt_exp

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
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
}
