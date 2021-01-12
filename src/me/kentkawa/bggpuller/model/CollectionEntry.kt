package me.kentkawa.bggpuller.model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import me.kentkawa.bggpuller.xml.parser.CollectionEntryParser

@JsonDeserialize(using = CollectionEntryParser::class)
data class CollectionEntry(
    val game: Game,
    val rating: Double
)
