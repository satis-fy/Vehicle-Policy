package com.example.vehicle_policy.di

import android.content.Context
import androidx.room.Room
import com.example.vehicle_policy.data.VehiclePolicyDao
import com.example.vehicle_policy.data.VehiclePolicyDatabase
import com.example.vehicle_policy.repository.DefaultVehiclePolicyRepository
import com.example.vehicle_policy.repository.VehiclePolicyRepository
import com.example.vehicle_policy.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context,callback: VehiclePolicyDatabase.Callback) =
        Room.databaseBuilder(context,VehiclePolicyDatabase::class.java,DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()

    @Provides
    @Singleton
    fun provideDefaultVehiclePolicyRepository(
        dao: VehiclePolicyDao
    ) = DefaultVehiclePolicyRepository(dao) as VehiclePolicyRepository

    @Provides
    fun provideVehiclePolicyDao(db: VehiclePolicyDatabase) = db.vehiclePolicyDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun providerApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope