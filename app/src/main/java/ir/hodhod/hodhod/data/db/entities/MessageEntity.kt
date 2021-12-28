package ir.hodhod.hodhod.data.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng


@Entity(tableName = "message")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val sender: String,
    val content: String,
    val time: Long,
    val roomKey: String,
    @Embedded
    val location: LatLng? = null
)
