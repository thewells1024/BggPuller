package me.kentkawa.bggpuller.xml.parser

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import me.kentkawa.bggpuller.model.PlaysForUser
import java.lang.IllegalStateException

class PlaysForUserParser : StdDeserializer<PlaysForUser>(PlaysForUser::class.java) {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): PlaysForUser {
        val json = ctxt?.readTree(p) ?: throw IllegalStateException("Cannot read tree")
        val username = json.get("username").textValue()
        val totalItems = json.get("total").asInt()
        val plays = handlePossibleList(json.get("play"), ::readPlay)
        return PlaysForUser(username, totalItems, plays)
    }
}
