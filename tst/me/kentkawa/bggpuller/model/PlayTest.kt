package me.kentkawa.bggpuller.model

import com.fasterxml.jackson.module.kotlin.readValue
import me.kentkawa.bggpuller.util.XmlParserBaseTest
import java.io.File
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PlayTest : XmlParserBaseTest() {
    private val PLAYER_FILE = File("tst-resources/player.xml")
    private val PLAY_FILE = File("tst-resources/play.xml")

    @Test
    fun testParsePlayer() {
        val player: Play.Player = this.xmlMapper.readValue(PLAYER_FILE)
        assertEquals("thewells1024", player.username)
        assertEquals("Kent", player.name)
        assertFalse(player.new)
        assertTrue(player.won)
    }

    @Test
    fun testParsePlay() {
        val play: Play = this.xmlMapper.readValue(PLAY_FILE)
        val expectedGame = Game("Terraforming Mars", 167791)
        val expectedDate = LocalDate.parse("2021-01-02")
        assertEquals(expectedGame, play.game)
        assertEquals(expectedDate, play.date)
        assertEquals(2, play.players.size)
    }
}
