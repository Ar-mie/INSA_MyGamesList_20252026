package com.insa.mygameslist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.insa.mygameslist.data.GameComplet
import com.insa.mygameslist.data.IGDB

//
fun miseEnFavori(game: GameComplet?, favoriStates: MutableMap<Long, MutableState<Boolean>>){
    if(game != null){
        game.favori = !game.favori
        favoriStates[game.id]?.value = game.favori
    }
}

fun selectionnerFavoris(igdb: IGDB, affichage : MutableState<ArrayList<Long>>, etat : MutableState<Boolean>){
    val jeux = ArrayList<Long>()
    if(!etat.value){
        for(game in igdb.gamesMapComplet.values){
            if(game.favori){
                jeux.add(game.id)
            }
        }
        etat.value = true
    }else{
        for(game in igdb.gamesMapComplet.values){
            jeux.add(game.id)
        }
        etat.value = false
    }
    affichage.value = jeux
}
