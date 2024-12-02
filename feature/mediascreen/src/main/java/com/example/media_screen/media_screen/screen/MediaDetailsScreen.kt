package com.example.media_screen.media_screen.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.data.remote.models.media_details_models.user_media_lists_response.UserMediaListsResponse
import com.example.data.remote.models.profile_models.user_data_response.AniListUser
import com.example.designsystem.error_section.ErrorSection
import com.example.designsystem.theme.mColors
import com.example.media_screen.media_screen.sections.CharactersLRSection
import com.example.media_screen.media_screen.sections.DescriptionSection
import com.example.media_screen.media_screen.sections.GenresLRSection
import com.example.media_screen.media_screen.sections.InfoSection
import com.example.media_screen.media_screen.sections.MediaHeader
import com.example.media_screen.media_screen.sections.MediaScreenTopBar
import com.example.media_screen.media_screen.sections.RecommendationsLRSection
import com.example.media_screen.media_screen.sections.TagsSection
import com.example.media_screen.media_screen.sections.UserListTypeSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaDetailsScreen(
    mediaId: Int,
    viewModel: MediaScreenVM,
    navController: NavController,
    aniListUser: AniListUser
) {
    //Get and collect media details
    viewModel.fetchMediaDetailsById(mediaId)
    val mediaDetails = viewModel.mediaDetails.collectAsStateWithLifecycle().value
    //Get and collect user media lists
    val userLists = viewModel.userMediaLists.collectAsStateWithLifecycle().value
    if(mediaDetails.data != null) {
        viewModel.fetchUserMediaLists(
            userName = aniListUser.data.viewer.name,
            type = mediaDetails.data!!.media.type
        )
    }
    var userListType by rememberSaveable { mutableStateOf("") }
    if(userLists.data != null) {
        userListType = checkIsMediaInUserList(userLists, mediaId)
    }

    val topBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            MediaScreenTopBar(
                title = mediaDetails.data?.media?.title,
                scrollBehavior = topBarScrollBehavior,
                onNavIconClick = { navController.navigateUp() },
                onFavoriteIconClick = {  },
                onListIconClick = {  }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(mColors.background)
            .nestedScroll(topBarScrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        if((mediaDetails.data == null) && (mediaDetails.exception == null)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if(mediaDetails.exception != null) {
            ErrorSection(
                errorText = mediaDetails.exception.toString(),
                modifier = Modifier.fillMaxSize()
            )
        }

        if(mediaDetails.data != null) {
            mediaDetails.data!!.media.let { media ->
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = innerPadding.calculateBottomPadding())
                ) {
                    item {
                        MediaHeader(
                            topPadding = innerPadding.calculateTopPadding(),
                            coverImage = media.coverImage.large,
                            title = if(media.title.english == null) media.title.romaji else media.title.english!!,
                            year = media.seasonYear,
                            format = media.format,
                            episodes = media.episodes,
                            nextAiringEpisode = media.nextAiringEpisode,
                            bannerImage = media.bannerImage,
                        )
                    }

                    item {
                        UserListTypeSection(
                            score = "${media.averageScore.toString().take(1)}.${media.averageScore.toString().takeLast(1)}",
                            favoritesCount = media.favourites,
                            userListType = userListType,
                            popularityCount = media.popularity,
                            modifier = Modifier.fillParentMaxWidth()
                        )
                    }

                    item {
                        GenresLRSection(media.genres)
                    }

                    item {
                        DescriptionSection(media.description)
                    }

                    item {
                        CharactersLRSection(media.characters)
                    }

                    item {
                        InfoSection(
                            title = media.title,
                            format = media.format,
                            episodes = media.episodes,
                            chapters = media.chapters,
                            episodeDuration = media.duration,
                            source = media.source,
                            status = media.status,
                            startDate = media.startDate,
                            endDate = media.endDate,
                            season = media.season,
                            seasonYear = media.seasonYear,
                            studios = media.studios
                        )
                    }

                    item {
                        TagsSection(media.tags)
                    }

                    item {
                        RecommendationsLRSection(
                            recommendations = media.recommendations,
                            navController = navController
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(0.dp))
                    }
                }
            }
        }
    }
}

private fun checkIsMediaInUserList(
    userList: UserMediaListsResponse,
    mediaId: Int
): String {
    userList.data!!.mediaListCollection.lists.forEach { list ->
        list.entries.forEach { entry ->
            if(mediaId == entry.media.id) {
                return list.name
            }
        }
    }
    return "Not in list"
}