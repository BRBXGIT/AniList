package com.example.media_screen.media_screen.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.data.remote.models.media_details_response.TitleX
import com.example.designsystem.icons.AniKunIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaScreenTopBar(
    title: TitleX?,
    scrollBehavior: TopAppBarScrollBehavior,
    onNavIconClick: () -> Unit,
    onListIconClick: () -> Unit,
    onFavoriteIconClick: () -> Unit
) {
    var mediaTitle by rememberSaveable { mutableStateOf("") }
    LaunchedEffect(title) {
        if(title != null) {
            mediaTitle = if(title.english == null) title.romaji else title.english!!
        }
    }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        title = {
            AnimatedVisibility(
                visible = scrollBehavior.state.overlappedFraction >= 0.99f,
                enter = fadeIn(tween(300)),
                exit = fadeOut(tween(300))
            ) {
                Text(
                    text = mediaTitle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = { onNavIconClick() }
            ) {
                Icon(
                    painter = painterResource(id = AniKunIcons.ArrowLeftFilled),
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(
                onClick = {  }
            ) {
                Icon(
                    painter = painterResource(id = AniKunIcons.List),
                    contentDescription = null
                )
            }

            IconButton(
                onClick = {  }
            ) {
                Icon(
                    painter = painterResource(id = AniKunIcons.Heart),
                    contentDescription = null
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}