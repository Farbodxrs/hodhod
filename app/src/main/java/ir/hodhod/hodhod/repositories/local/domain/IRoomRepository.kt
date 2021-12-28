package ir.hodhod.hodhod.repositories.local.domain

import ir.hodhod.hodhod.data.models.RoomModel

interface IRoomRepository {

    suspend fun insertRoom(roomModel: RoomModel): Unit?
    suspend fun deleteRoom(roomModel: RoomModel): Unit?
    suspend fun getAllRooms(): List<RoomModel>?
}