package com.example.jonathansnidervirginmoney.dependencyinjection

import com.example.jonathansnidervirginmoney.data.api.APIClient
import com.example.jonathansnidervirginmoney.data.api.APIDetails
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module //tells Hilt that there are definitions to be injected here
@InstallIn(SingletonComponent::class) //tells Hilt how long these dependencies are meant to last
// (in this case, SingletonComponent::class means as long as the application itself is active
class AppModule {

    @Provides
    fun provideGson(): Gson = Gson()

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
    }

    @Provides
    fun provideRetrofit(
        converterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(APIDetails.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    fun provideAPIClient(
        retrofit: Retrofit
    ): APIClient {
        return retrofit.create(APIClient::class.java)
    }
}