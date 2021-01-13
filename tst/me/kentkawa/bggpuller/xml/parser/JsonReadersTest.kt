package me.kentkawa.bggpuller.xml.parser

import com.fasterxml.jackson.databind.JsonNode
import io.mockk.every
import io.mockk.mockk
import me.kentkawa.bggpuller.model.Game
import me.kentkawa.bggpuller.model.Play
import java.lang.IllegalStateException
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JsonReadersTest {
    private val json: JsonNode = mockk()

    @Test
    fun testReadCollectionEntry() {
        setupGameFromCollectionEntry(json)
        every { json.get("stats").get("rating").get("value").asDouble() } returns 5.5
        val entry = readCollectionEntry(json)
        checkGame(entry.game)
        assertEquals(5.5, entry.rating)
    }

    @Test
    fun testReadGameFromCollectionEntry() {
        setupGameFromCollectionEntry(json)
        val game = readGameFromCollectionEntry(json)
        checkGame(game)
    }

    @Test
    fun testReadPlay() {
        val gameJson: JsonNode = mockk()
        val playerJson: JsonNode = mockk()
        setupGame(gameJson)
        setupPlayer(playerJson)
        every { playerJson.isArray } returns false
        every { json.get("item") } returns gameJson
        every { json.get("date").asText() } returns "2021-01-01"
        every { json.get("players").get("player") } returns playerJson
        val play = readPlay(json)
        checkGame(play.game)
        assertEquals(LocalDate.parse("2021-01-01"), play.date)
        assertEquals(1, play.players.size)
        checkPlayer(play.players[0])
    }

    @Test
    fun testReadGame() {
        setupGame(json)
        val game = readGame(json)
        checkGame(game)
    }

    @Test
    fun testReadPlayer() {
        setupPlayer(json)
        val player = readPlayer(json)
        checkPlayer(player)
    }

    @Test
    fun testReadXmlBooleanTrue() {
        assertTrue(readXmlBoolean(1))
    }

    @Test
    fun testReadXmlBooleanFalse() {
        assertFalse(readXmlBoolean(0))
    }

    @Test(expected = IllegalStateException::class)
    fun testReadXmlBooleanThrowsIllegalStateException() {
        readXmlBoolean(7)
    }

    @Test
    fun testHandlePossibleListSingleValue() {
        every { json.isArray } returns false
        val singleton = handlePossibleList(json) { x -> x }
        assertEquals(1, singleton.size)
        assertEquals(json, singleton[0])
    }

    @Test
    fun testHandlePossibleListMultipleValues() {
        val arrayEntry1: JsonNode = mockk()
        val arrayEntry2: JsonNode = mockk()
        every { json.isArray } returns true
        every { json.iterator() } returns mutableListOf(arrayEntry1, arrayEntry2).iterator()
        val list = handlePossibleList(json) { x -> x }
        assertEquals(2, list.size)
        assertEquals(arrayEntry1, list[0])
        assertEquals(arrayEntry2, list[1])
    }

    private fun setupGameFromCollectionEntry(gameJson: JsonNode) {
        every { gameJson.get("name").get("").asText() } returns "testGame"
        every { gameJson.get("objectid").asInt() } returns 1234
    }

    private fun setupPlayer(playerJson: JsonNode) {
        every { playerJson.get("username").asText() } returns "testUsername"
        every { playerJson.get("name").asText() } returns "testName"
        every { playerJson.get("new").asInt() } returns 1
        every { playerJson.get("win").asInt() } returns 0
    }

    private fun setupGame(gameJson: JsonNode) {
        every { gameJson.get("name").asText() } returns "testGame"
        every { gameJson.get("objectid").asInt() } returns 1234
    }

    private fun checkPlayer(player: Play.Player) {
        assertEquals("testUsername", player.username)
        assertEquals("testName", player.name)
        assertTrue(player.new)
        assertFalse(player.won)
    }

    private fun checkGame(game: Game) {
        assertEquals("testGame", game.name)
        assertEquals(1234, game.bggId)
    }
}
