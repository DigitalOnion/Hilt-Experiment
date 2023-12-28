package com.outerspace.hilt_exp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class GenderEnum {
    UNSPECIFIED, MALE, FEMALE, LGTB, FURRY, OTHER
}

@Entity(tableName = "persons")
data class PersonEntity (
    @PrimaryKey var id: Int?,
    @ColumnInfo(name = "first_name", defaultValue = "unknown") var firstName: String,
    @ColumnInfo(name = "last_name", defaultValue = "unknown") var lastName: String,
    @ColumnInfo(name = "gender", defaultValue = "UNSPECIFIED") var gender: GenderEnum,
) {
    fun fullName(): String = "$firstName $lastName"

    override fun equals(other: Any?): Boolean {
        return if (other == null || other !is PersonEntity)
            false
        else
            this.firstName == other.firstName &&
            this.lastName == other.lastName &&
            this.gender == other.gender
    }
}
