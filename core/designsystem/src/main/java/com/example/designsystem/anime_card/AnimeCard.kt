package com.example.designsystem.anime_card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.designsystem.animated_shimmer.AnimatedShimmer
import com.example.designsystem.theme.mColors
import com.example.designsystem.theme.mShapes
import com.example.designsystem.theme.mTypography

@Composable
fun AnimeCard(
    posterPath: String,
    title: String,
    description: String,
    episodes: Int,
    averageScore: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {  }
            .padding(16.dp)
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(posterPath)
                .crossfade(500)
                .size(Size.ORIGINAL)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp, 130.dp)
                .clip(mShapes.small),
            filterQuality = FilterQuality.Low,
            contentScale = ContentScale.Crop,
            loading = { AnimatedShimmer(100.dp, 130.dp) }
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                style = mTypography.bodyLarge.copy(
                    color = mColors.primary,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "$episodes Episodes • ${averageScore.take(1)}.${averageScore.takeLast(1)}★",
                style = mTypography.bodyMedium.copy(
                    color = mColors.secondary,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = AnnotatedString.fromHtml(description),
                style = mTypography.bodyMedium.copy(
                    color = mColors.tertiary
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 3
            )
        }
    }
}