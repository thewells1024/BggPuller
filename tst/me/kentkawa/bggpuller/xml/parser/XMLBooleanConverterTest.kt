package me.kentkawa.bggpuller.xml.parser

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.lang.IllegalStateException
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class XMLBooleanConverterTest {
    val p: JsonParser = mockk()
    val ctxt: DeserializationContext = mockk()

    @Test
    fun testDeserializeTrue() {
        val converter = XMLBooleanConverter()
        every {ctxt.readValue<Int>(p, Int::class.java)} returns 1
        assertTrue(converter.deserialize(p, ctxt))
        verify(exactly = 1) {
            ctxt.readValue<Int>(p, Int::class.java)
        }
    }

    @Test
    fun testDeserializeFalse() {
        val converter = XMLBooleanConverter()
        every {ctxt.readValue<Int>(p, Int::class.java)} returns 0
        assertFalse(converter.deserialize(p, ctxt))
        verify(exactly = 1) {
            ctxt.readValue<Int>(p, Int::class.java)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun testDeserializeCtxtNull() {
        val converter = XMLBooleanConverter()
        converter.deserialize(p, null)
    }

    @Test(expected = IllegalStateException::class)
    fun testDeserializeIllegalValue() {
        val converter = XMLBooleanConverter()
        every {ctxt.readValue<Int>(p, Int::class.java)} returns -1
        converter.deserialize(p, ctxt)
    }
}