package com.example.vehicle_policy.di

import android.content.Context
import androidx.room.Room
import com.example.vehicle_policy.data.VehiclePolicyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object AndroidTestAppModule {

    @Provides
    @Named("androidTest_db")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, VehiclePolicyDatabase::class.java)
            .allowMainThreadQueries()
            .build()
}