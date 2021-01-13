package me.kentkawa.bggpuller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.kentkawa.bggpuller.model.PlaysForUser
import me.kentkawa.bggpuller.util.XmlParserBaseTest
import org.junit.Test
import java.io.File
import java.net.URL

class BggPullerTest : XmlParserBaseTest() {
    companion object {
        const val PLAYS_XML = "tst-resources/plays.xml"
    }
    // mocking this to avoid a network call
    private val mockedXmlMapper: ObjectMapper = mockk()

    @Test
    fun testGetPlaysForUsers() {
        val expectedUrl = URL("https://www.boardgamegeek.com/xmlapi2/plays?username=test")
        every { mockedXmlMapper.readValue(expectedUrl, any<TypeReference<PlaysForUser>>()) } returns
            xmlMapper.readValue(File(PLAYS_XML))
        val puller = BggPuller(mockedXmlMapper)
        puller.getPlaysForUser("test")
        verify(exactly = 1) {
            mockedXmlMapper.readValue(expectedUrl, any<TypeReference<PlaysForUser>>())
        }
    }
}
