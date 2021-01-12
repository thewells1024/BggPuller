package me.kentkawa.bggpuller.xml.parser

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import me.kentkawa.bggpuller.model.Game
import me.kentkawa.bggpuller.model.Play
import me.kentkawa.bggpuller.model.PlaysForUser
import java.lang.IllegalStateException
import java.time.LocalDate

class PlaysForUserParser : StdDeserializer<PlaysForUser>(PlaysForUser::class.java) {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): PlaysForUser {
        val json = ctxt?.readTree(p) ?: throw IllegalStateException("Cannot read tree")
        val username = json.get("username").textValue()
        val plays = parsePlays(json)
        print(json)
        return PlaysForUser(username, plays)
    }

    fun parsePlays(json: JsonNode): List<Play> {
        val plays = ArrayList<Play>()
        for (jsonPlay in json.get("play")) {
            val jsonGame = jsonPlay.get("item")
            val gameName = jsonGame.get("name").asText()
            val gameId = jsonGame.get("objectid").asInt()
            val game = Game(gameName, gameId)
            val date = LocalDate.parse(jsonPlay.get("date").asText())
            val players = ArrayList<Play.Player>()
            for (playerJson in jsonPlay.get("players").get("player")) {
                val playerUsername = playerJson.get("username").asText()
                val playerName = playerJson.get("name").asText()
                val playerNew = convertXMLBoolean(playerJson.get("new").asInt())
                val playerWon = convertXMLBoolean(playerJson.get("win").asInt())
                players.add(Play.Player(playerUsername, playerName, playerNew, playerWon))
            }
            plays.add(Play(game, date, players))
        }
        return plays
    }

    fun convertXMLBoolean(value: Int): Boolean = if (value != 0 && value != 1) {
        throw IllegalStateException("XML Boolean must be a 0 or 1")
    } else {
        value != 0
    }
}
