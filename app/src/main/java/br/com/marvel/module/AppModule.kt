package br.com.marvel.module


import br.com.marvel.presentation.characters.adapter.CharactersAdapter
import br.com.marvel.presentation.characters.fragments.CharactersViewModel
import br.com.marvel.presentation.detail.DetailViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {

    viewModel { CharactersViewModel(get()) }
    viewModel { DetailViewModel(get()) }

    factory { CharactersAdapter() }

    single { createMarvelRepository(get(), get()) }
}