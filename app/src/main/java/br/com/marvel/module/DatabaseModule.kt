package br.com.marvel.module

import androidx.room.Room
import br.com.marvel.repository.database.MarvelDatabase
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            MarvelDatabase::class.java,
            "marvel-database"
        ).fallbackToDestructiveMigration()
            .build()
    }
    factory { get<MarvelDatabase>().charactersDao() }
}