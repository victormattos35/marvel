package br.com.marvel.presentation.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.marvel.R
import br.com.marvel.model.Character
import br.com.marvel.util.GlideApp
import br.com.marvel.util.MARVEL_CHARACTER
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.item_character.view.*
import org.koin.android.viewmodel.ext.android.viewModel


class DetailActivity : AppCompatActivity() {

    private val viewModel: DetailViewModel by viewModel()
    private var detailComicsAdapter = DetailAdapter()
    private val detailSeriesAdapter = DetailAdapter()
    var character: Character? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        character = intent?.extras?.getParcelable(MARVEL_CHARACTER)
        supportActionBar?.title = character?.name

        loadCharacter(character)
        viewModel.loadCharacter(character)
        loadRecyclersViews()

        viewModel.getComicsSeriesByCharacterId(character?.id)

        viewModel.comicsSeriesList.observe(this, Observer {
            detailComicsAdapter.insertData(it.first)
            detailSeriesAdapter.insertData(it.second)
        })
    }

    private fun loadCharacter(character: Character?) {
        if (character?.description.isNullOrBlank())
            textViewDescription.visibility = View.GONE
        else
            textViewDescription.text = character?.description

        GlideApp.with(this)
            .load("${character?.thumbnail?.path}.${character?.thumbnail?.extension}")
            .into(imageViewHero.imageViewHero)

    }

    private fun loadRecyclersViews() {
        val layoutManagerSerie = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL, false
        )
        val layoutManagerComic = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL, false
        )

        recyclerViewSeries.apply {
            adapter = detailComicsAdapter
            setHasFixedSize(true)
            this.layoutManager = layoutManagerSerie
        }

        recyclerViewComics.apply {
            adapter = detailSeriesAdapter
            setHasFixedSize(true)
            this.layoutManager = layoutManagerComic
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_favorite, menu)
        viewModel.character.observe(this, Observer {
            if (it.isFavorite) {
                menu.getItem(0).icon = getDrawable(R.drawable.ic_heath_filled_24dp)
            } else {
                menu.getItem(0).icon = getDrawable(R.drawable.ic_heart_outline_24dp)
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_favorite -> {
                viewModel.favoriteCharacter()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
