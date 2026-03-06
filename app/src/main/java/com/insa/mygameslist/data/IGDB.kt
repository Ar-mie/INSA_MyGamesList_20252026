package com.insa.mygameslist.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.insa.mygameslist.R

object IGDB {

    lateinit var covers: List<Cover>
    lateinit var coversMap: Map<Long,Cover>
    lateinit var games: List<Game>
    lateinit var gamesMap: Map<Long,Game>
    lateinit var gamesMapComplet: MutableMap<Long,GameComplet?>
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

        for(game in games){

            val g=ArrayList<Genre>()
            for(id in game.getGenresIds()){
                genresMap[id]?.let { g.add(it) }
            }

            val p=ArrayList<Platform>()
            for(id in game.getPlatIds()){
                platformMap[id]?.let { p.add(it) }
            }

            val gameCplt = coversMap[game.getCoverId()]?.let {
                GameComplet(
                    id = game.getId(),
                    cover = it,
                    firstRelease = game.getFirstRelease(),
                    genres = g,
                    name = game.getName(),
                    platform = p,
                    summary = game.getSummary(),
                    totalRating = game.getTotalRating()
                )
            }

            gamesMapComplet[game.getId()] = gameCplt
        }

    }

}

data class Cover(val id: Long, val url: String)
data class Game(val id:Long, val cover:Long, val firstRelease:Long, val genres:List<Long>, val name: String, val platform: List<Long>, val summary:String, val totalRating:Float){
    fun getId(): Long {
        return id
    }
    fun getCoverId():Long{
        return cover
    }
    fun getFirstRelease():Long{
        return firstRelease
    }
    fun getGenresIds():List<Long>{
        return genres
    }
    fun getName(): String{
        return name
    }
    fun getPlatIds():List<Long>{
        return platform
    }
    fun getSummary():String{
        return summary
    }
    fun getTotalRating():Float{
        return totalRating
    }
}

data class GameComplet(val id:Long, val cover:Cover, val firstRelease:Long, val genres:List<Genre>, val name: String, val platform: List<Platform>, val summary:String, val totalRating:Float)
data class Genre(val id:Long, val name: String)
data class PlatformLogo(val id:Long, val url:String)
data class Platform(val id:Long,val name:String,val platLogo:Long)