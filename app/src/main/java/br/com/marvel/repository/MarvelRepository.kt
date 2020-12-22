package br.com.marvel.repository

import br.com.marvel.model.Character
import br.com.marvel.model.CharactersResponse
import br.com.marvel.model.ComicsResponse
import br.com.marvel.model.SerieResponse
import kotlinx.coroutines.flow.Flow

interface MarvelRepository {
    //remote
    suspend fun getCharacters(limit: Int, offset: Int): CharactersResponse
    suspend fun getComicsByCharacterId(id: String): ComicsResponse
    suspend fun getSeriesByCharacterId(id: String): SerieResponse

    //local
    suspend fun saveCharacterLocal(character: Character): Long
    suspend fun getCharactersLocal(): Flow<MutableList<Character>>
    suspend fun deleteCharacterLocal(character: Character)
}