package com.example.navbarscreens.favorites_screen.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.data.remote.models.profile_models.user_favorites_response.Node
import com.example.designsystem.animated_shimmer.AnimatedShimmer
import com.example.designsystem.theme.mColors
import com.example.designsystem.theme.mShapes
import com.example.designsystem.theme.mTypography
import com.example.media_screen.character_screen.navigation.CharacterScreenRoute

@Composable
fun FavoriteCharacterLVGSection(
    favoriteCharacters: List<Node>,
    navController: NavController
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 16.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(favoriteCharacters) { index, character ->
            CharacterCard(
                character = character,
                index = index,
                onCardClick = {
                    navController.navigate(CharacterScreenRoute(character.id))
                }
            )
        }
    }
}

@Composable
private fun CharacterCard(
    onCardClick: () -> Unit,
    character: Node,
    index: Int
) {
    Card(
        shape = mShapes.small,
        onClick = { onCardClick() },
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(character.image.large)
                    .crossfade(500)
                    .size(Size.ORIGINAL)
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                filterQuality = FilterQuality.Low,
                contentScale = ContentScale.Crop,
                loading = { if(index <= 6) AnimatedShimmer(100.dp, 130.dp) }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(mColors.secondaryContainer)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = character.name.full,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = mTypography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )


                Text(
                    text = character.name.native,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = mTypography.bodySmall.copy(
                        color = mColors.secondary
                    )
                )
            }
        }
    }
}