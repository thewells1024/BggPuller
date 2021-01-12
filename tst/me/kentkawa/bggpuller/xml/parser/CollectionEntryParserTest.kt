package me.kentkawa.bggpuller.xml.parser

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.lang.IllegalStateException
import kotlin.test.Test
import kotlin.test.assertEquals

class CollectionEntryParserTest {
    val p: JsonParser = mockk()
    val ctxt: DeserializationContext = mockk()

    @Test
    fun testDeserialize() {
        val converter = CollectionEntryParser()
        val json: JsonNode = mockk()
        every { ctxt.readTree(p) } returns json
        every { json.get("name").get("").asText() } returns "Terraforming Mars"
        every { json.get("objectid").asInt() } returns 167791
        every { json.get("stats").get("rating").get("value").asDouble() } returns 8.0
        val entry = converter.deserialize(p, ctxt)
        assertEquals("Terraforming Mars", entry.game.name)
        assertEquals(167791, entry.game.bggId)
        assertEquals(8.0, entry.rating)
        verify(exactly = 1) {
            ctxt.readTree(p)
            json.get("name").get("").asText()
            json.get("objectid").asInt()
            json.get("stats").get("rating").get("value").asDouble()
        }
    }

    @Test(expected = IllegalStateException::class)
    fun testDeserializeCtxtNull() {
        val converter = CollectionEntryParser()
        converter.deserialize(p, null)
    }

    @Test(expected = IllegalStateException::class)
    fun testDeserializeIllegalValue() {
        val converter = CollectionEntryParser()
        every { ctxt.readTree(p) } returns null
        converter.deserialize(p, ctxt)
    }
}
