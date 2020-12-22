package br.com.marvel.presentation.characters.fragments

import br.com.marvel.model.Character
import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.MutableLiveData
import br.com.marvel.presentation.base.BaseViewModel
import br.com.marvel.repository.MarvelRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class CharactersViewModel (private val marvelRepository: MarvelRepository) : BaseViewModel(),
    Filterable {

    private var limit: Int = 20
    private var offset: Int = 1
    private var listCharacterHelper: MutableList<Character> = mutableListOf()
    private var contactListFiltered: MutableList<Character> = mutableListOf()

    val listAux: MutableLiveData<Character> = MutableLiveData()
    val listCharacters: MutableLiveData<MutableList<Character>> = MutableLiveData()
    val listCharactersFavorites: MutableLiveData<MutableList<Character>> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val errorConnection: MutableLiveData<Boolean> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val emptyList: MutableLiveData<Boolean> = MutableLiveData()
    val hideKeyboard: MutableLiveData<Boolean> = MutableLiveData()

    fun getCharacters() {
        isLoading.value = true
        scope.launch {
            try {
                val response = marvelRepository.getCharacters(limit, offset)

                //check if the new data from server is into local database and mark as favorite if it is.
                marvelRepository.getCharactersLocal().take(1).collect { localCharacters ->
                    response.data.results.forEach { character ->
                        val a = localCharacters.find { it.id == character.id }
                        if (a != null) character.isFavorite = true
                    }
                }
                if (response.data.results.isEmpty()) {
                    emptyList.value = true
                } else {
                    listCharacterHelper.addAll(response.data.results)
                    listCharacters.value = listCharacterHelper
                    offset += limit
                    errorConnection.value = false
                }
            } catch (e: Exception) {
                //we can catch any error here using getError
                errorMessage.value = getError(e).getErrorMessage()
                errorConnection.value = true
            }
            isLoading.value = false
        }

    }

    fun saveCharacter(character: Character) {
        scope.launch {
            try {
                marvelRepository.saveCharacterLocal(character)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getFavorites() {
        try {
            scope.launch {
                marvelRepository.getCharactersLocal().collect {
                    listCharactersFavorites.value = it
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteCharacter(character: Character) {
        try {
            scope.launch {
                marvelRepository.deleteCharacterLocal(character)
                listCharacters.value?.find { it.id == character.id }.apply {
                    this?.isFavorite = false
                }

                listCharacters.notifyObserver()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun verifyLocalFavorites() {
        scope.launch {
            marvelRepository.getCharactersLocal().take(1).collect { localCharacters ->
                listCharacters.value?.forEach { it.isFavorite = false }
                localCharacters.forEach { character ->
                    listCharacters.value?.find { it.id == character.id }.apply {
                        this?.isFavorite = true
                    }
                }
                listCharacters.notifyObserver()
            }
        }

    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charString = charSequence.toString()
                contactListFiltered = if (charString.isEmpty()) {
                    listCharacterHelper
                } else {
                    val filteredList: MutableList<Character> = ArrayList()
                    for (row in listCharacterHelper) {
                        if (row.name.toLowerCase(
                                Locale(
                                    "pt", "BR"
                                )
                            ).contains(charString.toLowerCase(Locale("pt", "BR")))
                        ) {
                            filteredList.add(row)
                        }
                    }
                    if (filteredList.size <= 0) {
                        hideKeyboard.value = true
                        emptyList.value = true
                    }
                    filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = contactListFiltered
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults
            ) {
                if (filterResults.values != null) {
                    listCharacters.value?.clear()
                    listCharacterHelper.addAll(filterResults.values as MutableList<Character>)
                    listCharacters.value = listCharacterHelper
                    listCharacters.notifyObserver()
                } else {
                    emptyList.value = true
                    hideKeyboard.value = true
                }
            }
        }
    }
}

