package com.insa.mygameslist

import Etats_Navigation.Game_Info
import Etats_Navigation.Home
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import coil3.compose.AsyncImage
import coil3.request.colorSpace
import com.insa.mygameslist.data.Game
import com.insa.mygameslist.data.IGDB
import com.insa.mygameslist.ui.theme.MyGamesListTheme


//@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AfficherListeJeux(igdb: IGDB, backStack: MutableList<Any>){
    Scaffold(topBar = {
        TopAppBar(
            colors = topAppBarColors(
                containerColor = Color.Cyan,
                titleContentColor = Color.Black,
            ),
            title = { Text("My Games List") })
    },
        contentWindowInsets = WindowInsets.systemBars,
        modifier = Modifier.fillMaxSize()) { innerPadding ->
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(igdb.games) { game ->
                    AfficheDonneesJeu(game,backStack)

                }
            }
    }

}

@Composable
fun AfficheDonneesJeu(game: Game,backStack: MutableList<Any>){
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.clickable(onClick = {backStack.add(Game_Info(game.id))})) {
        val coverId: Long = game.cover
        AsyncImage(
            model = "https:${IGDB.coversMap.get(coverId)}",
            contentDescription = null
        )
        Column(){
                Text(game.name)
                Text("Genres :")
                Row(){
                    Text(game.genres.filter { IGDB.genresMap.containsKey(it) }
                        .joinToString(", ") { IGDB.genresMap.get(it)!! }, overflow = TextOverflow.Ellipsis, maxLines = 1)

                }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AfficherDetailsJeu(igdb: IGDB, backStack: MutableList<Any>, id:Long){
    Scaffold(topBar = {
        TopAppBar(
            colors = topAppBarColors(
                containerColor = Color.Cyan,
                titleContentColor = Color.Black,
            ),
            title = {
                IGDB.gamesMap.get(id)?.let { it1 -> Text(it1.name) }
            },
            navigationIcon = {
                IconButton(onClick = {backStack.removeLastOrNull()}){
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_back_24),
                        contentDescription = ""
                    )
                }
            }
        )
    },
        contentWindowInsets = WindowInsets.systemBars,
        modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)){
                IGDB.gamesMap.get(id)?.let { it1 -> Text(it1.name) }
                val currentgame: Game? = IGDB.gamesMap.get(id)
                //TODO
                //FAIRE UNE SEULE MAP AVEC UN GAME QUI A TOUTES LES INFOS
                //cf game Complet

                AsyncImage(
                    model = "https:${IGDB.coversMap.get(currentgame?.cover )}",
                    contentDescription = null
                )

                Text("Description : ${currentgame?.summary}")


                Row(){
                }
            }
//        Text(
//            "ID du jeu : ${id}",
//            modifier = Modifier.padding(innerPadding)
//        )
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

            MyGamesListTheme {
                NavDisplay(
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() },
                    entryProvider = { key ->
                        when(key){
                            is Home -> NavEntry(key){
                                AfficherListeJeux( IGDB,backStack)
                            }
                            is Game_Info -> NavEntry(key){
                                AfficherDetailsJeu(IGDB,backStack,key.id)
                            }
                            else -> NavEntry(Unit) { Text("Unknown route") }
                        }
                    }
                )

            }
        }
    }
}