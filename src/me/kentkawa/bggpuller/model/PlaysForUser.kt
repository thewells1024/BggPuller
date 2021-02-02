package me.kentkawa.bggpuller.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import me.kentkawa.bggpuller.xml.parser.PlaysForUserParser

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = PlaysForUserParser::class)
data class PlaysForUser(
    @JacksonXmlProperty(isAttribute = true) val username: String,
    @JacksonXmlProperty(isAttribute = true, localName = "total") override val totalItems: Int,
    @JacksonXmlProperty(isAttribute = false) val plays: List<Play>
) : PagedResult<Play> {
    override val items: List<Play>
        get() = plays
}
