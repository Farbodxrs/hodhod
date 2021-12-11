package ir.hodhod.hodhod.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hodhod.hodhod.repositories.server.persistence.BaseResult
import ir.hodhod.hodhod.repositories.server.persistence.MQTTBrokerRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
// TODO: we have to use IMQTTBrokerRepository here later!
class SharedViewModel @Inject constructor(private val mqttBrokerRepository: MQTTBrokerRepository) :
    ViewModel() {

    val connectRespond = MutableLiveData<Unit>()
    val connectError = MutableLiveData<String>()

    val subscribeRespond = MutableLiveData<Unit>()
    val subscribeError = MutableLiveData<String>()

    val publishRespond = MutableLiveData<Unit>()
    val publishError = MutableLiveData<String>()

    val unsubscribeRespond = MutableLiveData<Unit>()
    val unsubscribeError = MutableLiveData<String>()

    val disconnectRespond = MutableLiveData<Unit>()
    val disconnectError = MutableLiveData<String>()

    val messageDeliver = MutableLiveData<Unit>()
    val connectionLost = MutableLiveData<String>()
    val messageArrived = MutableLiveData<String>()

    fun connectToServer() {
        viewModelScope.launch {
            mqttBrokerRepository.connect().also {
                when (it) {
                    is BaseResult.Success -> connectRespond.value = Unit
                    is BaseResult.Error -> connectError.value = it.message
                }
            }
        }
    }

    fun subscribeToTopic(topic: String) {
        viewModelScope.launch {
            mqttBrokerRepository.subscribe(topic).also {
                when (it) {
                    is BaseResult.Success -> subscribeRespond.value = Unit
                    is BaseResult.Error -> subscribeError.value = it.message
                }
            }
        }
    }

    fun publishMessage(topic: String, message: String) {
        viewModelScope.launch {
            mqttBrokerRepository.publish(topic, message).also {
                when (it) {
                    is BaseResult.Success -> publishRespond.value = Unit
                    is BaseResult.Error -> publishError.value = it.message
                }
            }
        }
    }

    fun unsubscribeFromTopic(topic: String) {
        viewModelScope.launch {
            mqttBrokerRepository.unsubscribe(topic).also {
                when (it) {
                    is BaseResult.Success -> unsubscribeRespond.value = Unit
                    is BaseResult.Error -> unsubscribeError.value = it.message
                }
            }
        }
    }

    fun disconnectFromServer() {
        viewModelScope.launch {
            mqttBrokerRepository.disconnect().also {
                when (it) {
                    is BaseResult.Success -> disconnectRespond.value = Unit
                    is BaseResult.Error -> disconnectError.value = it.message
                }
            }
        }
    }

    fun setMessageCallback() {
        viewModelScope.launch {
            mqttBrokerRepository.setMessageCallback(
                messageDeliver,
                messageArrived,
                connectionLost
            )
//                .collect {
//                Log.d("MQTTBrokerRepository", "hey $it")
//                when (it) {
//                    is BaseResult.Success -> {
//                        if (it.data is Unit)
//                            messageDeliver.value = Unit
//                        else messageArrived.value = it.data.toString()
//                    }
//                    is BaseResult.Error -> connectionLost.value = it.message
//                }
//            }
        }
    }
}