package com.insa.mygameslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import com.insa.mygameslist.data.IGDB

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Recherche(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: @Composable () -> Unit = { Text("Chercher un jeu") }
){
    // Track expanded state of search bar
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier
            .semantics { isTraversalGroup = true }
    ) {
        InputField(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = {
                onSearch(query)
                expanded = false
            },
            expanded = false,
            onExpandedChange = { expanded = false },
            placeholder = placeholder
            //leadingIcon = leadingIcon
        )
    }
}