package com.najmizahrian.pokedex.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CatchingPokemon
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.Groups
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.najmizahrian.pokedex.ui.components.EmptyView
import com.najmizahrian.pokedex.ui.components.ErrorView
import com.najmizahrian.pokedex.ui.components.LoadingView
import com.najmizahrian.pokedex.ui.components.PokemonCard
import com.najmizahrian.pokedex.ui.components.PokemonSearchBar
import com.najmizahrian.pokedex.ui.theme.DeepCobalt

/**
 * Layar utama Katalog dan Eksplorasi Pokémon
 * Mengusung tema Deep Cobalt Blue dengan akses cepat ke fitur Tim Sinergi
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onPokemonClick: (Int) -> Unit,
    onTeamClick: () -> Unit = {},
    viewModel: HomeViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "PokéTeam Architect",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black
                        )
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.CatchingPokemon,
                        contentDescription = "Pokeball Logo",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 4.dp)
                            .size(28.dp)
                    )
                },
                actions = {
                    // Tombol Navigasi ke Team Builder (Groups Icon)
                    IconButton(
                        onClick = onTeamClick,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(42.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.22f),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Groups,
                                    contentDescription = "PokéTeam Builder",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepCobalt
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header Banner Cobalt melengkung yang menaungi Search Bar
            Surface(
                color = DeepCobalt,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.padding(bottom = 8.dp, top = 0.dp)) {
                    PokemonSearchBar(
                        query = uiState.searchQuery,
                        onQueryChange = { viewModel.onSearchQueryChange(it) },
                        onClearQuery = { viewModel.clearSearchQuery() }
                    )
                }
            }

            // Area Konten Daftar Pokémon (Multi-State Handling)
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    // Loading State
                    uiState.isLoading && uiState.pokemonList.isEmpty() -> {
                        LoadingView(message = "Sedang mengambil data Pokémon...")
                    }

                    // Error State
                    uiState.errorMessage != null && uiState.pokemonList.isEmpty() -> {
                        ErrorView(
                            message = uiState.errorMessage ?: "Terjadi kesalahan.",
                            onRetry = { viewModel.loadPokemonList() }
                        )
                    }

                    // Empty Search State
                    uiState.filteredList.isEmpty() && uiState.searchQuery.isNotEmpty() -> {
                        EmptyView(query = uiState.searchQuery)
                    }

                    // Success State: Menampilkan LazyVerticalGrid dengan kartu modern
                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = uiState.filteredList,
                                key = { it.id }
                            ) { pokemon ->
                                PokemonCard(
                                    pokemon = pokemon,
                                    onClick = { onPokemonClick(pokemon.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
