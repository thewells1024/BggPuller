package me.kentkawa.bggpuller.model

import com.fasterxml.jackson.module.kotlin.readValue
import me.kentkawa.bggpuller.util.XmlParserBaseTest
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class CollectionEntryTest : XmlParserBaseTest() {
    private val GAME_DETAILS_FILE = File("tst-resources/collection-entry.xml")

    @Test
    fun testParseGameDetails() {
        val entry: CollectionEntry = xmlMapper.readValue(GAME_DETAILS_FILE)
        assertEquals("Terraforming Mars", entry.game.name)
        assertEquals(167791, entry.game.bggId)
        assertEquals(8.0, entry.rating)
    }
}