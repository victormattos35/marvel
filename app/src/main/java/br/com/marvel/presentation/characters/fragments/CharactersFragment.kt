package br.com.marvel.presentation.characters.fragments

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.marvel.model.Character
import br.com.marvel.R
import br.com.marvel.presentation.characters.adapter.CharactersAdapter
import br.com.marvel.presentation.detail.DetailActivity
import br.com.marvel.util.EndlessRecyclerViewScrollListener
import br.com.marvel.util.MARVEL_CHARACTER
import kotlinx.android.synthetic.main.fragment_characters.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel

class CharactersFragment : Fragment() {

    companion object {
        fun newInstance() =
            CharactersFragment()
    }

    private val viewModel: CharactersViewModel by sharedViewModel()
    private val charactersAdapter: CharactersAdapter by inject()
    lateinit var layoutManagerGrid: GridLayoutManager
    private var searchView: SearchView? = null
    private var queryTextListener: SearchView.OnQueryTextListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_characters, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        setupCharacterRecyclerView()
        getCharacters()
        hideKeyboard()
        getListCharacters()
        isLoading()
        setErrorConnection()
        setErrorMessage()
        setMessageEmptyList()
        saveCharacter()
        deleteCharacter()
        goDetailActivity()
        textViewTryAgain()
        swipeRefreshList()
    }

    private fun getCharacters() {
        viewModel.getCharacters()
    }

    private fun goDetailActivity() {
        charactersAdapter.setOnClickCharacterListener {
            startActivity(Intent(activity, DetailActivity::class.java).apply {
                putExtra(MARVEL_CHARACTER, it)
            })
        }
    }

    private fun deleteCharacter() {
        charactersAdapter.setOnUnFavoriteClickListener {
            viewModel.deleteCharacter(it)
        }
    }

    private fun saveCharacter() {
        charactersAdapter.setOnFavoriteClickListener {
            viewModel.saveCharacter(it)
        }
    }

    private fun swipeRefreshList() {
        swiperefresh.setOnRefreshListener {
            viewModel.getCharacters()
            swiperefresh.isRefreshing = false
        }
    }

    private fun textViewTryAgain() {
        textViewTryAgain.setOnClickListener {
            viewModel.getCharacters()
        }
    }

    private fun setMessageEmptyList() {
        viewModel.emptyList.observe(this, Observer { listIsEmpty ->
            when (listIsEmpty) {
                true -> {
                    charactersRecyclerView.visibility = View.INVISIBLE
                    layoutInternetError.visibility = View.VISIBLE
                    textViewErrorMessage.text = context?.resources?.getString(R.string.marvel_no_items)
                }
                false -> {
                    charactersRecyclerView.visibility = View.VISIBLE
                    layoutInternetError.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun setErrorMessage() {
        viewModel.errorMessage.observe(this, Observer {
            textViewErrorMessage.text = it
        })
    }

    private fun setErrorConnection() {
        viewModel.errorConnection.observe(this, Observer { hasError ->
            when (hasError) {
                true -> {
                    charactersRecyclerView.visibility = View.INVISIBLE
                    layoutInternetError.visibility = View.VISIBLE
                }
                false -> {
                    charactersRecyclerView.visibility = View.VISIBLE
                    layoutInternetError.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun isLoading() {
        viewModel.isLoading.observe(this, Observer { isLoading ->
            when (isLoading) {
                true -> {
                    progress.visibility = View.VISIBLE
                }
                false -> {
                    progress.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun getListCharacters() {
        viewModel.listCharacters.observe(this, Observer { list ->
            if (list != null)
                updateCharacterList(list)

        })
    }

    private fun hideKeyboard() {
        viewModel.hideKeyboard.observe(this, Observer { isVisible ->
            if (isVisible) {
                val imm =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view!!.windowToken, 0)
            }

        })
    }



    private fun setupCharacterRecyclerView() {
        layoutManagerGrid =
            if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
                GridLayoutManager(activity, 1)
            } else {
                GridLayoutManager(activity, 2)
            }

        charactersRecyclerView.apply {
            adapter = charactersAdapter
            setHasFixedSize(true)
            this.layoutManager = layoutManagerGrid
        }

        charactersRecyclerView.addOnScrollListener(
            object : EndlessRecyclerViewScrollListener(layoutManagerGrid) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    viewModel.getCharacters()
                }
            }
        )

    }

    private fun updateCharacterList(charactersList: List<Character>) {
        charactersAdapter.addCharacters(charactersList)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchManager =
            activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
            searchView?.apply {
                isIconified = false
                onActionViewExpanded()
                queryHint = context?.getString(R.string.marvel_search_hero)
                requestFocusFromTouch()
                isFocusable = true
                setOnSearchClickListener {
                    searchView.apply {
                        setQuery("", false)
                        onActionViewCollapsed()
                        isIconified = false
                    }
                }
            }
        }
        if (searchView != null) {
            searchView?.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            queryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(query: String): Boolean {
                    return if (query.isNotEmpty()) {
                        viewModel.filter?.filter(query)
                        true
                    } else {
                        viewModel.getCharacters()
                        false
                    }
                }
                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.filter?.filter(query)
                    return true
                }
            }
            searchView?.setOnQueryTextListener(queryTextListener)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onResume() {
        viewModel.verifyLocalFavorites()
        super.onResume()
    }
}