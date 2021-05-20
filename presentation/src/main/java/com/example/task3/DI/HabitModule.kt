package com.example.task3.DI

import android.content.Context
import androidx.room.Room
import com.example.data.*
import com.example.domain.repository.HabitRepository
import com.example.domain.useCases.*
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class HabitsModule {

    @Provides
    fun provideAddHabitUseCase(habitRepository: HabitRepository): AddHabitUseCase {
        return AddHabitUseCase(habitRepository, Dispatchers.IO)
    }

    @Provides
    fun provideUpdateHabitUseCase(habitRepository: HabitRepository): UpdateHabitUseCase {
        return UpdateHabitUseCase(habitRepository, Dispatchers.IO)
    }

    @Provides
    fun provideDeleteHabitUseCase(habitRepository: HabitRepository): DeleteHabitUseCase {
        return DeleteHabitUseCase(habitRepository, Dispatchers.IO)
    }


    @Provides
    fun provideGetHabitsUseCase(habitRepository: HabitRepository): GetHabitsUseCase {
        return GetHabitsUseCase(habitRepository, Dispatchers.IO)
    }

    @Provides
    fun providePostHabitUseCase(habitRepository: HabitRepository): PostHabitUseCase {
        return PostHabitUseCase(habitRepository, Dispatchers.IO)
    }

    @Provides
    fun provideHabitRepository(
        dataBase: HabitDb,
        searchRepository: ApiRepository
    ): HabitRepository {
        return HabitRepositoryImpl(dataBase, searchRepository)
    }

    @Provides
    fun provideSearchRepository(api: HabitService): ApiRepository {
        return ApiRepository(api)
    }

    @Provides
    fun provideHabitService(retrofit: Retrofit): HabitService {
        return retrofit.create(HabitService::class.java)
    }

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        val okHttpClient = OkHttpClient().newBuilder().addInterceptor { chain ->

            val request: Request = chain.request()
            var response = chain.proceed(request)
            var tryCount = 0
            while (!(response.isSuccessful) && tryCount < 5) {
                Thread.sleep(100)
                tryCount++
                response = chain.proceed(request)
            }

            response
        }
            .build()

        val gSon = GsonBuilder().registerTypeAdapter(
            HabitDbDao::class.java,
            HabitTypeAdapter()
        ).create()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gSon))
            .baseUrl("https://droid-test-server.doubletapp.ru/")
            .build()

        return retrofit
    }

    @Singleton
    @Provides
    fun provideDataBase(context: Context): HabitDb {
        return Room.databaseBuilder(
            context,
            HabitDb::class.java, "database"
        )
            .build()
    }


}