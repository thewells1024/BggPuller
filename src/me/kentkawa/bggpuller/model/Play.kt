package me.kentkawa.bggpuller.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import me.kentkawa.bggpuller.xml.parser.XMLBooleanConverter
import java.time.LocalDate

@JsonIgnoreProperties(ignoreUnknown = true)
data class Play(@JacksonXmlProperty(isAttribute = false, localName = "item") val game: Game,
                @JacksonXmlProperty(isAttribute = true) val date: LocalDate,
                @JacksonXmlElementWrapper(useWrapping = true, localName = "players") val players: List<Player>) {
    @JacksonXmlRootElement(localName = "player")
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Player(@JacksonXmlProperty(isAttribute = true) val username: String,
                      @JacksonXmlProperty(isAttribute = true) val name: String,
                      @JacksonXmlProperty(isAttribute = true, localName = "new")
                          @JsonDeserialize(using = XMLBooleanConverter::class)
                          val new: Boolean,
                      @JacksonXmlProperty(isAttribute = true, localName = "win")
                          @JsonDeserialize(using = XMLBooleanConverter::class)
                          val won: Boolean) {

    }
}