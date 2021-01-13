package me.kentkawa.bggpuller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import me.kentkawa.bggpuller.errors.MultipleEntriesInBggCollectionResponseException
import me.kentkawa.bggpuller.model.BggCollection
import me.kentkawa.bggpuller.model.CollectionEntry
import me.kentkawa.bggpuller.model.Game
import me.kentkawa.bggpuller.model.PlaysForUser
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.LocalDate

class BggPuller(private val xmlMapper: ObjectMapper, private val client: HttpClient) {
    private val BASE_URL = "https://www.boardgamegeek.com/xmlapi2"
    private val COLLECTION_PATH = "collection"
    private val PLAY_PATH = "plays"

    data class PlaysForUserRequestConfig(
        val minDate: LocalDate? = null,
        val maxDate: LocalDate? = null
    )

    fun getPlaysForUser(username: String, extraConfig: PlaysForUserRequestConfig? = null): PlaysForUser {
        var requestUrl = "$BASE_URL/$PLAY_PATH?username=$username"
        if (extraConfig?.minDate != null) {
            requestUrl += "&mindate=${extraConfig.minDate}"
        }
        if (extraConfig?.maxDate != null) {
            requestUrl += "&maxdate=${extraConfig.maxDate}"
        }
        return xmlMapper.readValue(URL(requestUrl))
    }

    fun getCollectionEntry(username: String, game: Game): CollectionEntry {
        return getCollectionEntry(username, game.bggId)
    }

    fun getCollectionEntry(username: String, gameId: Int): CollectionEntry {
        var requestUrl = "$BASE_URL/$COLLECTION_PATH?username=$username&id=$gameId&stats=1"
        val bggCollection: BggCollection = xmlMapper.readValue(readWithRetries(requestUrl))
        if (bggCollection.entries.size != 1) {
            throw MultipleEntriesInBggCollectionResponseException(
                "There were ${bggCollection.entries.size} entries when" +
                    " there should have been exactly 1"
            )
        }
        return bggCollection.entries[0]
    }

    private fun readWithRetries(url: String): String {
        val request = HttpRequest.newBuilder(URI(url))
            .GET()
            .build()
        var backoff = 1.0
        var response = client.send(request, HttpResponse.BodyHandlers.ofString())
        while (response?.statusCode() != 200) {
            Thread.sleep((backoff * 1000).toLong())
            backoff *= 1.5
            response = client.send(request, HttpResponse.BodyHandlers.ofString())
        }
        return response.body()
    }
}
