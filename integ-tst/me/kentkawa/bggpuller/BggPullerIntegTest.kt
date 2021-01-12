package me.kentkawa.bggpuller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class BggPullerIntegTest {
    companion object {
        val MIN_DATE = LocalDate.parse("2021-01-01")
        val MAX_DATE = LocalDate.parse("2021-01-12")
    }

    val xmlMapper: ObjectMapper = XmlMapper().registerKotlinModule()

    @Test
    fun testGetPlaysForUser() {
        val requestConfig = BggPuller.PlaysForUserRequestConfig(minDate = MIN_DATE, maxDate = MAX_DATE)
        val puller = BggPuller(xmlMapper)
        val plays = puller.getPlaysForUser("thewells1024", requestConfig)
        assertEquals("thewells1024", plays.username)
        assertEquals(4, plays.plays.size)
    }
}
