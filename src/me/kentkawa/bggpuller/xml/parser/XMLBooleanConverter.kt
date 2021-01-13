package me.kentkawa.bggpuller.xml.parser

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.lang.IllegalStateException

class XMLBooleanConverter : StdDeserializer<Boolean>(Boolean::class.java) {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Boolean {
        val node = ctxt?.readValue<Int>(p, Int::class.java)
            ?: throw IllegalStateException("Could not read value as Int")
        return readXmlBoolean(node)
    }
}
