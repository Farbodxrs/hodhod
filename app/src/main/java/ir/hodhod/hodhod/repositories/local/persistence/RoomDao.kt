package ir.hodhod.hodhod.repositories.local.persistence

import androidx.room.*
import ir.hodhod.hodhod.data.db.GeneralDao
import ir.hodhod.hodhod.data.db.entities.RoomEntity

@Dao
interface RoomDao : GeneralDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(roomEntity: RoomEntity)

    @Delete
    suspend fun deleteRoom(roomEntity: RoomEntity)

    @Query("SELECT * FROM room")
    suspend fun getAllRooms(): List<RoomEntity>
}