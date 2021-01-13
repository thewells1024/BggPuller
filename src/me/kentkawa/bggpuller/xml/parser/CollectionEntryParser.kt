package me.kentkawa.bggpuller.xml.parser

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import me.kentkawa.bggpuller.model.CollectionEntry
import java.lang.IllegalStateException

class CollectionEntryParser : StdDeserializer<CollectionEntry>(CollectionEntry::class.java) {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): CollectionEntry {
        val json = ctxt?.readTree(p)
            ?: throw IllegalStateException("Cannot read object as JSON")
        return readCollectionEntry(json)
    }
}
