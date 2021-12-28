package ir.hodhod.hodhod.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hodhod.hodhod.data.models.MessageModel
import ir.hodhod.hodhod.repositories.local.persistence.MessageRepository
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
// TODO: we have to use usecases here later!
class ChatViewModel @Inject constructor(private val messageRepository: MessageRepository) :
    ViewModel() {

    val getLocationsRespond = MutableLiveData<Map<String, LatLng?>>()
    val getLocationsError = MutableLiveData<Unit>()

    val getMessagesRespond = MutableLiveData<List<MessageModel>>()
    val getMessagesError = MutableLiveData<Unit>()

    val getUsersRespond = MutableLiveData<List<String>>()
    val getUsersError = MutableLiveData<Unit>()

    fun insertMessage(messageModel: MessageModel) {
        viewModelScope.launch {
            messageRepository.insertMessage(messageModel)
        }
    }

    fun getAllMessages(roomKey: String) {
        viewModelScope.launch {
            messageRepository.getAllMessagesByRoomKey(roomKey)?.also {
                getMessagesRespond.value = it
            } ?: apply { getMessagesError.value = Unit }
        }
    }

    fun getLocations(roomKey: String) {
        viewModelScope.launch {
            messageRepository.getAllLocationsByRoomKey(roomKey)?.also {
                getLocationsRespond.value = it
            } ?: apply { getLocationsError.value = Unit }
        }
    }

    fun getUsers(roomKey: String) {
        viewModelScope.launch {
            messageRepository.getAllUsernamesByRoomKey(roomKey)?.also {
                getUsersRespond.value = it
            } ?: apply { getUsersError.value = Unit }
        }
    }
}