package me.kentkawa.bggpuller.model

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import me.kentkawa.bggpuller.util.XmlParserBaseTest
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class GameTest: XmlParserBaseTest() {
    val GAME_FILE = File("tst-resources/game.xml")

    @Test
    fun testDeserialize() {
        val game: Game = this.xmlMapper.readValue(GAME_FILE)
        assertEquals("Terraforming Mars", game.name)
        assertEquals(167791, game.bggId)
    }
}