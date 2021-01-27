package me.kentkawa.bggpuller.xml.parser

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import me.kentkawa.bggpuller.model.BggCollection
import java.lang.IllegalStateException

class BggCollectionParser : StdDeserializer<BggCollection>(BggCollection::class.java) {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): BggCollection {
        val json = ctxt?.readTree(p) ?: throw IllegalStateException("cannot read tree")
        return BggCollection(
            json.get("totalitems").asInt(),
            handlePossibleList(json.get("item"), ::readCollectionEntry)
        )
    }
}
