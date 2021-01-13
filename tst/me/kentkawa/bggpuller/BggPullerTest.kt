package me.kentkawa.bggpuller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.kentkawa.bggpuller.errors.MultipleEntriesInBggCollectionResponseException
import me.kentkawa.bggpuller.model.PlaysForUser
import me.kentkawa.bggpuller.util.XmlParserBaseTest
import org.junit.Test
import java.io.File
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.test.assertEquals

class BggPullerTest : XmlParserBaseTest() {
    companion object {
        const val PLAYS_XML = "tst-resources/plays.xml"
        const val COLLECTION_XML = "tst-resources/collection.xml"
        const val MULTIPLE_COLLECTION_XML = "tst-resources/multiple-item-collection.xml"
    }
    // mocking this to avoid a network call
    private val mockedXmlMapper: ObjectMapper = mockk()
    private val httpClient: HttpClient = mockk()

    @Test
    fun testGetPlaysForUsers() {
        val expectedUrl = URL("https://www.boardgamegeek.com/xmlapi2/plays?username=test")
        every { mockedXmlMapper.readValue(expectedUrl, any<TypeReference<PlaysForUser>>()) } returns
            xmlMapper.readValue(File(PLAYS_XML))
        val puller = BggPuller(mockedXmlMapper, httpClient)
        puller.getPlaysForUser("test")
        verify(exactly = 1) {
            mockedXmlMapper.readValue(expectedUrl, any<TypeReference<PlaysForUser>>())
        }
    }

    @Test
    fun testGetCollectionEntry() {
        val expectedUrl = URI("https://www.boardgamegeek.com/xmlapi2/collection?username=test&id=123&stats=1")
        val response: HttpResponse<String> = mockk()
        val slot = CapturingSlot<HttpRequest>()
        every { httpClient.send(capture(slot), HttpResponse.BodyHandlers.ofString()) } returns
            response
        every { response.statusCode() } returns 202 andThen 200
        every { response.body() } returns File(COLLECTION_XML).readText()
        val puller = BggPuller(xmlMapper, httpClient)
        puller.getCollectionEntry("test", 123)
        verify(exactly = 2) {
            httpClient.send(any(), HttpResponse.BodyHandlers.ofString())
            response.statusCode()
        }
        verify(exactly = 1) {
            response.body()
        }
        assertEquals(expectedUrl, slot.captured.uri())
    }

    @Test(expected = MultipleEntriesInBggCollectionResponseException::class)
    fun testGetCollectionEntryThrowsException() {
        val response: HttpResponse<String> = mockk()
        every { httpClient.send(any(), HttpResponse.BodyHandlers.ofString()) } returns
            response
        every { response.statusCode() } returns 202 andThen 200
        every { response.body() } returns File(MULTIPLE_COLLECTION_XML).readText()
        val puller = BggPuller(xmlMapper, httpClient)
        puller.getCollectionEntry("test", 123)
    }
}
