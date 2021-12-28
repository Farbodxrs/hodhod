package ir.hodhod.hodhod.repositories.local.domain

import com.google.android.gms.maps.model.LatLng
import ir.hodhod.hodhod.data.models.MessageModel

interface IMessageRepository {

    suspend fun insertMessage(messageModel: MessageModel): Unit?
    suspend fun getAllMessagesByRoomKey(roomKey: String): List<MessageModel>?
    suspend fun getAllLocationsByRoomKey(roomKey: String): Map<String, LatLng?>?
    suspend fun getAllUsernamesByRoomKey(roomKey: String): List<String>?
}