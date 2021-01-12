package me.kentkawa.bggpuller.xml.parser

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.lang.IllegalStateException

class XMLBooleanConverter : StdDeserializer<Boolean>(Boolean::class.java) {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Boolean {
        val node = ctxt?.readValue<Int>(p, Int::class.java)
            ?: throw IllegalStateException("Could not read value as Int")
        if (node != 0 && node != 1) {
            throw IllegalStateException("$node is not 0 or 1")
        }
        return node != 0
    }
}
