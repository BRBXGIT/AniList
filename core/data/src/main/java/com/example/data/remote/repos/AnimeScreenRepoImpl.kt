package com.example.data.remote.repos

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.remote.api_instance.AniListApiInstance
import com.example.data.remote.models.anime_models.response.Media
import com.example.data.remote.paging.AnimeListsPS
import com.example.data.repos.AnimeScreenRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnimeScreenRepoImpl @Inject constructor(
    private val apiInstance: AniListApiInstance
): AnimeScreenRepo {

    override fun getAnimeList(
        sort: String,
        season: String?,
        seasonYear: Int?,
    ): Flow<PagingData<Media>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                AnimeListsPS(
                    apiInstance = apiInstance,
                    sort = sort,
                    season = season,
                    seasonYear = seasonYear
                )
            }
        ).flow
    }
}