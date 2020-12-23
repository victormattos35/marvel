package br.com.marvel.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

data class CharactersResponse(
    val data: Data
)

data class Data(
    val results: List<Character>
)

@Parcelize
@Entity(tableName = "charactersTable")
data class Character(
    @PrimaryKey val id: String = "",
    val name: String = "",
    val description: String = "",
    var isFavorite: Boolean = false,
    val series: Series? = null,
    val stories: Stories? = null,
    val thumbnail: Thumbnail? = null,
    val comics: Comics? = null,
    val events: Events? = null
) : Parcelable

@Parcelize
data class Comics(
    val available: String = "",
    val collectionURI: String = "",
    val items: List<Item>? = null,
    val returned: String = ""
) : Parcelable

@Parcelize
data class Events(
    val available: String = "",
    val collectionURI: String = "",
    val items: List<Item>? = null,
    val returned: String = ""
) : Parcelable

@Parcelize
data class Series(
    val available: String = "",
    val collectionURI: String = "",
    val items: List<Item>? = null,
    val returned: String = ""
) : Parcelable

@Parcelize
data class Stories(
    val available: String = "",
    val collectionURI: String = "",
    val items: List<Item>? = null,
    val returned: String = ""
) : Parcelable

@Parcelize
data class Item(
    val name: String = "",
    val resourceURI: String = "",
    val type: String = ""
) : Parcelable

@Parcelize
data class Thumbnail(
    val extension: String = "",
    val path: String = ""
) : Parcelable