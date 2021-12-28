package ir.hodhod.hodhod.repositories.local.persistence

import ir.hodhod.hodhod.data.db.executeQuery
import ir.hodhod.hodhod.data.mapper.MessageLocalMapper
import ir.hodhod.hodhod.data.models.MessageModel
import ir.hodhod.hodhod.repositories.local.domain.IMessageRepository
import javax.inject.Inject

class MessageRepository @Inject constructor(private val messageDao: MessageDao) :
    IMessageRepository {

    override suspend fun insertMessage(messageModel: MessageModel) =
        messageDao.executeQuery({
            insertMessage(MessageLocalMapper.fromDomain(messageModel))
        })

    override suspend fun getAllMessagesByRoomKey(roomKey: String) =
        messageDao.executeQuery({
            getAllMessagesByRoomKey(roomKey).map { MessageLocalMapper.toDomain(it) }
        })

    override suspend fun getAllLocationsByRoomKey(roomKey: String) =
        messageDao.executeQuery({
            getAllLocationMessagesByRoomKey(roomKey).associate { it.sender to it.location }
        })

    override suspend fun getAllUsernamesByRoomKey(roomKey: String) =
        messageDao.executeQuery({
            getAllUsernamesByRoomKey(roomKey)
        })
}