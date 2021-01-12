package me.kentkawa.bggpuller.xml.parser

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import me.kentkawa.bggpuller.model.CollectionEntry
import me.kentkawa.bggpuller.model.Game
import java.lang.IllegalStateException

class CollectionEntryParser : StdDeserializer<CollectionEntry>(CollectionEntry::class.java) {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): CollectionEntry {
        val json = ctxt?.readTree(p)
            ?: throw IllegalStateException("Cannot read object as JSON")
        print(json)
        val name = json.get("name").get("").asText()
        val gameId = json.get("objectid").asInt()
        val game = Game(name, gameId)
        val rating = json.get("stats").get("rating").get("value").asDouble()
        return CollectionEntry(game, rating)
    }
}
