package br.com.marvel.presentation.characters.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import br.com.marvel.R
import br.com.marvel.presentation.characters.adapter.CharactersAdapter
import br.com.marvel.presentation.detail.DetailActivity
import br.com.marvel.util.MARVEL_CHARACTER
import kotlinx.android.synthetic.main.fragment_favorite_characters.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel

class FavoriteCharactersFragment : Fragment() {

    companion object {
        fun newInstance() =
            FavoriteCharactersFragment()
    }

    private val viewModel: CharactersViewModel by sharedViewModel()
    private val charactersAdapter: CharactersAdapter by inject()
    private lateinit var layoutManagerGrid: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite_characters, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupFavoriteRecyclerView()
        getFavorites()
        getListCharactersFavorites()
        deleteCharacter()
        goDetailActivity()
        swipeRefreshList()
    }

    private fun getFavorites() {
        viewModel.getFavorites()
    }

    private fun getListCharactersFavorites() {
        viewModel.listCharactersFavorites.observe(this, Observer { list ->
            when (list.isNullOrEmpty()) {
                true -> {
                    textViewNoFavorite.visibility = View.VISIBLE
                }
                false -> {
                    textViewNoFavorite.visibility = View.INVISIBLE
                }
            }
            charactersAdapter.addCharacters(list)
        })
    }

    private fun deleteCharacter() {
        charactersAdapter.setOnUnFavoriteClickListener {
            viewModel.deleteCharacter(it)
        }
    }

    private fun goDetailActivity() {
        charactersAdapter.setOnClickCharacterListener { character ->
            startActivity(Intent(activity, DetailActivity::class.java).apply {
                putExtra(MARVEL_CHARACTER, character)
            })
        }
    }

    private fun setupFavoriteRecyclerView() {
        layoutManagerGrid =
            if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
                GridLayoutManager(activity, 1)
            } else {
                GridLayoutManager(activity, 2)
            }
        favoriteRecyclerView.apply {
            adapter = charactersAdapter
            setHasFixedSize(true)
            layoutManager = layoutManagerGrid
        }
    }

    private fun swipeRefreshList() {
        swiperefreshFavorite.setOnRefreshListener {
            viewModel.getFavorites()
            swiperefreshFavorite.isRefreshing = false
        }
    }

}