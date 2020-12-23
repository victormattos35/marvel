package br.com.marvel.presentation.detail

import androidx.lifecycle.MutableLiveData
import br.com.marvel.model.ComicSerie
import br.com.marvel.presentation.base.BaseViewModel
import br.com.marvel.repository.MarvelRepository
import br.com.marvel.model.Character
import kotlinx.coroutines.launch

class DetailViewModel(private val marvelRepository: MarvelRepository) : BaseViewModel() {
    val comicsSeriesList: MutableLiveData<Pair<MutableList<ComicSerie>, MutableList<ComicSerie>>> =
        MutableLiveData()

    val character: MutableLiveData<Character> = MutableLiveData()

    fun loadCharacter(character: Character?) {
        this.character.value = character
    }

    fun favoriteCharacter() {
        if (character.value?.isFavorite!!) {
            character.value = character.value?.copy(isFavorite = false)
            deleteCharacter(character.value!!)
        } else {
            character.value = character.value?.copy(isFavorite = true)
            saveCharacter(character.value!!)
        }
    }

    fun getComicsSeriesByCharacterId(id: String?) {

        scope.launch {
            try {
                val comicsListHelper: MutableList<ComicSerie> = mutableListOf()
                val seriesListHelper: MutableList<ComicSerie> = mutableListOf()

                val comics = id?.let {
                    marvelRepository.getComicsByCharacterId(it)
                }
                comics?.data?.results?.forEach {
                    if (!it.images.isNullOrEmpty()) {
                        comicsListHelper.add(
                            ComicSerie(
                                it.title,
                                it.images[0].path + "." + it.images[0].extension
                            )
                        )
                    }
                }

                val series = id?.let {
                    marvelRepository.getSeriesByCharacterId(it)
                }
                series?.data?.results?.forEach {
                    seriesListHelper.add(
                        ComicSerie(
                            it.title,
                            it.thumbnail.path + "." + it.thumbnail.extension
                        )
                    )
                }

                comicsSeriesList.value = Pair(comicsListHelper, seriesListHelper)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun deleteCharacter(character: Character) {
        try {
            scope.launch {
                marvelRepository.deleteCharacterLocal(character)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveCharacter(character: Character) {
        scope.launch {
            try {
                marvelRepository.saveCharacterLocal(character)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}