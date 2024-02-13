package com.outerspace.hilt_exp

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.testing.TestInstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [PeopleRepoModule::class]
)
object FakePeopleRepoModule {
    @Provides
    fun providePeopleDatabase(@ApplicationContext appContext: Context): PeopleDatabase =
        Room.inMemoryDatabaseBuilder(
            appContext,
            PeopleDatabase::class.java,
        ).build()

    @Provides
    fun providePeopleDao(db: PeopleDatabase): PeopleDao {
        val dao = db.peopleDao()
        runBlocking {
            dao.addPerson(PersonEntity(null, "Zorg", "XRT347", GenderEnum.LGTB))
        }
        return dao
    }
}