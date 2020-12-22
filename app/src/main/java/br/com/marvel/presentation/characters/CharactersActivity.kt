package br.com.marvel.presentation.characters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.marvel.R
import br.com.marvel.presentation.characters.adapter.CharactersPagerAdapter
import br.com.marvel.presentation.characters.fragments.CharactersFragment
import br.com.marvel.presentation.characters.fragments.FavoriteCharactersFragment
import kotlinx.android.synthetic.main.activity_characters.*

class CharactersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_characters)
        setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.app_name)
        setupTabsAndViewPager()
    }

    private fun setupTabsAndViewPager() {
        val charactersFragment = CharactersFragment.newInstance()
        val favoriteCharactersFragment = FavoriteCharactersFragment.newInstance()

        CharactersPagerAdapter(supportFragmentManager).apply {
            addFragment(charactersFragment, getString(R.string.marvel_characters))
            addFragment(favoriteCharactersFragment, getString(R.string.marvel_favorites))
        }.also { characterPageAdapter ->
            charactersViewPager.adapter = characterPageAdapter
        }
        charactersTabs.setupWithViewPager(charactersViewPager)
    }
}