package com.ububi.explore_romania.ui.collection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ububi.explore_romania.MusicManager
import com.ububi.explore_romania.MusicTrack
import com.ububi.explore_romania.R
import com.ububi.explore_romania.ui.stickers.Sticker
import com.ububi.explore_romania.ui.collection.CollectionViewModel.OwnershipFilter
import com.ububi.explore_romania.ui.collection.CollectionViewModel.RarityFilter
import com.ububi.explore_romania.ui.stickers.StickerRarity
import com.ububi.explore_romania.ui.theme.BackButtonColor
import com.ububi.explore_romania.ui.theme.FilterButtonColor
import com.ububi.explore_romania.ui.theme.FilterDropdownColor
import com.ububi.explore_romania.ui.theme.ScreenBackground

@Composable
fun CollectionScreen(
    stickers: List<Sticker>,
    ownershipFilter: OwnershipFilter,
    rarityFilter: RarityFilter,
    onOwnershipFilterChange: (OwnershipFilter) -> Unit,
    onRarityFilterChange: (RarityFilter) -> Unit,
    ownedIds: Set<String>,
    onBackClick: () -> Unit
) {
    val MarioFont = FontFamily(Font(R.font.retromario))

    val DarkGreenBorder = Color(0xFF192E29)
    val LightGreenBg = Color(0xFFE8FCE6)

    LaunchedEffect(Unit) {
        MusicManager.playTrack(MusicTrack.COLLECTION)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.sticker),
                contentScale = ContentScale.Crop
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Button(
                    onClick = onBackClick,
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreenBorder),
                    modifier = Modifier.size(52.dp),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("←", fontFamily = MarioFont, fontSize = 28.sp, color = LightGreenBg)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Colecție de Stickere",
                    fontFamily = MarioFont,
                    fontSize = 40.sp,
                    color = Color.White,
                    style = androidx.compose.ui.text.TextStyle(
                        shadow = androidx.compose.ui.graphics.Shadow(
                            color = DarkGreenBorder,
                            offset = androidx.compose.ui.geometry.Offset(4f, 4f),
                            blurRadius = 2f
                        )
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            FilterRow(
                ownershipFilter = ownershipFilter,
                rarityFilter = rarityFilter,
                onOwnershipFilterChange = onOwnershipFilterChange,
                onRarityFilterChange = onRarityFilterChange,
                buttonColor = DarkGreenBorder,
                dropdownColor = LightGreenBg
            )

            Spacer(modifier = Modifier.height(12.dp))

            StickerGrid(
                stickers = stickers,
                ownedIds = ownedIds
            )
        }
    }
}



@Composable
fun FilterRow(
    ownershipFilter: OwnershipFilter,
    rarityFilter: RarityFilter,
    onOwnershipFilterChange: (OwnershipFilter) -> Unit,
    onRarityFilterChange: (RarityFilter) -> Unit,
    buttonColor: Color,
    dropdownColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OwnershipFilterDropdown(
            selected = ownershipFilter,
            onSelected = onOwnershipFilterChange,
            buttonColor = buttonColor,
            dropdownColor = dropdownColor
        )
        RarityFilterDropdown(
            selected = rarityFilter,
            onSelected = onRarityFilterChange,
            buttonColor = buttonColor,
            dropdownColor = dropdownColor
        )
    }
}

@Composable
fun OwnershipFilterDropdown(
    selected: OwnershipFilter,
    onSelected: (OwnershipFilter) -> Unit,
    buttonColor: Color,    // Parametru adăugat
    dropdownColor: Color   // Parametru adăugat
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(text = when (selected) {
                OwnershipFilter.OWNED -> "Deținute"
                OwnershipFilter.MISSING -> "Negăsite"
                OwnershipFilter.ALL -> "Toate"
            }, color = Color.White)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, containerColor = dropdownColor) {
            DropdownMenuItem(text = { Text("Deținute", color = Color(0xFF192E29)) }, onClick = { expanded = false; onSelected(OwnershipFilter.OWNED) })
            DropdownMenuItem(text = { Text("Negăsite", color = Color(0xFF192E29)) }, onClick = { expanded = false; onSelected(OwnershipFilter.MISSING) })
            DropdownMenuItem(text = { Text("Toate", color = Color(0xFF192E29)) }, onClick = { expanded = false; onSelected(OwnershipFilter.ALL) })
        }
    }
}

@Composable
fun RarityFilterDropdown(
    selected: RarityFilter,
    onSelected: (RarityFilter) -> Unit,
    buttonColor: Color,    // Parametru adăugat
    dropdownColor: Color   // Parametru adăugat
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(text = when (selected) {
                RarityFilter.ALL -> "Toate"
                RarityFilter.COMMON -> "Comune"
                RarityFilter.RARE -> "Rare"
                RarityFilter.EPIC -> "Epice"
                RarityFilter.LEGENDARY -> "Legendare"
            }, color = Color.White)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, containerColor = dropdownColor) {
            DropdownMenuItem(text = { Text("Toate", color = Color(0xFF192E29)) }, onClick = { expanded = false; onSelected(RarityFilter.ALL) })
            DropdownMenuItem(text = { Text("Comune", color = Color(0xFF192E29)) }, onClick = { expanded = false; onSelected(RarityFilter.COMMON) })
            DropdownMenuItem(text = { Text("Rare", color = Color(0xFF192E29)) }, onClick = { expanded = false; onSelected(RarityFilter.RARE) })
            DropdownMenuItem(text = { Text("Epice", color = Color(0xFF192E29)) }, onClick = { expanded = false; onSelected(RarityFilter.EPIC) })
            DropdownMenuItem(text = { Text("Legendare", color = Color(0xFF192E29)) }, onClick = { expanded = false; onSelected(RarityFilter.LEGENDARY) })
        }
    }
}

@Preview(
    name = "Collection Screen – Tablet Landscape",
    showBackground = true,
    widthDp = 1280,
    heightDp = 800
)
@Composable
fun PreviewCollectionScreenTablet() {

    // mock stickers
    // nu o sa arate poza cu sticker-ul
    val mockStickers = listOf(
        Sticker(
            id = "common_Cangur",
            name = "Cangur",
            rarity = StickerRarity.COMMON,
            assetPath = "stickers/common/Cangur.png",
            grayAssetPath = "stickers/common/Cangur_gray.png"
        ),
        Sticker(
            id = "rare_Boba",
            name = "Boba",
            rarity = StickerRarity.RARE,
            assetPath = "stickers/rare/Boba.png",
            grayAssetPath = "stickers/rare/Boba_gray.png"
        ),
        Sticker(
            id = "epic_BMO",
            name = "BMO",
            rarity = StickerRarity.EPIC,
            assetPath = "stickers/epic/BMO.png",
            grayAssetPath = "stickers/epic/BMO_gray.png"
        ),
        Sticker(
            id = "legendary_Mira",
            name = "Mira",
            rarity = StickerRarity.LEGENDARY,
            assetPath = "stickers/legendary/Mira.png",
            grayAssetPath = "stickers/legendary/Mira_gray.png"
        )
    )

    val ownedMock = setOf(
        "common_Cangur",
        "epic_BMO"
    )

    CollectionScreen(
        stickers = mockStickers,
        ownershipFilter = CollectionViewModel.OwnershipFilter.ALL,
        rarityFilter = CollectionViewModel.RarityFilter.ALL,
        onOwnershipFilterChange = {},
        onRarityFilterChange = {},
        ownedIds = ownedMock,
        onBackClick = {}
    )
}
