package com.bhushan.chingariassignment.koin

import com.bhushan.chingariassignment.BuildConfig
import com.bhushan.chingariassignment.network.WeatherDataRepository
import com.bhushan.chingariassignment.network.WeatherService
import com.bhushan.chingariassignment.utils.networkUtility.AppExecutors
import com.bhushan.chingariassignment.utils.networkUtility.LiveDataCallAdapterFactory
import com.bhushan.chingariassignment.viewModel.WeatherDataViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Used koin to provide dependency for
 * ---------------------------------------------------------------------------------------
 * WeatherDataViewModel | WeatherDataRepository | AppExecutors | WeatherService | Retrofit
 * ----------------------------------------------------------------------------------------
 */

val viewModelModule = module {
    viewModel {
        WeatherDataViewModel(get())
    }
}

val repositoryModule = module {
    single {
        WeatherDataRepository(get(), get())
    }
}

val appExecutorsModule = module {
    single {
        AppExecutors()
    }
}

val apiModule = module {
    fun provideUseApi(retrofit: Retrofit): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }

    single { provideUseApi(get()) }
}

val retrofitModule = module {

    fun provideHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.connectTimeout(30, TimeUnit.SECONDS)
        httpClient.readTimeout(30, TimeUnit.SECONDS)
        httpClient.writeTimeout(30, TimeUnit.SECONDS)
        return if (BuildConfig.BUILD_TYPE == "debug") {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
            httpClient.build()
        } else {
            httpClient.build()
        }
    }

    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    single { provideHttpClient() }
    single { provideRetrofit(get()) }
}