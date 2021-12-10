package ir.hodhod.hodhod.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hodhod.hodhod.repositories.server.persistence.MQTTBrokerRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class SharedViewModel @Inject constructor(private val mqttBrokerRepository: MQTTBrokerRepository) :
    ViewModel() {

    fun connectToServer() {
        viewModelScope.launch {
            mqttBrokerRepository.connect()
        }
    }

    fun subscribeToTopic(topic: String) {
        viewModelScope.launch {
            mqttBrokerRepository.subscribe(topic)
        }
    }

    fun publishMessage(topic: String, message: String) {
        viewModelScope.launch {
            mqttBrokerRepository.publish(topic, message)
        }
    }

    fun unsubscribeFromTopic(topic: String) {
        viewModelScope.launch {
            mqttBrokerRepository.unsubscribe(topic)
        }
    }

    fun disconnectFromServer() {
        viewModelScope.launch {
            mqttBrokerRepository.disconnect()
        }
    }
}