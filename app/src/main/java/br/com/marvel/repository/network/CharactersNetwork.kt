package br.com.marvel.repository.network

import br.com.marvel.model.CharactersResponse
import br.com.marvel.model.ComicsResponse
import br.com.marvel.model.SerieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharactersNetwork {
    @GET("/v1/public/characters")
    suspend fun getCharacters(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): CharactersResponse

    @GET("/v1/public/characters/{characterId}/comics")
    suspend fun getComicsByCharacterId(
        @Path("characterId") characterId: String
    ): ComicsResponse

    @GET("/v1/public/characters/{characterId}/series")
    suspend fun getSeriesByCharacterId(
        @Path("characterId") characterId: String
    ): SerieResponse
}