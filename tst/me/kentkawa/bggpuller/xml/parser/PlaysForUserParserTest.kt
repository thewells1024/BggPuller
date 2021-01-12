package me.kentkawa.bggpuller.xml.parser

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import me.kentkawa.bggpuller.model.PlaysForUser
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class PlaysForUserParserTest {
    companion object {
        val PLAYS_JSON = File("tst-resources/plays.json")
    }
    @Test
    fun testDeserializePlays() {
        val objectMapper = ObjectMapper().registerKotlinModule()
        val playsForUser: PlaysForUser = objectMapper.readValue(PLAYS_JSON)
        assertEquals("thewells1024", playsForUser.username)
        assertEquals(4, playsForUser.plays.size)
    }
}