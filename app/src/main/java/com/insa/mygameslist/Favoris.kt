package com.insa.mygameslist

import androidx.compose.runtime.MutableState
import com.insa.mygameslist.data.GameComplet
import com.insa.mygameslist.data.IGDB

/**
 * Fonction mise en favori permettant de mettre en favori un jeu, modifiant l'état favori de l'objet GameComplet et dans la map favoriStates.
 * Elle prend en paramètre l'objet GameComplet (le jeu à mettre en favori), et la map favoriStates (à mettre à jour en conséquence).
 **/
fun miseEnFavori(game: GameComplet?, favoriStates: MutableMap<Long, MutableState<Boolean>>){
    if(game != null){
        game.favori = !game.favori
        favoriStates[game.id]?.value = game.favori
    }
}

/**
 * Fonction qui permet de sélectionner tous les favoris, afin de pouvoir n'afficher qu'eux.
 * Elle prend en paramètre l'objet IGDB, l'objet affichage correspondant à la liste des jeux qui doivent être affichés dans la liste, et le booléen etat.
 * "etat" est une variable initialisée dans la main activity, qui sert à déterminer si on affiche actuellement que les favoris ou non, pour pouvoir, en cliquant sur la petite étoile dans la topbar, afficher les favoris ou revenir à l'affichage normal.
 **/
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
