package com.example.navbarscreens.manga_screen.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.designsystem.theme.mColors
import com.example.navbarscreens.common.navbar.NavBar
import com.example.navbarscreens.common.pager.CommonPager
import com.example.navbarscreens.common.search_bar.NavbarScreensSearchBar
import com.example.navbarscreens.common.topbar.NavBarScreensTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaScreen(
    navController: NavController,
    viewModel: MangaScreenVM
) {
    val topBarScrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var isSearching by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        topBar = {
            NavBarScreensTopBar(
                text = "Manga",
                scrollBehavior = topBarScrollBehaviour,
                onSearchClick = { isSearching = true }
            )
        },
        bottomBar = { NavBar(navController) },
        modifier = Modifier
            .fillMaxSize()
            .background(mColors.background)
            .nestedScroll(topBarScrollBehaviour.nestedScrollConnection)
    ) { innerPadding ->
        if(isSearching) {
            val mangaByQuery = viewModel.mangaByQuery.collectAsLazyPagingItems()

            NavbarScreensSearchBar(
                placeHolderText = "Find manga",
                onExpandChange = { isSearching = false },
                onSearchClick = { viewModel.setQuery(it) },
                mediaByQuery = mangaByQuery
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val trendingManga = viewModel.trendingManga.collectAsLazyPagingItems()
            val thisSeasonManga = viewModel.thisSeasonManga.collectAsLazyPagingItems()
            val nextSeasonManga = viewModel.nextSeasonManga.collectAsLazyPagingItems()
            val allTimePopularManga = viewModel.allTimePopularManga.collectAsLazyPagingItems()

            CommonPager(
                anime = emptyList(),
                manga = listOf(
                    trendingManga,
                    thisSeasonManga,
                    nextSeasonManga,
                    allTimePopularManga
                )
            )
        }
    }
}