package me.kentkawa.bggpuller.model

import com.fasterxml.jackson.module.kotlin.readValue
import me.kentkawa.bggpuller.util.XmlParserBaseTest
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class PlaysForUserTest : XmlParserBaseTest() {
    companion object {
        val PLAYS_XML = File("tst-resources/plays.xml")
    }

    @Test
    fun testDeserialize() {
        val playsForUser: PlaysForUser = xmlMapper.readValue(PLAYS_XML)
        assertEquals("thewells1024", playsForUser.username)
        assertEquals(4, playsForUser.plays.size)
    }
}
