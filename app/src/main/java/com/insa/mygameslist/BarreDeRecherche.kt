package com.insa.mygameslist

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import com.insa.mygameslist.data.IGDB

/**
 * Fonction rechercherJeu qui permet de gérer la recherche d'un jeu en fonction de son nom, ses genres et ses plateformes.
 * Elle prend en paramètre l'objet IGDB, le String "recherche" correspondant à la recherche en cours, ainsi que la liste des jeux actuellement affichés, qui sera modifiée pour prendre en compte la recherche en cours.
 * Lors de l'appel de la fonction dans le fichier MainActivity.kt, le String "recherche" est en réalité l'imput de l'utilisateur.
 **/
fun rechercherJeu(igdb: IGDB, recherche : String?, affichage : MutableState<ArrayList<Long>>) {
    val jeux = ArrayList<Long>()
    if(recherche != null) {
        for (game in igdb.gamesMapComplet.values) {
            var g = false
            if (game.name.contains(recherche, true)) {
                jeux.add(game.id)
                g = true
            }
            for (genre in game.genres) {
                if (genre.name.contains(recherche, true) && !g) {
                    jeux.add(game.id)
                    g = true
                }
            }
            for (plateforme in game.platforms) {
                if (plateforme.name.contains(recherche, true) && !g) {
                    jeux.add(game.id)
                    g = true
                }
            }
        }
    }else{
        for(game in igdb.gamesMapComplet.values){
            jeux.add(game.id)
        }
    }
    affichage.value = jeux
}

/**
 * Fonction composable BarreDeRecherche permettant d'afficher la barre de recherche.
 * Elle prend en paramètre un String query correspondant à la recherche actuelle.
 * Les lambdas onQueryChange et onSearch servent à définir le comportement de la barre de recherche lorsque l'on lance une recherche.
 * Lors de l'appel de la fonction dans le fichier MainActivity.kt, les fonctions passées en paramètre pour onQueryChange et onSearch sont la fonction rechercherJeu définie juste au dessus.
 * Elle prend aussi en paramètre un modifier et un placeholder qui sert à afficher un texte dans la barre de recherche quand rien n'y est écrit. Ce placeholder est défini par défaut à "Chercher un jeu".
 **/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarreDeRecherche(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: @Composable () -> Unit = { Text("Chercher un jeu") }
){
    Box(
        modifier
            .semantics { isTraversalGroup = true }
    ) {
        InputField(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = {
                onSearch(query)
            },
            expanded = false,
            onExpandedChange = { },
            placeholder = placeholder
        )
    }
}