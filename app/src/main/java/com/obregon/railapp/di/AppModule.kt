package com.obregon.railapp.di

import android.content.Context
import com.obregon.railapp.data.api.RailApi
import com.obregon.railapp.data.repository.RailRepository
import com.obregon.railapp.data.repository.RailRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesRetrofit(@Named("ApiUrl") apiUrl:String, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(apiUrl)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    @Named("ApiUrl")
    fun providesApiUrl():String{
        return "http://api.irishrail.ie/realtime/realtime.asmx/"
    }

    @Singleton
    @Provides
    fun provideHttpClient(interceptor: Interceptor, cache: Cache): OkHttpClient =
        OkHttpClient
            .Builder().cache(cache)
            .addInterceptor(interceptor)
            .build()

    @Singleton
    @Provides
    fun provideInterceptor(): Interceptor {
        return  HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideCache(@ApplicationContext context: Context): Cache = Cache(context.cacheDir, 5 * 1024 * 1024)

    @Singleton
    @Provides
    fun provideRailApi(retrofit: Retrofit): RailApi {
       return retrofit.create(RailApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRailRepository(railRepositoryImpl: RailRepositoryImpl): RailRepository {
        return railRepositoryImpl
    }
}