package me.kentkawa.bggpuller

import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.kentkawa.bggpuller.errors.MultipleEntriesInBggCollectionResponseException
import me.kentkawa.bggpuller.model.Game
import me.kentkawa.bggpuller.util.XmlParserBaseTest
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.junit.Test
import java.io.File
import java.time.LocalDate
import kotlin.test.assertEquals

class BggPullerTest : XmlParserBaseTest() {
    companion object {
        const val PLAYS_XML = "tst-resources/plays.xml"
        const val COLLECTION_XML = "tst-resources/collection.xml"
        const val MULTIPLE_COLLECTION_XML = "tst-resources/multiple-item-collection.xml"
    }
    private val httpClient: OkHttpClient = mockk()

    @Test
    fun testGetPlaysForUsers() {
        val expectedUrl = "https://www.boardgamegeek.com/xmlapi2/plays?username=test".toHttpUrl()
        val response: Response = mockk()
        val slot = CapturingSlot<Request>()
        every { httpClient.newCall(capture(slot)).execute() } returns
            response
        every { response.code } returns 200
        every { response.body?.string() } returns File(PLAYS_XML).readText()
        every { response.close() } returns Unit
        val puller = BggPuller(xmlMapper, httpClient)
        puller.getPlaysForUser("test")
        verify(exactly = 1) {
            httpClient.newCall(any()).execute()
            response.body?.string()
        }
        assertEquals(expectedUrl, slot.captured.url)
    }

    @Test
    fun testGetPlaysForUsersWithMinDate() {
        val expectedUrl = "https://www.boardgamegeek.com/xmlapi2/plays?username=test&mindate=2021-01-01".toHttpUrl()
        val response: Response = mockk()
        val slot = CapturingSlot<Request>()
        every { httpClient.newCall(capture(slot)).execute() } returns
            response
        every { response.code } returns 200
        every { response.body?.string() } returns File(PLAYS_XML).readText()
        every { response.close() } returns Unit
        val puller = BggPuller(xmlMapper, httpClient)
        val playsForUserRequestConfig = BggPuller.PlaysForUserRequestConfig(minDate = LocalDate.parse("2021-01-01"))
        puller.getPlaysForUser("test", playsForUserRequestConfig)
        verify(exactly = 1) {
            httpClient.newCall(any()).execute()
            response.body?.string()
        }
        assertEquals(expectedUrl, slot.captured.url)
    }

    @Test
    fun testGetPlaysForUsersWithMaxDate() {
        val expectedUrl = "https://www.boardgamegeek.com/xmlapi2/plays?username=test&maxdate=2021-01-01".toHttpUrl()
        val response: Response = mockk()
        val slot = CapturingSlot<Request>()
        every { httpClient.newCall(capture(slot)).execute() } returns
            response
        every { response.code } returns 200
        every { response.body?.string() } returns File(PLAYS_XML).readText()
        every { response.close() } returns Unit
        val puller = BggPuller(xmlMapper, httpClient)
        val playsForUserRequestConfig = BggPuller.PlaysForUserRequestConfig(maxDate = LocalDate.parse("2021-01-01"))
        puller.getPlaysForUser("test", playsForUserRequestConfig)
        verify(exactly = 1) {
            httpClient.newCall(any()).execute()
            response.body?.string()
        }
        assertEquals(expectedUrl, slot.captured.url)
    }

    @Test
    fun testGetPlaysForUsersWithGame() {
        val expectedUrl = "https://www.boardgamegeek.com/xmlapi2/plays?username=test&id=123&type=thing".toHttpUrl()
        val response: Response = mockk()
        val slot = CapturingSlot<Request>()
        every { httpClient.newCall(capture(slot)).execute() } returns
            response
        every { response.code } returns 200
        every { response.body?.string() } returns File(PLAYS_XML).readText()
        every { response.close() } returns Unit
        val puller = BggPuller(xmlMapper, httpClient)
        val playsForUserRequestConfig = BggPuller.PlaysForUserRequestConfig(game = Game("testGame", 123))
        puller.getPlaysForUser("test", playsForUserRequestConfig)
        verify(exactly = 1) {
            httpClient.newCall(any()).execute()
            response.body?.string()
        }
        assertEquals(expectedUrl, slot.captured.url)
    }

    @Test
    fun testGetCollectionEntry() {
        val expectedUrl = "https://www.boardgamegeek.com/xmlapi2/collection?username=test&id=123&stats=1".toHttpUrl()
        val response: Response = mockk()
        val slot = CapturingSlot<Request>()
        every { httpClient.newCall(capture(slot)).execute() } returns
            response
        every { response.code } returns 202 andThen 200
        every { response.body?.string() } returns File(COLLECTION_XML).readText()
        every { response.close() } returns Unit
        val puller = BggPuller(xmlMapper, httpClient)
        puller.getCollectionEntry("test", 123)
        verify(exactly = 2) {
            httpClient.newCall(any()).execute()
            response.code
        }
        verify(exactly = 1) {
            response.body?.string()
        }
        assertEquals(expectedUrl, slot.captured.url)
    }

    @Test(expected = MultipleEntriesInBggCollectionResponseException::class)
    fun testGetCollectionEntryThrowsException() {
        val response: Response = mockk()
        every { httpClient.newCall(any()).execute() } returns
            response
        every { response.code } returns 200
        every { response.body?.string() } returns File(MULTIPLE_COLLECTION_XML).readText()
        every { response.close() } returns Unit
        val puller = BggPuller(xmlMapper, httpClient)
        puller.getCollectionEntry("test", 123)
    }
}
