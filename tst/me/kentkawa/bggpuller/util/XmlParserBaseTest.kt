package me.kentkawa.bggpuller.util;

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlin.test.BeforeTest

abstract class XmlParserBaseTest {
    val xmlMapper: ObjectMapper = XmlMapper()

    @BeforeTest
    fun setup() {
        xmlMapper.registerKotlinModule()
                .registerModule(JavaTimeModule())
    }
}
