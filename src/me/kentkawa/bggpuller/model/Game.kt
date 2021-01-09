package me.kentkawa.bggpuller.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "item")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Game(@JacksonXmlProperty(isAttribute = true, localName = "name") val name: String,
                @JacksonXmlProperty(isAttribute = true, localName = "objectid") val bggId: Int) {
}