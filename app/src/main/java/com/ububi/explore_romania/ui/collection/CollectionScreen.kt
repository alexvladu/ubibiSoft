package com.ububi.explore_romania.ui.collection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ububi.explore_romania.R
import com.ububi.explore_romania.ui.stickers.Sticker
import com.ububi.explore_romania.ui.collection.CollectionViewModel.OwnershipFilter
import com.ububi.explore_romania.ui.collection.CollectionViewModel.RarityFilter
import com.ububi.explore_romania.ui.theme.ScreenBackground

@Composable
fun CollectionScreen(
    stickers: List<Sticker>,
    ownershipFilter: OwnershipFilter,
    rarityFilter: RarityFilter,
    onOwnershipFilterChange: (OwnershipFilter) -> Unit,
    onRarityFilterChange: (RarityFilter) -> Unit,
    ownedIds: Set<String>
)
 {
    val MarioFont = FontFamily(Font(R.font.retromario))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .padding(16.dp)
    ) {
        Text(
            text = "Colecție de Stickere",
            fontFamily = MarioFont,
            fontSize = 46.sp,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        FilterRow(
            ownershipFilter = ownershipFilter,
            rarityFilter = rarityFilter,
            onOwnershipFilterChange = onOwnershipFilterChange,
            onRarityFilterChange = onRarityFilterChange
        )

        Spacer(modifier = Modifier.height(12.dp))

        StickerGrid(
            stickers = stickers,
            ownedIds = ownedIds
        )
    }
}


@Composable
fun FilterRow(
    ownershipFilter: OwnershipFilter,
    rarityFilter: RarityFilter,
    onOwnershipFilterChange: (OwnershipFilter) -> Unit,
    onRarityFilterChange: (RarityFilter) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OwnershipFilterDropdown(
            selected = ownershipFilter,
            onSelected = onOwnershipFilterChange
        )

        RarityFilterDropdown(
            selected = rarityFilter,
            onSelected = onRarityFilterChange
        )
    }
}

@Composable
fun OwnershipFilterDropdown(
    selected: OwnershipFilter,
    onSelected: (OwnershipFilter) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF424242))
        ) {
            Text(text = when (selected) {
                OwnershipFilter.OWNED -> "Deținute"
                OwnershipFilter.MISSING -> "Negăsite"
                OwnershipFilter.ALL -> "Toate"
            })
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Deținute") },
                onClick = { expanded = false; onSelected(OwnershipFilter.OWNED) }
            )
            DropdownMenuItem(
                text = { Text("Negăsite") },
                onClick = { expanded = false; onSelected(OwnershipFilter.MISSING) }
            )
            DropdownMenuItem(
                text = { Text("Toate") },
                onClick = { expanded = false; onSelected(OwnershipFilter.ALL) }
            )
        }
    }
}

@Composable
fun RarityFilterDropdown(
    selected: RarityFilter,
    onSelected: (RarityFilter) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF424242))
        ) {
            Text(text = when (selected) {
                RarityFilter.ALL -> "Toate"
                RarityFilter.COMMON -> "Comune"
                RarityFilter.RARE -> "Rare"
                RarityFilter.EPIC -> "Epice"
                RarityFilter.LEGENDARY -> "Legendare"
            })
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Toate") },
                onClick = { expanded = false; onSelected(RarityFilter.ALL) }
            )
            DropdownMenuItem(
                text = { Text("Comune") },
                onClick = { expanded = false; onSelected(RarityFilter.COMMON) }
            )
            DropdownMenuItem(
                text = { Text("Rare") },
                onClick = { expanded = false; onSelected(RarityFilter.RARE) }
            )
            DropdownMenuItem(
                text = { Text("Epice") },
                onClick = { expanded = false; onSelected(RarityFilter.EPIC) }
            )
            DropdownMenuItem(
                text = { Text("Legendare") },
                onClick = { expanded = false; onSelected(RarityFilter.LEGENDARY) }
            )
        }
    }
}
