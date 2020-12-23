package br.com.marvel.module

import br.com.marvel.BuildConfig
import br.com.marvel.repository.MarvelRepository
import br.com.marvel.repository.MarvelRepositoryImp
import br.com.marvel.repository.database.CharactersDAO
import br.com.marvel.repository.network.CharactersNetwork
import br.com.marvel.util.HashGenerate
import br.com.marvel.util.MARVEL_APIKEY
import br.com.marvel.util.MARVEL_HASH
import br.com.marvel.util.MARVEL_TIMESTAMP
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val URL = "https://gateway.marvel.com"

val NetworkModule = module {

    single { createService(get()) }
    single { createOkHttpClient() }

    single { createRetrofit(get(), URL) }


}

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
    return OkHttpClient.Builder()
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .addInterceptor { chain -> createParametersDefault(chain) }
        .addInterceptor(httpLoggingInterceptor).build()

}

fun createParametersDefault(chain: Interceptor.Chain): Response {
    val timeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
    var request = chain.request()
    val builder = request.url().newBuilder()

    builder.addQueryParameter(MARVEL_APIKEY, BuildConfig.API_PUBLIC)
        .addQueryParameter(
            MARVEL_HASH,
            HashGenerate.generate(timeStamp, BuildConfig.API_PRIVATE, BuildConfig.API_PUBLIC)
        )
        .addQueryParameter(MARVEL_TIMESTAMP, timeStamp.toString())

    request = request.newBuilder().url(builder.build()).build()
    return chain.proceed(request)
}

fun createRetrofit(okHttpClient: OkHttpClient, url: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun createService(retrofit: Retrofit): CharactersNetwork {
    return retrofit.create(CharactersNetwork::class.java)
}

fun createMarvelRepository(
    characterDao: CharactersDAO,
    charactersNetwork: CharactersNetwork
): MarvelRepository {
    return MarvelRepositoryImp(characterDao, charactersNetwork)
}