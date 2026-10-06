package com.najmizahrian.pokedex.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.najmizahrian.pokedex.ui.theme.getPokemonTypeColor

/**
 * Chip / Badge representasi elemen Pokémon (Fire, Water, Grass, dll)
 */
@Composable
fun TypeBadge(
    type: String,
    modifier: Modifier = Modifier
) {
    val backgroundColor = getPokemonTypeColor(type)
    val displayType = type.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = displayType,
            color = Color.White,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        )
    }
}
