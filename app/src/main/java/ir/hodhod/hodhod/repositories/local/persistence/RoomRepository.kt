package ir.hodhod.hodhod.repositories.local.persistence

import ir.hodhod.hodhod.data.db.executeQuery
import ir.hodhod.hodhod.data.mapper.RoomLocalMapper
import ir.hodhod.hodhod.data.models.RoomModel
import ir.hodhod.hodhod.repositories.local.domain.IRoomRepository
import javax.inject.Inject

class RoomRepository @Inject constructor(private val roomDao: RoomDao) : IRoomRepository {

    override suspend fun insertRoom(roomModel: RoomModel) =
        roomDao.executeQuery({
            insertRoom(RoomLocalMapper.fromDomain(roomModel))
        })

    override suspend fun deleteRoom(roomModel: RoomModel) =
        roomDao.executeQuery({
            deleteRoom(RoomLocalMapper.fromDomain(roomModel))
        })

    override suspend fun getAllRooms() =
        roomDao.executeQuery({
            getAllRooms().map { RoomLocalMapper.toDomain(it) }
        })
}