package ir.hodhod.hodhod.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.hodhod.hodhod.data.models.RoomModel
import ir.hodhod.hodhod.repositories.local.persistence.RoomRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoomListViewModel @Inject constructor(private val roomRepository: RoomRepository) :
    ViewModel() {

    val deleteRespond = MutableLiveData<Unit>()
    val deleteError = MutableLiveData<Unit>()

    val getRoomsRespond = MutableLiveData<List<RoomModel>>()
    val getRoomsError = MutableLiveData<Unit>()

    fun insertRoom(roomModel: RoomModel) {
        viewModelScope.launch {
            roomRepository.insertRoom(roomModel)
        }
    }

    fun deleteRoom(roomModel: RoomModel) {
        viewModelScope.launch {
            roomRepository.deleteRoom(roomModel)?.also { deleteRespond.value = Unit }
                ?: apply { deleteError.value = Unit }
        }
    }

    fun getAllRooms() {
        viewModelScope.launch {
            roomRepository.getAllRooms()?.also {
                getRoomsRespond.value = it
            } ?: apply { getRoomsError.value = Unit }
        }
    }
}