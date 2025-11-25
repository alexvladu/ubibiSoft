package com.ububi.explore_romania.ui.collection

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ububi.explore_romania.ui.stickers.Sticker
import com.ububi.explore_romania.ui.stickers.StickerRarity
import com.ububi.explore_romania.ui.stickers.StickerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.nio.ByteBuffer


class CollectionViewModel(
    private val repo: StickerRepository,
    private val context: Context
) : ViewModel() {

    private val _stickers = MutableStateFlow<List<Sticker>>(emptyList())
    val stickers: StateFlow<List<Sticker>> = _stickers

    private val _ownedIds = MutableStateFlow<Set<String>>(emptySet())
    val ownedIds: StateFlow<Set<String>> = _ownedIds

    enum class OwnershipFilter { OWNED, MISSING, ALL }
    enum class RarityFilter { ALL, COMMON, RARE, EPIC, LEGENDARY }

    private val _ownershipFilter = MutableStateFlow(OwnershipFilter.OWNED)
    val ownershipFilter: StateFlow<OwnershipFilter> = _ownershipFilter

    private val _rarityFilter = MutableStateFlow(RarityFilter.ALL)
    val rarityFilter: StateFlow<RarityFilter> = _rarityFilter

    private val _filteredStickers = MutableStateFlow<List<Sticker>>(emptyList())
    val filteredStickers: StateFlow<List<Sticker>> = _filteredStickers

    init {
        viewModelScope.launch {

            repo.addSticker("common_Ananas", StickerRarity.COMMON)
            repo.addSticker("common_Chitară", StickerRarity.COMMON)
            repo.addSticker("common_Stea_Strălucitoare", StickerRarity.COMMON)
            repo.addSticker("common_Onigiri", StickerRarity.COMMON)
            repo.addSticker("rare_Axolotl", StickerRarity.RARE)
            repo.addSticker("rare_Diplodocus", StickerRarity.RARE)
            repo.addSticker("rare_Cufăr", StickerRarity.RARE)
            repo.addSticker("rare_Fan_k-pop", StickerRarity.RARE)
            repo.addSticker("epic_ElGato", StickerRarity.EPIC)
            repo.addSticker("epic_Omul_Păianjen", StickerRarity.EPIC)
            repo.addSticker("legendary_Ballerina_Cappuccina", StickerRarity.LEGENDARY)

            val all = repo.loadAllStickers()
            _stickers.value = all

            val owned = repo.loadOwnedStickers().keys
            _ownedIds.value = owned

            updateFilter()
        }
    }

    private fun updateFilter() {
        val all = _stickers.value
        val owned = _ownedIds.value

        val filtered = all.filter { sticker ->

            val isOwned = owned.contains(sticker.id)

            val ownershipOK = when (_ownershipFilter.value) {
                OwnershipFilter.OWNED -> isOwned
                OwnershipFilter.MISSING -> !isOwned
                OwnershipFilter.ALL -> true
            }

            val rarityOK = when (_rarityFilter.value) {
                RarityFilter.ALL -> true
                RarityFilter.COMMON -> sticker.rarity == StickerRarity.COMMON
                RarityFilter.RARE -> sticker.rarity == StickerRarity.RARE
                RarityFilter.EPIC -> sticker.rarity == StickerRarity.EPIC
                RarityFilter.LEGENDARY -> sticker.rarity == StickerRarity.LEGENDARY
            }

            ownershipOK && rarityOK
        }

        _filteredStickers.value = filtered
    }

    fun setOwnershipFilter(filter: OwnershipFilter) {
        _ownershipFilter.value = filter
        updateFilter()
    }

    fun setRarityFilter(filter: RarityFilter) {
        _rarityFilter.value = filter
        updateFilter()
    }
}




@Suppress("UNCHECKED_CAST")
class CollectionVMFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = StickerRepository(context)
        return CollectionViewModel(repo, context) as T
    }
}
