package com.outerspace.hilt_exp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class GenderEnum {
    UNSPECIFIED, MALE, FEMALE, LGTB, FURRY, OTHER
}

@Entity(tableName = "persons")
data class PersonEntity (
    @PrimaryKey val id: Int?,
    @ColumnInfo(name = "first_name", defaultValue = "unknown") val firstName: String,
    @ColumnInfo(name = "last_name", defaultValue = "unknown") val lastName: String,
    @ColumnInfo(name = "gender", defaultValue = "UNSPECIFIED") val gender: GenderEnum,
) {
    fun fullName(): String = "$firstName $lastName"
}
