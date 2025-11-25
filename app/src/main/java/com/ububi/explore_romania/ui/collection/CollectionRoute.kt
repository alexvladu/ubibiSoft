package com.ububi.explore_romania.ui.collection

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun CollectionRoute() {

    val context = LocalContext.current
    val vm: CollectionViewModel = viewModel(factory = CollectionVMFactory(context))

    val filteredStickers by vm.filteredStickers.collectAsState()
    val ownershipFilter by vm.ownershipFilter.collectAsState()
    val rarityFilter by vm.rarityFilter.collectAsState()
    val images by vm.images.collectAsState()
    val ownedIds by vm.ownedIds.collectAsState()
    val grayImages by vm.grayImages.collectAsState()

    CollectionScreen(
        stickers = filteredStickers,
        ownershipFilter = ownershipFilter,
        rarityFilter = rarityFilter,
        onOwnershipFilterChange = vm::setOwnershipFilter,
        onRarityFilterChange = vm::setRarityFilter,
        ownedIds = ownedIds,
        images = images,
        grayImages = grayImages
    )
}
