package br.com.marvel.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.marvel.model.Character
import br.com.marvel.util.Converters

@Database(entities = [Character::class], version = 2)
@TypeConverters(
    Converters.SeriesConverter::class,
    Converters.ComicsConverter::class,
    Converters.StoriesConverter::class,
    Converters.EventsConverter::class,
    Converters.ThumbnailConverter::class
)
abstract class MarvelDatabase : RoomDatabase() {
    abstract fun charactersDao(): CharactersDAO
}