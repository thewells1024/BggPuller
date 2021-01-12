package me.kentkawa.bggpuller.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import me.kentkawa.bggpuller.xml.parser.PlaysForUserParser

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = PlaysForUserParser::class)
data class PlaysForUser(
    @JacksonXmlProperty(isAttribute = true) val username: String,
    @JacksonXmlProperty(isAttribute = false) val plays: List<Play>
)
