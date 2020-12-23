package br.com.marvel.repository

import br.com.marvel.model.Character
import br.com.marvel.model.CharactersResponse
import br.com.marvel.model.ComicsResponse
import br.com.marvel.model.SerieResponse
import br.com.marvel.repository.database.CharactersDAO
import br.com.marvel.repository.network.CharactersNetwork
import kotlinx.coroutines.flow.Flow

class MarvelRepositoryImp(
    private val charactersDAO: CharactersDAO,
    private val charactersNetwork: CharactersNetwork
) : MarvelRepository {

    override suspend fun getCharacters(limit: Int, offset: Int): CharactersResponse {
        return charactersNetwork.getCharacters(limit, offset)
    }

    override suspend fun getComicsByCharacterId(id: String): ComicsResponse {
        return charactersNetwork.getComicsByCharacterId(id)
    }

    override suspend fun getSeriesByCharacterId(id: String): SerieResponse {
        return charactersNetwork.getSeriesByCharacterId(id)
    }

    override suspend fun saveCharacterLocal(character: Character): Long {
        return charactersDAO.insert(character)
    }

    override suspend fun getCharactersLocal(): Flow<MutableList<Character>> {
        return charactersDAO.getAll()
    }

    override suspend fun deleteCharacterLocal(character: Character) {
        return charactersDAO.delete(character)
    }
}