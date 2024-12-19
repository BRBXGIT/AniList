package com.example.navbarscreens.favorites_screen.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.data.remote.models.profile_models.user_favorites_response.Node
import com.example.designsystem.custom_modifiers.customTabIndicatorOffset
import com.example.designsystem.theme.mColors
import com.example.designsystem.theme.mShapes
import kotlinx.coroutines.launch
import com.example.data.remote.models.anime_list_response.Media as AnimeListMedia
import com.example.data.remote.models.manga_list_response.Media as MangaListMedia

@Composable
fun UserFavoritesPager(
    userFavoriteAnime: List<AnimeListMedia>,
    userFavoriteManga: List<MangaListMedia>,
    userFavoriteCharacters: List<Node>,
    navController: NavController
) {
    var selectedType by rememberSaveable { mutableIntStateOf(0) }
    val animationScope = rememberCoroutineScope()

    val userFavoritesTypes = listOf(
        "Anime",
        "Manga",
        "Characters"
    )

    val pagerState = rememberPagerState(
        pageCount = { userFavoritesTypes.size }
    )

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            selectedType = page
        }
    }

    TabRow(
        selectedTabIndex = selectedType,
        divider = {
            HorizontalDivider(
                thickness = 1.dp,
            )
        },
        indicator = { tabPositions ->
            if(selectedType < tabPositions.size) {
                Box(
                    modifier = Modifier
                        .customTabIndicatorOffset(
                            currentTabPosition = tabPositions[selectedType],
                            width = 50.dp
                        )
                        .height(3.dp)
                        .background(
                            color = mColors.primary,
                            shape = RoundedCornerShape(3.dp)
                        )
                )
            }
        }
    ) {
        userFavoritesTypes.forEachIndexed { index, type ->
            Tab(
                selected = index == selectedType,
                onClick = {
                    selectedType = index
                    animationScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(mShapes.small),
                text = {
                    Text(
                        text = type
                    )
                }
            )
        }
    }

    HorizontalPager(state = pagerState) { page ->
        when(page) {
            0 -> FavoriteAnimeLCSection(userFavoriteAnime, navController)
            1 -> FavoriteMangaLVGSection(userFavoriteManga, navController)
            2 -> FavoriteCharacterLVGSection(userFavoriteCharacters, navController)
        }
    }
}