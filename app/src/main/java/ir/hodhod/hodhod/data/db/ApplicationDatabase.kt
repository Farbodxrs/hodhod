package ir.hodhod.hodhod.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.hodhod.hodhod.data.db.entities.MessageEntity
import ir.hodhod.hodhod.data.db.entities.RoomEntity
import ir.hodhod.hodhod.repositories.local.persistence.MessageDao
import ir.hodhod.hodhod.repositories.local.persistence.RoomDao


@Database(entities = [RoomEntity::class, MessageEntity::class], version = 1)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun roomDao(): RoomDao
    abstract fun messageDao(): MessageDao
}