package com.insa.mygameslist.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.insa.mygameslist.R

object IGDB {

    lateinit var covers: List<Cover>
    lateinit var coversMap: Map<Long,Cover>
    lateinit var games: List<Game>
    lateinit var gamesMap: Map<Long,Game>
    lateinit var gamesMapComplet : MutableMap<Long,GameComplet>
    lateinit var genres: List<Genre>
    lateinit var genresMap: Map<Long,Genre>
    lateinit var platformLogo: List<PlatformLogo>
    lateinit var platformLogoMap:Map<Long, PlatformLogo>
    lateinit var platform: List<Platform>
    lateinit var platformMap : Map<Long, Platform>

    fun load(context: Context) {
        val coversFromJson: List<Cover> = Gson().fromJson(
            context.resources.openRawResource(R.raw.covers).bufferedReader(),
            object : TypeToken<List<Cover>>() {}.type
        )

        covers = coversFromJson
        coversMap = covers.associateBy({ it.id },{it})

        val gamesFromJson: List<Game> = Gson().fromJson(
            context.resources.openRawResource(R.raw.games).bufferedReader(),
            object : TypeToken<List<Game>>() {}.type
        )

        games = gamesFromJson
        gamesMap = games.associateBy({it.id}, {it} )

        val genresFromJson: List<Genre> = Gson().fromJson(
            context.resources.openRawResource(R.raw.genres).bufferedReader(),
            object : TypeToken<List<Genre>>() {}.type
        )

        genres = genresFromJson
        genresMap = genres.associateBy({ it.id },{it})


        val platLogoFromJson: List<PlatformLogo> = Gson().fromJson(
            context.resources.openRawResource(R.raw.platform_logos).bufferedReader(),
            object : TypeToken<List<PlatformLogo>>() {}.type
        )

        platformLogo = platLogoFromJson
        platformLogoMap = platformLogo.associateBy({ it.id },{it})

        val platformFromJson: List<Platform> = Gson().fromJson(
            context.resources.openRawResource(R.raw.platforms).bufferedReader(),
            object : TypeToken<List<Platform>>() {}.type
        )

        platform = platformFromJson
        platformMap = platform.associateBy ({ it.id },{it})

        gamesMapComplet = mutableMapOf<Long,GameComplet>()

        for(game in games){

            val g=ArrayList<Genre>()
            for(id in game.genres){
                genresMap[id]?.let { g.add(it) }
            }

            val p=ArrayList<Platform>()
            for(id in game.platforms){
                platformMap[id]?.let { p.add(it) }
            }

            val gameCplt = coversMap[game.cover]?.let {
                GameComplet(
                    id = game.id,
                    cover = it,
                    firstRelease = game.firstRelease,
                    genres = g,
                    name = game.name,
                    platforms = p,
                    summary = game.summary,
                    totalRating = game.totalRating,
                    favori = false
                )
            }
            gameCplt?.let { gamesMapComplet.put(game.id, it) }
        }

    }

}

data class Cover(val id: Long, val url: String)
data class Game(val id:Long, val cover:Long, @SerializedName("first_release_date") val firstRelease:Long, val genres:List<Long>, val name: String, val platforms: List<Long>, val summary:String, @SerializedName("total_rating") val totalRating:Float){
}

data class GameComplet(val id:Long, val cover:Cover, val firstRelease:Long, val genres:List<Genre>, val name: String, val platforms: List<Platform>, val summary:String, val totalRating:Float, var favori: Boolean)
data class Genre(val id:Long, val name: String)
data class PlatformLogo(val id:Long, val url:String)
data class Platform(val id:Long,val name:String, @SerializedName("platform_logo") val platLogo:Long)