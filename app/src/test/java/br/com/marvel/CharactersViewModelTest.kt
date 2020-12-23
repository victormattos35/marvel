package br.com.marvel

import br.com.marvel.model.Character
import br.com.marvel.model.CharactersResponse
import br.com.marvel.repository.MarvelRepository
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*


class CharactersViewModelTest : BaseUnitTest() {

    @Mock
    private var mockcharacter: Character? = null

    @Test
    fun testCharacterList() {
        runBlocking {
            val responseMock = CharactersResponse(createCharacter())
            `when`(repoMock.getCharacters(20, 1)).thenReturn(responseMock)
            assertEquals(
                responseMock.data.results.size,
                repoMock.getCharacters(20, 1).data.results.size
            )
        }
    }

    @Test
    fun testCharacterListNotNull() {
        runBlocking {
            val responseMock = CharactersResponse(createCharacter())
            `when`(repoMock.getCharacters(20, 1)).thenReturn(responseMock)
            responseMock.data.results.forEach {
                assertNotNull(it)
            }
        }
    }

    @Test
    fun testCharacterFavorite() {
        runBlocking {
            val responseMock = CharactersResponse(createCharacter())
            `when`(repoMock.getCharacters(20, 1)).thenReturn(responseMock)
            assertEquals(
                responseMock.data.results[0].isFavorite,
                repoMock.getCharacters(20, 1).data.results[0].isFavorite
            )
        }
    }

    @Test
    fun testSaveCharacterLocal() {
        runBlocking {
            val responseMock = getCharacter()
            assertEquals(repoMock.saveCharacterLocal(responseMock), null)
        }
    }

    @Test
    fun testDeleteCharacterLocal() {
        val marvelRepository: MarvelRepository = mock(MarvelRepository::class.java)
        runBlocking {
            mockcharacter?.let {character ->
                verify(marvelRepository, times(1)).deleteCharacterLocal(character)
            }
        }
    }
}
