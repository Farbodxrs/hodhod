package ir.hodhod.hodhod.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "room")
data class RoomEntity(
    @PrimaryKey
    val roomKey: String,
    val time: Long = 0
) {
    // we override it here to check only primary key for equality
    override fun equals(other: Any?): Boolean {
        return other is RoomEntity && other.roomKey == roomKey
    }

    override fun hashCode(): Int {
        return roomKey.hashCode()
    }
}
