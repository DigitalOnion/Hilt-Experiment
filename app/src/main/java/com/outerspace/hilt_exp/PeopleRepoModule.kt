package com.outerspace.hilt_exp

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

const val PEOPLE_DATABASE_NAME = "people-database"

@Module
@InstallIn(ViewModelComponent::class)
object PeopleRepoModule {
    @Provides
    fun providePeopleDatabase(@ApplicationContext appContext: Context): PeopleDatabase =
        Room.databaseBuilder(
            appContext,
            PeopleDatabase::class.java,
            PEOPLE_DATABASE_NAME
        ).build()

    @Provides
    fun providePeopleDao(db: PeopleDatabase) = db.peopleDao()
}