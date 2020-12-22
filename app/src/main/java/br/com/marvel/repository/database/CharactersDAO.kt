package br.com.marvel.repository.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.com.marvel.model.Character
import kotlinx.coroutines.flow.Flow

@Dao
interface CharactersDAO {
    @Query("SELECT * FROM charactersTable")
    fun getAll(): Flow<MutableList<Character>>

    @Query("SELECT * FROM charactersTable WHERE id=:id ")
    suspend fun getCharacterById(id: String): Character

    @Insert
    suspend fun insert(character: Character): Long

    @Delete
    suspend fun delete(character: Character)
}