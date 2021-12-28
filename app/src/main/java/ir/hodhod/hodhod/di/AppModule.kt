package ir.hodhod.hodhod.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.hodhod.hodhod.repositories.server.persistence.MQTTBrokerRepository
import ir.hodhod.hodhod.utils.Constants
import ir.hodhod.hodhod.utils.UsernameSharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.eclipse.paho.android.service.MqttAndroidClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMQTTClient(@ApplicationContext context: Context): MqttAndroidClient {
        val userPreference = UsernameSharedPreferences.initialWith(context.applicationContext)
        return MqttAndroidClient(context, Constants.SERVER_URL, userPreference.getUsername())
    }

    @ExperimentalCoroutinesApi
    @Singleton
    @Provides
    fun provideMQTTBrokerRepository(mqttAsyncClient: MqttAndroidClient) =
        MQTTBrokerRepository(mqttAsyncClient)
}