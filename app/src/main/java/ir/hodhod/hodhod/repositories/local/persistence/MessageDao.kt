package ir.hodhod.hodhod.repositories.local.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.hodhod.hodhod.data.db.GeneralDao
import ir.hodhod.hodhod.data.db.entities.MessageEntity

@Dao
interface MessageDao : GeneralDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(messageEntity: MessageEntity)

    @Query("SELECT * FROM message WHERE roomKey = :roomKey ORDER BY time")
    suspend fun getAllMessagesByRoomKey(roomKey: String): List<MessageEntity>

    @Query("SELECT * FROM message WHERE roomKey = :roomKey GROUP BY sender ORDER BY time DESC LIMIT 1")
    suspend fun getAllLocationMessagesByRoomKey(roomKey: String): List<MessageEntity>

    @Query("SELECT DISTINCT sender FROM message WHERE roomKey = :roomKey")
    suspend fun getAllUsernamesByRoomKey(roomKey: String): List<String>
}