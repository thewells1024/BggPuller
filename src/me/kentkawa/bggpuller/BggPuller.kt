package me.kentkawa.bggpuller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import me.kentkawa.bggpuller.errors.MultipleEntriesInBggCollectionResponseException
import me.kentkawa.bggpuller.model.BggCollection
import me.kentkawa.bggpuller.model.CollectionEntry
import me.kentkawa.bggpuller.model.Game
import me.kentkawa.bggpuller.model.PlaysForUser
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.IllegalStateException
import java.time.LocalDate

class BggPuller(private val xmlMapper: ObjectMapper, private val client: OkHttpClient) {
    companion object {
        const val SCHEME = "https"
        const val HOST = "www.boardgamegeek.com"
        const val API_BASE_PATH = "xmlapi2"
        const val COLLECTION_PATH = "collection"
        const val PLAY_PATH = "plays"
        const val BACKOFF_MULTIPLIER = 1.5
    }

    data class PlaysForUserRequestConfig(
        val minDate: LocalDate? = null,
        val maxDate: LocalDate? = null,
        val game: Game? = null
    )

    data class SearchCollectionConfig(
        val owned: Boolean? = null,
        val wishlistPriority: Int? = null,
        val minRating: Int? = null,
        val limit: Int? = null,
        val sortBy: SortType?,
        val sortDescending: Boolean = false
    )

    enum class SortType {
        RATING,
        NAME;

        fun getCompareWithComparator(): Comparator<CollectionEntry> {
            return Comparator { o1, o2 ->
                when (this@SortType) {
                    RATING -> o1.rating.compareTo(o2.rating)
                    NAME -> o1.game.name.compareTo(o2.game.name)
                }
            }
        }
    }

    fun getPlaysForUser(username: String, extraConfig: PlaysForUserRequestConfig? = null): PlaysForUser {
        val urlBuilder = getBggApiUrlBuilder()
            .addPathSegment(PLAY_PATH)
            .addQueryParameter("username", username)
        if (extraConfig?.minDate != null) {
            urlBuilder.addQueryParameter("mindate", extraConfig.minDate.toString())
        }
        if (extraConfig?.maxDate != null) {
            urlBuilder.addQueryParameter("maxdate", extraConfig.maxDate.toString())
        }
        if (extraConfig?.game != null) {
            urlBuilder.addQueryParameter("id", extraConfig.game.bggId.toString())
            urlBuilder.addQueryParameter("type", "thing")
        }
        val xmlData = sendGetRequest(urlBuilder.build())
        return xmlMapper.readValue(xmlData)
    }

    fun getCollectionEntry(username: String, game: Game): CollectionEntry {
        return getCollectionEntry(username, game.bggId)
    }

    fun getCollectionEntry(username: String, gameId: Int): CollectionEntry {
        val urlBuilder = getBggApiUrlBuilder()
            .addPathSegment(COLLECTION_PATH)
            .addQueryParameter("username", username)
            .addQueryParameter("id", gameId.toString())
            .addQueryParameter("stats", "1")
        val xmlData = sendGetRequest(urlBuilder.build(), retryOn202 = true)
        val bggCollection: BggCollection = xmlMapper.readValue(xmlData)
        if (bggCollection.entries.size != 1) {
            throw MultipleEntriesInBggCollectionResponseException(
                "There were ${bggCollection.entries.size} entries when" +
                    " there should have been exactly 1"
            )
        }
        return bggCollection.entries[0]
    }

    fun searchCollection(username: String, config: SearchCollectionConfig? = null):
        List<CollectionEntry> {
            val urlBuilder = getBggApiUrlBuilder()
                .addPathSegment(COLLECTION_PATH)
                .addQueryParameter("username", username)
                .addQueryParameter("stats", "1")
            if (config?.owned == true) {
                urlBuilder.addQueryParameter("own", "1")
            }
            if (config?.minRating != null) {
                urlBuilder.addQueryParameter("minrating", config.minRating.toString())
            }
            if (config?.wishlistPriority != null) {
                urlBuilder.addQueryParameter("wishlistpriority", config.wishlistPriority.toString())
            }
            val xmlData = sendGetRequest(urlBuilder.build(), retryOn202 = true)
            val bggResponse: BggCollection = xmlMapper.readValue(xmlData)
            return processResults(bggResponse.entries, config?.sortBy, config?.limit, config?.sortDescending ?: false)
        }

    private fun processResults(
        entries: List<CollectionEntry>,
        sortBy: SortType?,
        limit: Int?,
        sortDescending: Boolean
    ): List<CollectionEntry> {
        val sortedEntries = if (sortBy != null) {
            var comparator = sortBy.getCompareWithComparator()
            if (sortDescending) {
                comparator = comparator.reversed()
            }
            entries.sortedWith(comparator)
        } else {
            entries
        }
        return if (limit != null && sortedEntries.size > limit) {
            sortedEntries.dropLast(sortedEntries.size - limit)
        } else {
            sortedEntries
        }
    }

    private fun getBggApiUrlBuilder(): HttpUrl.Builder {
        return HttpUrl.Builder()
            .scheme(SCHEME)
            .host(HOST)
            .addPathSegment(API_BASE_PATH)
    }

    private fun sendGetRequest(url: HttpUrl, retryOn202: Boolean = false): String {
        var request = Request.Builder()
            .url(url)
            .build()
        var backoff = 1.0
        var response = client.newCall(request).execute()
        while (retryOn202 && response.code == 202) {
            response.close()
            Thread.sleep((backoff * 1000).toLong())
            backoff *= BACKOFF_MULTIPLIER
            response = client.newCall(request).execute()
        }
        return response.use { it.body?.string() ?: throw IllegalStateException("cannot read response") }
    }
}
