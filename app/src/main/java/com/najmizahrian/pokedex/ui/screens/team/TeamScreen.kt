package com.najmizahrian.pokedex.ui.screens.team

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.najmizahrian.pokedex.data.model.PokemonDetail
import com.najmizahrian.pokedex.data.model.PokemonItem
import com.najmizahrian.pokedex.ui.components.TypeBadge
import com.najmizahrian.pokedex.ui.theme.CobaltBlue
import com.najmizahrian.pokedex.ui.theme.DeepCobalt
import com.najmizahrian.pokedex.ui.theme.ElectricCyan
import com.najmizahrian.pokedex.ui.theme.getPokemonGradient
import com.najmizahrian.pokedex.ui.theme.getPokemonTypeColor
import com.najmizahrian.pokedex.ui.theme.getStatColor

/**
 * Layar Utama PokéTeam Architect & Synergy Roster
 * Fitur eksklusif Muhammad Najmi Zahrian (NIM: H1D024038)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamScreen(
    onBackClick: () -> Unit,
    viewModel: TeamViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.notificationMessage) {
        uiState.notificationMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.dismissNotification()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "PokéTeam Architect",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Team Builder & Synergy Roster",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 11.sp
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    // Tombol Preset Juara Kanto
                    IconButton(onClick = { viewModel.fillKantoChampionsPreset() }) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Preset Juara",
                            tint = ElectricCyan
                        )
                    }
                    // Tombol Kosongkan Tim
                    IconButton(onClick = { viewModel.clearTeam() }) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Kosongkan Tim",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepCobalt
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Info & Banner
            item {
                TeamHeroBanner(
                    synergyRank = uiState.synergyRank,
                    synergyScore = uiState.synergyScore,
                    memberCount = uiState.slots.filterNotNull().size,
                    totalBst = uiState.statsSummary.totalBaseStats
                )
            }

            // Grid 6 Slot Tim
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Susunan Formasi Tim (6 Slot)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "${uiState.slots.filterNotNull().size}/6 Terisi",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 6 Slots in 2x3 Grid
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        for (row in 0..2) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                val slotIndex1 = row * 2
                                val slotIndex2 = row * 2 + 1

                                Box(modifier = Modifier.weight(1f)) {
                                    TeamSlotCard(
                                        slotIndex = slotIndex1,
                                        pokemon = uiState.slots[slotIndex1],
                                        onSelectClick = { viewModel.openSelector(slotIndex1) },
                                        onRemoveClick = { viewModel.removePokemonFromSlot(slotIndex1) }
                                    )
                                }

                                Box(modifier = Modifier.weight(1f)) {
                                    TeamSlotCard(
                                        slotIndex = slotIndex2,
                                        pokemon = uiState.slots[slotIndex2],
                                        onSelectClick = { viewModel.openSelector(slotIndex2) },
                                        onRemoveClick = { viewModel.removePokemonFromSlot(slotIndex2) }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Analitik Statistik Rata-rata Tim
            item {
                TeamStatsSection(summary = uiState.statsSummary)
            }

            // Analisis Sinergi & Cakupan Ofensif (Super Effective)
            item {
                OffensiveCoverageSection(
                    coverage = uiState.offensiveCoverage,
                    memberCount = uiState.slots.filterNotNull().size
                )
            }

            // Analisis Kerentanan & Kelemahan Tim (Defensive Weaknesses)
            item {
                DefensiveVulnerabilitySection(
                    weaknesses = uiState.weaknesses,
                    memberCount = uiState.slots.filterNotNull().size
                )
            }

            // Tombol Aksi Cepat Bawah
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.clearTeam() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Reset Tim")
                    }
                    ElevatedButton(
                        onClick = { viewModel.fillKantoChampionsPreset() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = CobaltBlue,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Preset Tim")
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // Modal Pemilihan Pokémon untuk Slot
    if (uiState.activeSelectorSlotIndex != null) {
        val slotNum = (uiState.activeSelectorSlotIndex ?: 0) + 1
        PokemonSelectorBottomSheet(
            slotNumber = slotNum,
            query = uiState.selectorQuery,
            pokemonList = uiState.filteredAvailableList,
            onQueryChange = { viewModel.onSelectorQueryChange(it) },
            onSelect = { pokemonId ->
                viewModel.selectPokemonForSlot(uiState.activeSelectorSlotIndex ?: 0, pokemonId)
            },
            onDismiss = { viewModel.closeSelector() }
        )
    }
}

/**
 * Banner Ringkasan Sinergi Tim
 */
@Composable
private fun TeamHeroBanner(
    synergyRank: String,
    synergyScore: Int,
    memberCount: Int,
    totalBst: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(DeepCobalt, CobaltBlue)
                    )
                )
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "RATING SINERGI TIM",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ElectricCyan,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                    )
                    Text(
                        text = synergyRank,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Black
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Total BST: $totalBst | Anggota: $memberCount/6 Pokémon",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.18f),
                    modifier = Modifier.size(68.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$synergyScore",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = ElectricCyan
                                )
                            )
                            Text(
                                text = "SKOR",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Kartu per Slot Pokémon (Berisi Pokémon atau Tombol Tambah Kosong)
 */
@Composable
private fun TeamSlotCard(
    slotIndex: Int,
    pokemon: PokemonDetail?,
    onSelectClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    if (pokemon == null) {
        // Slot Kosong
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clickable { onSelectClick() },
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = CobaltBlue.copy(alpha = 0.15f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Tambah Slot",
                            tint = CobaltBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Slot #${slotIndex + 1}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "+ Pilih Pokémon",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = CobaltBlue,
                        fontSize = 11.sp
                    )
                )
            }
        }
    } else {
        // Slot Terisi Pokémon
        val gradientColors = getPokemonGradient(pokemon.primaryType)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clickable { onSelectClick() },
            shape = RoundedCornerShape(14.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(colors = gradientColors)
                    )
                    .padding(8.dp)
            ) {
                // Tombol Hapus Pojok Kanan Atas
                IconButton(
                    onClick = onRemoveClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(24.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.35f),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Hapus",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                // Konten Slot Pokémon
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Artwork Pokémon
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(pokemon.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = pokemon.displayName,
                        modifier = Modifier
                            .size(75.dp)
                            .padding(end = 4.dp),
                        contentScale = ContentScale.Fit,
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = Color.White
                                )
                            }
                        }
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                    ) {
                        Text(
                            text = pokemon.formattedId,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White.copy(alpha = 0.85f),
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                        Text(
                            text = pokemon.displayName,
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Black
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Type Badges
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            pokemon.types.take(2).forEach { type ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.White.copy(alpha = 0.25f)
                                ) {
                                    Text(
                                        text = type.uppercase(),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 8.sp
                                        ),
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Quick Stats Pill (HP / ATK)
                        val hp = pokemon.stats.find { it.name.lowercase() == "hp" }?.value ?: 0
                        val atk = pokemon.stats.find { it.name.lowercase() == "attack" }?.value ?: 0
                        Text(
                            text = "HP:$hp • ATK:$atk",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }
    }
}

/**
 * Komponen Analitik Statistik Tim
 */
@Composable
private fun TeamStatsSection(summary: TeamStatsSummary) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Rata-Rata Statistik Tim",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "Performa seimbang dari seluruh anggota tim di arena",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            StatAverageRow("HP", summary.avgHp, 150, getStatColor("hp"))
            StatAverageRow("Attack", summary.avgAttack, 150, getStatColor("attack"))
            StatAverageRow("Defense", summary.avgDefense, 150, getStatColor("defense"))
            StatAverageRow("Sp. Atk", summary.avgSpAtk, 150, getStatColor("special-attack"))
            StatAverageRow("Sp. Def", summary.avgSpDef, 150, getStatColor("special-defense"))
            StatAverageRow("Speed", summary.avgSpeed, 150, getStatColor("speed"))
        }
    }
}

@Composable
private fun StatAverageRow(
    label: String,
    value: Int,
    maxValue: Int,
    barColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.width(60.dp)
        )
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            ),
            modifier = Modifier.width(36.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        LinearProgressIndicator(
            progress = { (value.toFloat() / maxValue.toFloat()).coerceIn(0f, 1f) },
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = barColor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

/**
 * Komponen Cakupan Efektivitas Ofensif (Super Effective 2x)
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun OffensiveCoverageSection(
    coverage: List<String>,
    memberCount: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = CobaltBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cakupan Efektivitas Ofensif",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = CobaltBlue.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "${coverage.size}/18 Tipe",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = CobaltBlue,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tipe musuh yang dapat diserang dengan damage Super Effective (2x):",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (coverage.isEmpty()) {
                Text(
                    text = if (memberCount == 0) "Belum ada Pokémon dalam tim." else "Tidak ada keunggulan tipe terdeteksi.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.outline
                    )
                )
            } else {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    coverage.forEach { type ->
                        TypeBadge(type = type)
                    }
                }
            }
        }
    }
}

/**
 * Komponen Analisis Kerentanan Defensif Tim
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DefensiveVulnerabilitySection(
    weaknesses: Map<String, Int>,
    memberCount: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.WarningAmber,
                    contentDescription = null,
                    tint = Color(0xFFF57C00),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Analisis Kerentanan Defensif Tim",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Frekuensi Pokémon dalam tim yang rentan menerima damage 2x:",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (weaknesses.isEmpty()) {
                Text(
                    text = if (memberCount == 0) "Tim masih kosong." else "Luar biasa! Tidak ada kerentanan bersama.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.SemiBold
                    )
                )
            } else {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    weaknesses.entries.forEach { entry ->
                        val isCritical = entry.value >= 3
                        val chipBg = if (isCritical) Color(0xFFFFEBEE) else MaterialTheme.colorScheme.surfaceVariant
                        val textColor = if (isCritical) Color(0xFFC62828) else MaterialTheme.colorScheme.onSurfaceVariant

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = chipBg,
                            border = if (isCritical) BorderStroke(1.dp, Color(0xFFEF5350)) else null
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val dotColor = getPokemonTypeColor(entry.key)
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(dotColor)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${entry.key.uppercase()} : ${entry.value}x",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = textColor,
                                        fontWeight = if (isCritical) FontWeight.Bold else FontWeight.Medium
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Modal BottomSheet untuk memilih Pokémon ke dalam Slot Tim
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokemonSelectorBottomSheet(
    slotNumber: Int,
    query: String,
    pokemonList: List<PokemonItem>,
    onQueryChange: (String) -> Unit,
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Pilih Pokémon untuk Slot #$slotNumber",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "Pilih dari 151 Pokémon katalog Kanto",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Search input
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Cari nama atau tipe Pokémon...") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // List of Pokémon
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(pokemonList, key = { it.id }) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(item.id) },
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            SubcomposeAsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(item.imageUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = item.displayName,
                                modifier = Modifier.size(60.dp),
                                contentScale = ContentScale.Fit
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.formattedId,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            )
                            Text(
                                text = item.displayName,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}
