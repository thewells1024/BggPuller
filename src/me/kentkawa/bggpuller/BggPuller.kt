package me.kentkawa.bggpuller

import com.fasterxml.jackson.dataformat.xml.XmlMapper

class BggPuller(val xmlMapper: XmlMapper) {
    private val BASE_URL = "https://www.boardgamegeek.com/xmlapi2/"
    private val COLLECTION_PATH = "collection"
    private val PLAY_PATH = "plays"
}
