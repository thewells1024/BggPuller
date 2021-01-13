package me.kentkawa.bggpuller.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import me.kentkawa.bggpuller.xml.parser.BggCollectionParser

@JacksonXmlRootElement(localName = "items")
@JsonDeserialize(using = BggCollectionParser::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class BggCollection(
    @JacksonXmlProperty(localName = "item")
    val entries: List<CollectionEntry>
)
