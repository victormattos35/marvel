package br.com.marvel

import br.com.marvel.model.Data
import br.com.marvel.model.Character
import br.com.marvel.repository.MarvelRepository
import br.com.marvel.repository.MarvelRepositoryImp
import org.mockito.Mockito

open class BaseUnitTest {

    var repoMock: MarvelRepository = Mockito.mock(MarvelRepositoryImp::class.java)

    fun createCharacter(): Data {
        val character1 = Character(id = "1", name = "Hulk", isFavorite = true)
        val character2 = Character(id = "2", name = "IronMan", isFavorite = false)
        val listCharacter = arrayListOf(character1, character2)
        return Data(listCharacter)
    }

    fun getCharacter(): Character {
        return Character(id = "1", name = "Hulk", isFavorite = true)
    }
}