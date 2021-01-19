package me.kentkawa.bggpuller.xml.parser

import com.fasterxml.jackson.databind.JsonNode
import me.kentkawa.bggpuller.model.CollectionEntry
import me.kentkawa.bggpuller.model.Game
import me.kentkawa.bggpuller.model.Play
import java.lang.IllegalStateException
import java.time.LocalDate

fun readCollectionEntry(entry: JsonNode): CollectionEntry {
    val game = readGameFromCollectionEntry(entry)
    val rating = entry.get("stats").get("rating").get("value").asDouble()
    return CollectionEntry(game, rating)
}

fun readGameFromCollectionEntry(entry: JsonNode): Game {
    val gameName = entry.get("name").get("").asText()
    val gameId = entry.get("objectid").asInt()
    return Game(gameName, gameId)
}

fun readPlay(play: JsonNode): Play {
    val game = readGame(play.get("item"))
    val date = LocalDate.parse(play.get("date").asText())
    val players = handlePossibleList(play.get("players").get("player"), ::readPlayer)
    return Play(game, date, players)
}

fun readGame(game: JsonNode): Game {
    val gameName = game.get("name").asText()
    val gameId = game.get("objectid").asInt()
    return Game(gameName, gameId)
}

fun readPlayer(player: JsonNode): Play.Player {
    val username = player.get("username").asText()
    val name = player.get("name").asText()
    val new = readXmlBoolean(player.get("new").asInt())
    val won = readXmlBoolean(player.get("win").asInt())
    return Play.Player(username, name, new, won)
}

fun readXmlBoolean(value: Int): Boolean = if (value != 0 && value != 1) {
    throw IllegalStateException("XML Boolean must be a 0 or 1")
} else {
    value != 0
}

fun <T> handlePossibleList(possibleList: JsonNode, converter: (JsonNode) -> T): List<T> {
    return if (possibleList.isArray) {
        val convertedValues = ArrayList<T>()
        for (listItem in possibleList) {
            convertedValues.add(converter(listItem))
        }
        convertedValues
    } else {
        listOf(converter(possibleList))
    }
}
