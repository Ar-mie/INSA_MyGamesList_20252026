package com.insa.mygameslist

import Etats_Navigation.Game_Info
import Etats_Navigation.Home
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import coil3.compose.AsyncImage
import com.insa.mygameslist.data.GameComplet
import com.insa.mygameslist.data.IGDB
import com.insa.mygameslist.ui.theme.MyGamesListTheme


//@Preview(showBackground = true)

@Composable
fun AfficheDonneesJeu(game: GameComplet?, backStack: MutableList<Any>, favori_states: MutableMap<Long, MutableState<Boolean>>){
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.clickable(onClick = { game?.let { backStack.add(Game_Info(it.id)) } })) {
        AsyncImage(
            model = "https:${game?.cover?.url}",
            contentDescription = null
        )
        Column(){
                game?.name?.let { Text(it) }
                Text("Genres :")
                Row(){
                    if (game != null) {
                        Text(game.genres.joinToString(", ") { it.name }, overflow = TextOverflow.Ellipsis, maxLines = 1)
                    }

                }
                IconButton(onClick = { miseEnFavori(game,favori_states) }) {
                    if(game != null){
                        if(favori_states[game.id]?.value==true){
                            Icon(
                                painter = painterResource(R.drawable.etoile_pleine),
                                contentDescription = ""
                            )
                        }
                        else{
                            Icon(
                                painter = painterResource(R.drawable.etoile_vide),
                                contentDescription = ""
                            )
                        }
                    }

                }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AfficherDetailsJeu(igdb: IGDB, backStack: MutableList<Any>, id: Long, favori_states: MutableMap<Long, MutableState<Boolean>>){
    Scaffold(topBar = {
        TopAppBar(
            colors = topAppBarColors(
                containerColor = Color.Cyan,
                titleContentColor = Color.Black,
            ),
            title = {
                IGDB.gamesMapComplet[id]?.let { it1 -> Text(it1.name) }
            },
            navigationIcon = {
                IconButton(onClick = { backStack.removeLastOrNull() }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_back_24),
                        contentDescription = ""
                    )
                }
            },
            actions = {
                if(favori_states[id]?.value == true){
                    IconButton(onClick = { miseEnFavori(IGDB.gamesMapComplet[id],favori_states) }) {
                        Icon(
                            painter = painterResource(R.drawable.etoile_pleine),
                            contentDescription = ""
                        )
                    }
                }else{
                    IconButton(onClick = { miseEnFavori(IGDB.gamesMapComplet[id],favori_states) }) {
                        Icon(
                            painter = painterResource(R.drawable.etoile_vide),
                            contentDescription = ""
                        )
                    }

                }
            }
        )
    },
        contentWindowInsets = WindowInsets.systemBars,
        modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding), Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                IGDB.gamesMapComplet[id]?.let { it1 ->
                    Text(it1.name,
                        modifier = Modifier.paddingFromBaseline(top = 30.dp),
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        fontSize = 24.sp
                    )
                }
                val currentgame: GameComplet? = IGDB.gamesMapComplet[id]

                AsyncImage(
                    model = "https:${currentgame?.cover?.url}",
                    contentDescription = null,
                    modifier = Modifier.paddingFromBaseline(top = 30.dp)
                )


                if (currentgame != null) {
                    Text(currentgame.genres.joinToString(", ") { it.name }, overflow = TextOverflow.Ellipsis, maxLines = 1,
                        fontStyle = FontStyle.Italic,
                        fontSize = 12.sp,
                        modifier = Modifier.paddingFromBaseline(top = 20.dp)
                    )
                }

                LazyRow(modifier = Modifier.paddingFromBaseline(top = 30.dp)) {
                    if(currentgame != null){
                        items(currentgame.platforms){ platf ->
                            if(igdb.platformLogoMap[platf.platLogo]?.url != null){
                                AsyncImage(
                                    model = "https:${igdb.platformLogoMap[platf.platLogo]?.url}",
                                    contentDescription = null,
                                    modifier = Modifier.height(60.dp).width(80.dp).padding(horizontal = 10.dp),
                                    contentScale = ContentScale.FillBounds,

                                    error = painterResource(R.drawable.baseline_no_photography_24)
                                )

                            }else{
                                Icon(
                                    painter = painterResource(R.drawable.baseline_no_photography_24),
                                    contentDescription = "",
                                    modifier = Modifier.height(60.dp).width(80.dp)
                                )
                            }
                        }

                    }
                }
                Text("Description : ${currentgame?.summary}", modifier = Modifier.paddingFromBaseline(top = 40.dp).padding(15.dp))
            }
    }
}
fun rechercherJeu(igdb: IGDB, recherche : String?,affichage : MutableState<ArrayList<Long>>) {
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
fun AfficherListeJeux(igdb: IGDB, backStack: MutableList<Any>, jeux_a_afficher: MutableState<ArrayList<Long>>, favori_states: MutableMap<Long, MutableState<Boolean>>){
    var champTexte by rememberSaveable { mutableStateOf("")}
    //val resultats = ArrayList<Long>()
    var rechercheEnCours by rememberSaveable { mutableStateOf(false) }
    Scaffold(topBar = {
        TopAppBar(
            colors = topAppBarColors(
                containerColor = Color.Cyan,
                titleContentColor = Color.Black,
            ),
            title = { if(!rechercheEnCours){
                Text("My Games List")
            }else{
                Recherche(
                    query = champTexte,
                    onQueryChange = { input -> champTexte = input
                                    rechercherJeu(igdb, champTexte, jeux_a_afficher)
                                     },
                    onSearch = { input -> champTexte = input
                                rechercherJeu(igdb,champTexte, jeux_a_afficher)
                                },
                    modifier = Modifier)
            } },
            actions ={
                IconButton(onClick = { rechercheEnCours=true }){
                    Icon(
                        painter = painterResource(R.drawable.search_icon),
                        contentDescription = ""
                    )
                }
            }
        )
    },
        contentWindowInsets = WindowInsets.systemBars,
        modifier = Modifier.fillMaxSize()) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            if(jeux_a_afficher.value.isNotEmpty()) {
                items(jeux_a_afficher.value) { idGame ->
                    AfficheDonneesJeu(igdb.gamesMapComplet[idGame], backStack, favori_states)

                }
            }
        }
    }

}

fun miseEnFavori(game: GameComplet?, favori_states: MutableMap<Long, MutableState<Boolean>>){
    if(game != null){
        game.favori = !game.favori
        favori_states[game.id]?.value = game.favori
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        IGDB.load(this)


        enableEdgeToEdge()
        setContent {
            val backStack = remember{ mutableStateListOf<Any>(Home) }
            val listeTotale = ArrayList<Long>()
            val favori_states_map = mutableMapOf<Long, MutableState<Boolean>>()
            for(game in IGDB.gamesMapComplet.values){
                listeTotale.add(game.id)
            }
            for(game in IGDB.gamesMapComplet.values){
                 val fav_state = rememberSaveable { mutableStateOf(game.favori) }
                favori_states_map[game.id] = fav_state
            }
            val jeux_a_afficher = rememberSaveable { mutableStateOf(listeTotale) }

            MyGamesListTheme {
                NavDisplay(
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() },
                    entryProvider = { key ->
                        when(key){
                            is Home -> NavEntry(key){
                                AfficherListeJeux( IGDB, backStack, jeux_a_afficher, favori_states_map)
                            }
                            is Game_Info -> NavEntry(key){
                                AfficherDetailsJeu(IGDB,backStack,key.id,favori_states_map)
                            }
                            else -> NavEntry(Unit) { Text("Unknown route") }
                        }
                    }
                )

            }
        }
    }
}