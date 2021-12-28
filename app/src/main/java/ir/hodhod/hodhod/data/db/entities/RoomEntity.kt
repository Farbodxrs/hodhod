package ir.hodhod.hodhod.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "room")
data class RoomEntity(
    @PrimaryKey
    val roomKey: String,
    val time: Long
)
