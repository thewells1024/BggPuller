package me.kentkawa.bggpuller.model

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GameTest {
    val GAME_XML = "<item name=\"Terraforming Mars\" objecttype=\"thing\" objectid=\"167791\"></item>"
    var xmlMapper: ObjectMapper? = null

    @Before
    fun setup() {
        xmlMapper = XmlMapper().registerKotlinModule()
    }

    @Test
    fun testDeserialize() {
        val xmlMapper: ObjectMapper = XmlMapper()
        val game: Game = xmlMapper.readValue(GAME_XML)
        assertEquals("Terraforming Mars", game.name)
        assertEquals(167791, game.bggId)
    }
}