package me.kentkawa.bggpuller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import me.kentkawa.bggpuller.model.PlaysForUser
import java.net.URL
import java.time.LocalDate

class BggPuller(val xmlMapper: ObjectMapper) {
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
}
