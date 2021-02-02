package me.kentkawa.bggpuller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import okhttp3.OkHttpClient
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class BggPullerIntegTest {
    companion object {
        val MIN_DATE = LocalDate.parse("2021-01-01")
        val MAX_DATE = LocalDate.parse("2021-01-12")
    }

    val xmlMapper: ObjectMapper = XmlMapper().registerKotlinModule()
    val client = OkHttpClient()

    @Test
    fun testGetPlaysForUser() {
        val requestConfig = BggPuller.PlaysForUserRequestConfig(minDate = MIN_DATE, maxDate = MAX_DATE)
        val puller = BggPuller(xmlMapper, client)
        val plays = puller.getPlaysForUser("thewells1024", requestConfig)
        assertEquals(4, plays.size)
    }

    @Test
    fun testGetCollectionEntry() {
        val puller = BggPuller(xmlMapper, client)
        val entry = puller.getCollectionEntry("thewells1024", 167791)
        assertEquals("Terraforming Mars", entry.game.name)
    }
}
