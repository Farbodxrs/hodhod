package ir.hodhod.hodhod.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.hodhod.hodhod.Constants
import ir.hodhod.hodhod.repositories.server.persistence.MQTTBrokerRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.eclipse.paho.android.service.MqttAndroidClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMQTTClient(@ApplicationContext context: Context) =
        MqttAndroidClient(context, Constants.SERVER_URL, "sepehr")

    @ExperimentalCoroutinesApi
    @Singleton
    @Provides
    fun provideMQTTBrokerRepository(mqttAsyncClient: MqttAndroidClient) =
        MQTTBrokerRepository(mqttAsyncClient)
}