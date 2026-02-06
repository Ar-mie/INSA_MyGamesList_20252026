package com.insa.mygameslist.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.insa.mygameslist.R

object IGDB {

    lateinit var covers: List<Cover>
    lateinit var coversMap: Map<Long,String>
    lateinit var games: List<Game>
    lateinit var genres: List<Genre>
    lateinit var genresMap: Map<Long,String>
    lateinit var platformLogo: List<PlatformLogo>
    lateinit var platform: List<Platform>

    fun load(context: Context) {
        val coversFromJson: List<Cover> = Gson().fromJson(
            context.resources.openRawResource(R.raw.covers).bufferedReader(),
            object : TypeToken<List<Cover>>() {}.type
        )

        covers = coversFromJson
        coversMap = covers.associateBy({ it.id },{it.url})

        val gamesFromJson: List<Game> = Gson().fromJson(
            context.resources.openRawResource(R.raw.games).bufferedReader(),
            object : TypeToken<List<Game>>() {}.type
        )

        games = gamesFromJson

        val genresFromJson: List<Genre> = Gson().fromJson(
            context.resources.openRawResource(R.raw.genres).bufferedReader(),
            object : TypeToken<List<Genre>>() {}.type
        )

        genres = genresFromJson
        genresMap = genres.associateBy({ it.id },{it.name})


        val platLogoFromJson: List<PlatformLogo> = Gson().fromJson(
            context.resources.openRawResource(R.raw.platform_logos).bufferedReader(),
            object : TypeToken<List<PlatformLogo>>() {}.type
        )

        platformLogo = platLogoFromJson

        val platformFromJson: List<Platform> = Gson().fromJson(
            context.resources.openRawResource(R.raw.platforms).bufferedReader(),
            object : TypeToken<List<Platform>>() {}.type
        )

        platform = platformFromJson

    }

}

data class Cover(val id: Long, val url: String)
data class Game(val id:Long, val cover:Long, val firstRealease:Long, val genres:List<Long>, val name: String, val platform: List<Long>, val summary:String, val totalRating:Float)
data class Genre(val id:Long, val name: String)
data class PlatformLogo(val id:Long, val url:String)
data class Platform(val id:Long,val name:String,val platLogo:Long)